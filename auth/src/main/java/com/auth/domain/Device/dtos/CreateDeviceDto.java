package com.auth.domain.Device.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDeviceDto {

    @NotBlank(message = "Device name is required")
    private String deviceName;

    @NotBlank(message = "Device type is required")
    private String deviceType;

    @NotBlank(message = "OS is required")
    private String os;

    private String osVersion;

    private String browser;

    private  String language;

    private String timezone;
    private String country;
    private String region;
    private String city;

    private  String browserVersion;

    private String platform;

    @NotBlank(message = "IP Address is required")
    private String ipAddress;

    private String userAgent;

    @NotBlank(message = "Device fingerprint is required")
    private String deviceFingerprint;

    private String deviceHash;

    private String refreshToken;
}