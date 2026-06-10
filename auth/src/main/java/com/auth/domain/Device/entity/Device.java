package com.auth.domain.Device.entity;


import com.auth.base.BaseEntity;
import com.auth.domain.Users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Device extends BaseEntity {

    // ---------------- DEVICE INFO ----------------
    @Column(name = "device_name", nullable = false)
    private String deviceName; // e.g. Chrome on Windows

    @Column(name = "device_type", nullable = false)
    private String deviceType; // MOBILE / DESKTOP / TABLET

    @Column(name = "os", nullable = false)
    private String os; // Windows, Android, iOS

    @Column(name = "os_version")
    private String osVersion;

    @Column(name = "platform")
    private String platform;

    @Column(name = "browser")
    private String browser;

    @Column(name = "browser_version")
    private String browserVersion;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "user_agent", length = 1000)
    private String userAgent;

    @Column(name = "device_fingerprint", nullable = false, unique = true, length = 512)
    private String deviceFingerprint;

    @Column(name = "device_hash", length = 256)
    private String deviceHash;



    // ---------------- COUNTRY ----------------
    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "language")
    private String language;


    // ---------------- AUTH / SECURITY ----------------
    @Column(name = "refresh_token", length = 2000)
    private String refreshToken;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_trusted", nullable = false)
    private Boolean isTrusted;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    // ---------------- SESSION ----------------
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    // ---------------- USER RELATION ----------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
