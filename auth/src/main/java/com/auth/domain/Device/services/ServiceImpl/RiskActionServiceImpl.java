package com.auth.domain.Device.services.ServiceImpl;


import com.auth.domain.Device.services.RiskActionService;
import com.auth.domain.Emails.Services.EmailService;
import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Tokens.services.TokenService;
import com.auth.domain.Users.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskActionServiceImpl  implements RiskActionService {


    private final TokenService tokenService;
    private final EmailService emailService;

    @Override
    public String handleSuspiciousLogin(User user) {

        Token token = tokenService.createLoginVerificationToken(user);

        emailService.sendSuspiciousLoginEmail(
                user,
                token.getTokenValue()
        );

        return "Suspicious login detected. Verification email sent.";
    }
}
