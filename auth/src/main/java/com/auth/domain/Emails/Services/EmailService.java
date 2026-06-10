package com.auth.domain.Emails.Services;

import com.auth.domain.Users.entity.User;

public interface EmailService {

   void sendSuspiciousLoginEmail(User user, String  token);

    void sendEmailVerificationOtp(User user, String secureOtp);
}
