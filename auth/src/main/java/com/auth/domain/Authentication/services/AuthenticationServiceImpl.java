package com.auth.domain.Authentication.services;


import com.auth.domain.Authentication.dtos.AuthLoginDto;
import com.auth.domain.Authentication.dtos.CreateUserDto;
import com.auth.domain.Authentication.dtos.VerifyEmailDto;
import com.auth.domain.Device.dtos.CreateDeviceDto;
import com.auth.domain.Device.services.DeviceService;
import com.auth.domain.Device.services.DeviceUtil;
import com.auth.domain.Tokens.dtos.TokenCreateDto;
import com.auth.domain.Tokens.dtos.TokenResponseDto;
import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Tokens.enums.TokenType;
import com.auth.domain.Tokens.repository.TokenRepository;
import com.auth.domain.Tokens.services.TokenService;
import com.auth.domain.Users.dtos.UserDto;
import com.auth.domain.Users.entity.User;
import com.auth.domain.Users.mapper.UserMapper;
import com.auth.domain.Users.repository.UserRepository;
import com.auth.globalException.BadRequestException;
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
import org.springframework.stereotype.Service;

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
        log.info("Initiating email verification process for email: {}");

        Token token = tokenService.validateEmailVerificationToken(
                verifyEmailDto.getEmail(), verifyEmailDto.getToken());

        User user = token.getUser();

        user.setEmailVerified(true);

        tokenService.revokeToken(token);

        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        CreateDeviceDto deviceDto = deviceUtil.extractDevice(request);

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
    public String login(AuthLoginDto dto) {
        return "";
    }
}
