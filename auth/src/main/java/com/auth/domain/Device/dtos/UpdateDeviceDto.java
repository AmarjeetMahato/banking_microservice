package com.auth.domain.Device.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDeviceDto {
    @Size(min = 2, max = 100,
            message = "Device name must be between 2 and 100 characters")
    private String deviceName;

    @Size(max = 100,
            message = "Browser name must not exceed 100 characters")
    private String browser;

    @Size(max = 2000,
            message = "Refresh token must not exceed 2000 characters")
    private String refreshToken;

    private Boolean isActive;

    private Boolean isTrusted;

    private Boolean isBlocked;
}
