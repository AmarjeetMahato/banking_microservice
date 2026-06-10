package com.auth.domain.Emails.Services;


import com.auth.domain.Users.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailServiceImpl implements  EmailService {
    @Override
    public void sendSuspiciousLoginEmail(User user, String token) {

    }

    @Override
    public void sendEmailVerificationOtp(User user, String secureOtp) {

    }
}
