package com.auth.domain.Emails.Services;


import com.auth.domain.Users.entity.User;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Year;

@Service
@Slf4j
public class EmailServiceImpl implements  EmailService {
    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final String fromName;
    private final int otpExpiryMinutes;
    private final int suspiciousLoginExpiryMinutes;

    // Inject everything through a single constructor
    public EmailServiceImpl(
            JavaMailSender mailSender,
            @Value("${app.mail.from}") String fromEmail,
            @Value("${app.mail.from-name}") String fromName,
            @Value("${app.mail.otp-expiry-minutes:10}") int otpExpiryMinutes,
            @Value("${app.mail.suspicious-login-expiry-minutes:30}") int suspiciousLoginExpiryMinutes) {

        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        this.fromName = fromName;
        this.otpExpiryMinutes = otpExpiryMinutes;
        this.suspiciousLoginExpiryMinutes = suspiciousLoginExpiryMinutes;
    }
    // ─────────────────────────────────────────────────────────────────────────
    // PUBLIC METHODS
    // ─────────────────────────────────────────────────────────────────────────

    @Async
    @Override
    public void sendEmailVerificationOtp(User user, String secureOtp) {
        try {
            String subject = "Your Verification Code – " + secureOtp;
            String html = buildOtpEmailHtml(user.getFirstName(), secureOtp);
            sendHtmlEmail(user.getEmail(), subject, html);
            log.info("OTP verification email sent to userId={} email={}",
                    user.getId(), maskEmail(user.getEmail()));
        } catch (Exception ex) {
            log.error("Failed to send OTP email to userId={}", user.getId(), ex);
            // Don't rethrow — email failure should never crash the auth flow
            // In production: publish to a dead-letter queue for retry
        }
    }

    @Async
    @Override
    public void sendSuspiciousLoginEmail(User user, String token) {
        try {
            String subject = "⚠️ Suspicious Login Detected – Action Required";
            String html = buildSuspiciousLoginHtml(user.getFirstName(), token);
            sendHtmlEmail(user.getEmail(), subject, html);
            log.info("Suspicious login email sent to userId={} email={}",
                    user.getId(), maskEmail(user.getEmail()));
        } catch (Exception ex) {
            log.error("Failed to send suspicious login email to userId={}", user.getId(), ex);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    private void sendHtmlEmail(String to, String subject, String html)
            throws MessagingException, UnsupportedEncodingException, jakarta.mail.MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(new InternetAddress(fromEmail, fromName));
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true); // true = isHtml
        helper.setReplyTo(fromEmail);

        mailSender.send(message);
    }

    private String maskEmail(String email) {
        // logs user@example.com as u***@example.com
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) return "***";
        return email.charAt(0) + "***" + email.substring(atIndex);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // OTP EMAIL TEMPLATE
    // ─────────────────────────────────────────────────────────────────────────

