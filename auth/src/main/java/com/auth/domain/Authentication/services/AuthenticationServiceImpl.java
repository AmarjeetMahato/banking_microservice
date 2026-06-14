package com.auth.domain.Authentication.services;


import com.auth.domain.Authentication.dtos.AuthLoginDto;
import com.auth.domain.Authentication.dtos.VerifyEmailDto;
import com.auth.domain.Device.dtos.CreateDeviceDto;
import com.auth.domain.Device.dtos.DeviceContext;
import com.auth.domain.Device.dtos.GeoLocation;
import com.auth.domain.Device.entity.Device;
import com.auth.domain.Device.services.DeviceSecurityService;
import com.auth.domain.Device.services.DeviceService;
import com.auth.domain.Device.services.GeoLocationService;
import com.auth.domain.Device.services.ServiceImpl.DeviceUtil;
import com.auth.domain.Device.services.RiskActionService;
import com.auth.domain.Emails.Services.EmailService;
import com.auth.domain.Tokens.dtos.TokenCreateDto;
import com.auth.domain.Tokens.dtos.TokenResponseDto;
import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Tokens.enums.TokenType;
import com.auth.domain.Tokens.services.TokenService;
import com.auth.domain.Users.dtos.UserDto;
import com.auth.domain.Users.entity.User;
import com.auth.domain.Users.mapper.UserMapper;
import com.auth.domain.Users.repository.UserRepository;
import com.auth.globalException.BadCredentialsException;
import com.auth.globalException.ResourceAlreadyExistsException;
import com.auth.helpers.JwtCookieService;
import com.auth.springsecurity.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements  AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private  final TokenService tokenService;
    private  final DeviceService deviceService;
    private  final JwtCookieService jwtCookieService;
    private  final JwtService jwtService;
    private final DeviceUtil deviceUtil;
    private  final PasswordEncoder passwordEncoder;
    private final DeviceSecurityService deviceSecurityService;
    private  final RiskActionService riskActionService;
    private  final GeoLocationService geoLocationService;
    private final TaskExecutor taskExecutor;
    private  final EmailService emailService;

    @Override
    public void CreateUser(UserDto dto) {

        try {
            // Check if email already exists
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new ResourceAlreadyExistsException("User already registered with this email");
            }

            // Check if phone already exists (only if phone is provided)
            if (dto.getPhone() != null && userRepository.existsByPhone(dto.getPhone())) {
                throw new ResourceAlreadyExistsException("User already registered with this phone number");
            }


            User user = userMapper.toEntity(dto);
            User savedUser = userRepository.save(user);

            TokenCreateDto tokenDto = TokenCreateDto.builder()
                    .userId(savedUser.getId())
                    .tokenType(TokenType.EMAIL_VERIFICATION)
                    .build();
            TokenResponseDto tokenResponse =  tokenService.createToken(tokenDto);
            // ── 4. Hand off email to thread pool (non-blocking) ───────────────────
            //    Pass primitives / value copies — never pass the JPA-managed entity
            //    into an async task (session will be closed by the time it runs).
            String userId    = savedUser.getId();
            String firstName = savedUser.getFirstName();
            String email     = savedUser.getEmail();
            String otp       = tokenResponse.getTokenValue();   // the raw OTP / secure token

            taskExecutor.execute(() -> dispatchVerificationEmail(userId, firstName, email, otp));

            return ;
        } catch (ResourceAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Runs on email-async-* thread — completely isolated from the HTTP thread
    // ─────────────────────────────────────────────────────────────────────────
    private void dispatchVerificationEmail(
            String userId,
            String firstName,
            String email,
            String otp
    ) {
        try {
            log.info("[email-thread] Sending verification OTP to userId={}", userId);

            // Build a detached User object — just enough data for the template
            User emailUser = User.builder()
                    .id(userId)
                    .firstName(firstName)
                    .email(email)
                    .build();

            emailService.sendEmailVerificationOtp(emailUser, otp);

            log.info("[email-thread] Verification email dispatched userId={}", userId);

        } catch (Exception ex) {
            // Never propagate — this is a background thread.
            // In production: push to a retry queue / outbox table here.
            log.error("[email-thread] Failed to send verification email userId={} reason={}",
                    userId, ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public String verifyEmail(VerifyEmailDto verifyEmailDto,  HttpServletRequest request,
                              HttpServletResponse response) {
        log.info("Initiating email verification process for email: {}", verifyEmailDto.getEmail());

        Token token = tokenService.validateEmailVerificationToken(
                verifyEmailDto.getEmail(), verifyEmailDto.getToken());

        User user = token.getUser();

        user.setEmailVerified(true);

        tokenService.revokeToken(token);

        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        CreateDeviceDto deviceDto = deviceUtil.extractDevice(request);
        GeoLocation geo =
                geoLocationService.getLocation(
                        deviceDto.getIpAddress()
                );

        deviceDto.setCountry(geo.getCountry());
        deviceDto.setRegion(geo.getRegion());
        deviceDto.setCity(geo.getCity());
        deviceDto.setTimezone(geo.getTimezone());

        deviceService.createDevice(deviceDto, user, refreshToken);

        ResponseCookie accessCookie = jwtCookieService.createAccessCookie(accessToken);

        ResponseCookie refreshCookie = jwtCookieService.createRefreshCookie(refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        log.info(
                "Email verified successfully for user {}",
                user.getEmail());

        return "Email Verify successfully";
    }



    @Override
    @Transactional
    public String login(AuthLoginDto dto, HttpServletRequest request, HttpServletResponse response) {

        log.info("Login attempt for email: {}", dto.getPassword());

        // 1. Find user
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // 2. Check password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // 3. Check account status
        if (!user.isActive()) {
            throw new RuntimeException("Account is disabled");
        }

        if (user.isBlocked()) {
            throw new RuntimeException("Account is blocked");
        }

        // 4. Email verification check
        if (!user.isEmailVerified()) {
            // optionally resend verification token
            tokenService.generateEmailVerificationToken(user);
            throw new RuntimeException("Email not verified. Please verify your email.");
        }

        // 5. Extract device info
        CreateDeviceDto deviceDto = deviceUtil.extractDevice(request);

        String ip = deviceUtil.getClientIp(request);

        GeoLocation geo = geoLocationService.getLocation(ip);

        DeviceContext context = DeviceContext.builder()
                .device(deviceDto)
                .ipAddress(ip)
                .country(geo.getCountry())
                .region(geo.getRegion())
                .city(geo.getCity())
                .timezone(geo.getTimezone())
                .build();



        Optional<Device> existingDeviceOpt =
                deviceService.findByFingerprint(deviceDto.getDeviceFingerprint(), user.getId());

        boolean suspicious = false;

         suspicious = deviceSecurityService.isSuspiciousDevice(deviceDto,context, user.getId());

        if (suspicious) {
            return riskActionService.handleSuspiciousLogin(user);
        }
        // 8. Generate tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // 9. Save / Update device session
        Device device = existingDeviceOpt.orElseGet(Device::new);

        // 10. Create cookies
        ResponseCookie accessCookie =
                jwtCookieService.createAccessCookie(accessToken);

        ResponseCookie refreshCookie =
                jwtCookieService.createRefreshCookie(refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // 11. Audit log
//        auditService.logLogin(
//                user,
//                deviceDto.getIpAddress(),
//                deviceDto.getCountry(),
//                deviceDto.getDeviceFingerprint(),
//                true
//        );

        log.info("Login successful for user: {}", user.getEmail());

        return "Login successful";
    }
}
