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

    @Override
    public String CreateUser(UserDto dto) {

        try {
            if(userRepository.findByEmail(dto.getEmail()).isPresent()){
                  throw  new ResourceAlreadyExistsException("User already register with this email");
            }
            User user = userMapper.toEntity(dto);
            User savedUser = userRepository.save(user);

            TokenCreateDto tokenDto = TokenCreateDto.builder()
                    .userId(savedUser.getId())
                    .tokenType(TokenType.EMAIL_VERIFICATION)
                    .build();
            TokenResponseDto tokenResponse =  tokenService.createToken(tokenDto);
            return  "User registered successfully.";
        } catch (ResourceAlreadyExistsException e) {
            throw new RuntimeException(e);
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
