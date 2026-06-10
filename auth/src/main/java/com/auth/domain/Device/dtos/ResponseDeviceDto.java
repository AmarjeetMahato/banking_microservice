package com.auth.domain.Device.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDeviceDto {

    private String id;

    private String deviceName;

    private String deviceType;

    private String os;

    private String browser;

    private String ipAddress;

    private String userAgent;

    private String deviceFingerprint;

    private String deviceHash;

    private Boolean isActive;

    private Boolean isTrusted;

    private Boolean isBlocked;

    private LocalDateTime lastLoginAt;

    private LocalDateTime expiresAt;

    private String userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}