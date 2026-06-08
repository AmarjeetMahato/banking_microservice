package com.auth.domain.Authentication.services;


import com.auth.domain.Authentication.dtos.AuthLoginDto;
import com.auth.domain.Authentication.dtos.CreateUserDto;
import com.auth.domain.Authentication.dtos.VerifyEmailDto;
import com.auth.domain.Tokens.dtos.TokenCreateDto;
import com.auth.domain.Tokens.dtos.TokenResponseDto;
import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Tokens.enums.TokenType;
import com.auth.domain.Tokens.services.TokenService;
import com.auth.domain.Users.dtos.UserDto;
import com.auth.domain.Users.entity.User;
import com.auth.domain.Users.mapper.UserMapper;
import com.auth.domain.Users.repository.UserRepository;
import com.auth.globalException.BadRequestException;
import com.auth.globalException.ResourceAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.ITokenSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements  AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private  final TokenService tokenService;

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

    @Transactional
    public String verifyEmail(VerifyEmailDto verifyEmailDto, HttpServletRequest request) {
        log.info("Initiating email verification process for email: {}", verifyEmailDto.getEmail());

        // 1. DB se OTP aur Type ke match ka Token fetch karein
        Token token = tokenRepository.findByTokenValueAndTokenType(
                verifyEmailDto.getOtpCode(), // Assuming DTO has getOtpCode()
                TokenType.EMAIL_VERIFICATION
        ).orElseThrow(() -> {
            log.error("Verification failed: Invalid OTP code provided.");
            return new BadRequestException("Invalid OTP code or token does not exist.");
        });

        // 2. Security Validation: Kya ye token sahi user ka hai?
        if (!token.getUser().getEmail().equalsIgnoreCase(verifyEmailDto.getEmail())) {
            log.error("Verification failed: OTP does not belong to the requested email.");
            throw new BadRequestException("Invalid token for this email address.");
        }

        // 3. Validation: Kya token pehle hi use/revoke ho chuka hai?
        if (token.isRevoked()) {
            log.error("Verification failed: Token has already been revoked/used.");
            throw new BadRequestException("This OTP has already been used or cancelled.");
        }

        // 4. Validation: Kya token expire ho chuka hai?
        if (token.isExpired() || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            // Safe side ke liye DB me bhi status update kar dete hain
            token.setExpired(true);
            tokenRepository.save(token);
            log.error("Verification failed: Token has expired. Expiry was at: {}", token.getExpiryDate());
            throw new BadRequestException("OTP has expired. Please request a new one.");
        }

        // 5. Success Flow: User ko active mark karein aur token ko consume/revoke karein
        User user = token.getUser();
        user.setEnabled(true); // Ya fir user.setEmailVerified(true) jo bhi aapki field ho
        // userRepository.save(user); // @Transactional automatically save kar dega

        // Token ko revoke kar dete hain taaki ye 6-digit code dobara use na ho sake (Replay Attack Safety)
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);

        log.info("Email verified successfully for user: {}. Token status updated to revoked.", user.getEmail());

        return "Email verified successfully! You can now log in.";
    }

    @Override
    public String login(AuthLoginDto dto) {
        return "";
    }
}