    private String buildOtpEmailHtml(String firstName, String otp) {
        // Split OTP digits for individual styled boxes
        String[] digits = otp.split("");
        StringBuilder digitBoxes = new StringBuilder();
        for (String d : digits) {
            digitBoxes.append("""
                <td style="padding:4px;">
                  <div style="
                    width:48px; height:56px; background:#F7F8FF;
                    border:2px solid #E0E3FF; border-radius:10px;
                    font-size:28px; font-weight:700; color:#1A1D3B;
                    text-align:center; line-height:56px;
                    font-family:'Courier New', monospace; letter-spacing:0;">
                    %s
                  </div>
                </td>
            """.formatted(d));
        }

        int currentYear = Year.now().getValue();

        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
          <meta charset="UTF-8"/>
          <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
          <title>Email Verification</title>
        </head>
        <body style="margin:0;padding:0;background:#F0F2FF;font-family:'Segoe UI',Arial,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0" style="background:#F0F2FF;padding:40px 0;">
            <tr><td align="center">
              <table width="560" cellpadding="0" cellspacing="0"
                style="background:#ffffff;border-radius:16px;overflow:hidden;
                       box-shadow:0 4px 24px rgba(74,85,240,0.10);">

                <!-- Header -->
                <tr>
                  <td style="background:linear-gradient(135deg,#4A55F0 0%%,#7B5EF8 100%%);
                              padding:36px 40px; text-align:center;">
                    <div style="font-size:32px;margin-bottom:8px;">🔐</div>
                    <div style="color:#ffffff;font-size:22px;font-weight:700;
                                letter-spacing:0.3px;">Email Verification</div>
                    <div style="color:#C8CAFF;font-size:14px;margin-top:4px;">
                      YourBank Security
                    </div>
                  </td>
                </tr>

                <!-- Body -->
                <tr>
                  <td style="padding:40px 40px 32px;">

                    <p style="margin:0 0 6px;color:#6B7280;font-size:14px;font-weight:500;">
                      Hello,
                    </p>
                    <h2 style="margin:0 0 20px;color:#1A1D3B;font-size:20px;font-weight:700;">
                      %s 👋
                    </h2>

                    <p style="margin:0 0 28px;color:#4B5563;font-size:15px;line-height:1.7;">
                      We received a request to verify your email address.
                      Use the code below to complete your verification.
                      This code is valid for <strong style="color:#4A55F0;">%d minutes</strong>.
                    </p>

                    <!-- OTP boxes -->
                    <table cellpadding="0" cellspacing="0" style="margin:0 auto 28px;">
                      <tr>%s</tr>
                    </table>

                    <!-- Copy hint -->
                    <div style="background:#F7F8FF;border:1px solid #E0E3FF;border-radius:10px;
                                padding:14px 20px;margin-bottom:28px;text-align:center;">
                      <span style="color:#6B7280;font-size:13px;">
                        📋 Copy this code and paste it on the verification page
                      </span>
                    </div>

                    <!-- Warning box -->
                    <div style="background:#FFF7ED;border-left:4px solid #F59E0B;
                                border-radius:0 8px 8px 0;padding:14px 18px;margin-bottom:24px;">
                      <p style="margin:0;color:#92400E;font-size:13px;line-height:1.6;">
                        <strong>⚠️ Security Notice:</strong> Never share this code with anyone.
                        YourBank will <strong>never</strong> ask for your OTP via phone, email, or chat.
                      </p>
                    </div>

                    <p style="margin:0;color:#9CA3AF;font-size:13px;line-height:1.6;">
                      If you didn't request this code, you can safely ignore this email.
                      Your account remains secure.
                    </p>
                  </td>
                </tr>

                <!-- Footer -->
                <tr>
                  <td style="background:#F9FAFB;border-top:1px solid #E5E7EB;
                              padding:24px 40px;text-align:center;">
                    <p style="margin:0 0 8px;color:#6B7280;font-size:12px;">
                      This is an automated security email from YourBank.
                    </p>
                    <p style="margin:0;color:#9CA3AF;font-size:11px;">
                      © %d YourBank · <a href="#" style="color:#4A55F0;text-decoration:none;">Privacy Policy</a>
                      · <a href="#" style="color:#4A55F0;text-decoration:none;">Terms of Service</a>
                    </p>
                  </td>
                </tr>

              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """.formatted(firstName, otpExpiryMinutes, digitBoxes.toString(), currentYear);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SUSPICIOUS LOGIN EMAIL TEMPLATE
    // ─────────────────────────────────────────────────────────────────────────

    private String buildSuspiciousLoginHtml(String firstName, String actionToken) {
        // In production, this URL points to your auth-service endpoint
        // that invalidates all sessions when clicked
        String blockUrl = "https://yourbank.com/api/v1/auth/block-account?token=" + actionToken;
        String safeUrl  = "https://yourbank.com/api/v1/auth/confirm-safe?token=" + actionToken;

        int currentYear = Year.now().getValue();

        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
          <meta charset="UTF-8"/>
          <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
          <title>Suspicious Login Alert</title>
        </head>
        <body style="margin:0;padding:0;background:#FFF1F0;font-family:'Segoe UI',Arial,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0" style="background:#FFF1F0;padding:40px 0;">
            <tr><td align="center">
              <table width="560" cellpadding="0" cellspacing="0"
                style="background:#ffffff;border-radius:16px;overflow:hidden;
                       box-shadow:0 4px 24px rgba(239,68,68,0.12);">

                <!-- Alert header -->
                <tr>
                  <td style="background:linear-gradient(135deg,#DC2626 0%%,#EF4444 100%%);
                              padding:36px 40px;text-align:center;">
                    <div style="font-size:40px;margin-bottom:8px;">🚨</div>
                    <div style="color:#ffffff;font-size:22px;font-weight:700;">
                      Suspicious Login Detected
                    </div>
                    <div style="color:#FCA5A5;font-size:14px;margin-top:4px;">
                      Immediate action may be required
                    </div>
                  </td>
                </tr>

                <!-- Alert banner -->
                <tr>
                  <td style="background:#FEF2F2;border-bottom:1px solid #FECACA;
                              padding:14px 40px;text-align:center;">
                    <span style="color:#B91C1C;font-size:13px;font-weight:600;">
                      ⚡ This alert expires in %d minutes
                    </span>
                  </td>
                </tr>

                <!-- Body -->
                <tr>
                  <td style="padding:36px 40px 28px;">
                    <p style="margin:0 0 6px;color:#6B7280;font-size:14px;">Hello,</p>
                    <h2 style="margin:0 0 20px;color:#1A1D3B;font-size:20px;font-weight:700;">
                      %s, we noticed something unusual.
                    </h2>

                    <p style="margin:0 0 24px;color:#4B5563;font-size:15px;line-height:1.7;">
                      A login attempt was made on your account from an
                      <strong style="color:#DC2626;">unrecognized device or location</strong>.
                      If this was you, no action is needed. If not, secure your account immediately.
                    </p>

                    <!-- Login details card -->
                    <div style="background:#F9FAFB;border:1px solid #E5E7EB;
                                border-radius:12px;padding:20px 24px;margin-bottom:28px;">
                      <div style="color:#6B7280;font-size:12px;font-weight:600;
                                  text-transform:uppercase;letter-spacing:0.8px;margin-bottom:16px;">
                        Login Attempt Details
                      </div>
                      <table width="100%%" cellpadding="6" cellspacing="0">
                        <tr>
                          <td style="color:#9CA3AF;font-size:13px;width:40%%;">🕐 Time</td>
                          <td style="color:#1F2937;font-size:13px;font-weight:500;">
                            Just now (UTC)
                          </td>
                        </tr>
                        <tr>
                          <td style="color:#9CA3AF;font-size:13px;">📍 Location</td>
                          <td style="color:#1F2937;font-size:13px;font-weight:500;">
                            Unknown / Unrecognized
                          </td>
                        </tr>
                        <tr>
                          <td style="color:#9CA3AF;font-size:13px;">💻 Device</td>
                          <td style="color:#1F2937;font-size:13px;font-weight:500;">
                            Unrecognized Device
                          </td>
                        </tr>
                        <tr>
                          <td style="color:#9CA3AF;font-size:13px;">🔑 Status</td>
                          <td>
                            <span style="background:#FEF2F2;color:#DC2626;font-size:12px;
                                         font-weight:600;padding:2px 10px;border-radius:20px;">
                              Flagged
                            </span>
                          </td>
                        </tr>
                      </table>
                    </div>

                    <!-- Action buttons -->
                    <p style="margin:0 0 16px;color:#374151;font-size:14px;font-weight:600;">
                      Was this you?
                    </p>

                    <table cellpadding="0" cellspacing="0" width="100%%">
                      <tr>
                        <td style="padding-right:8px;" width="50%%">
                          <a href="%s"
                            style="display:block;background:#DC2626;color:#ffffff;
                                   text-decoration:none;text-align:center;padding:14px;
                                   border-radius:10px;font-size:14px;font-weight:600;">
                            🔒 No, Block My Account
                          </a>
                        </td>
                        <td style="padding-left:8px;" width="50%%">
                          <a href="%s"
                            style="display:block;background:#F0FDF4;color:#15803D;
                                   text-decoration:none;text-align:center;padding:14px;
                                   border-radius:10px;font-size:14px;font-weight:600;
                                   border:1px solid #BBF7D0;">
                            ✅ Yes, It Was Me
                          </a>
                        </td>
                      </tr>
                    </table>

                    <div style="margin-top:24px;background:#FFFBEB;border-left:4px solid #F59E0B;
                                border-radius:0 8px 8px 0;padding:14px 18px;">
                      <p style="margin:0;color:#92400E;font-size:13px;line-height:1.6;">
                        <strong>🔐 Security tip:</strong> If you didn't initiate this login,
                        click <em>"Block My Account"</em> immediately and change your password
                        from a trusted device.
                      </p>
                    </div>
                  </td>
                </tr>

                <!-- Footer -->
                <tr>
                  <td style="background:#F9FAFB;border-top:1px solid #E5E7EB;
                              padding:24px 40px;text-align:center;">
                    <p style="margin:0 0 8px;color:#6B7280;font-size:12px;">
                      YourBank Security Team · We protect your account 24/7
                    </p>
                    <p style="margin:0;color:#9CA3AF;font-size:11px;">
                      © %d YourBank ·
                      <a href="#" style="color:#DC2626;text-decoration:none;">Privacy Policy</a>
                      · <a href="#" style="color:#DC2626;text-decoration:none;">Help Center</a>
                    </p>
                  </td>
                </tr>

              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """.formatted(
                suspiciousLoginExpiryMinutes,
                firstName,
                blockUrl,
                safeUrl,
                currentYear
        );
    }
}
