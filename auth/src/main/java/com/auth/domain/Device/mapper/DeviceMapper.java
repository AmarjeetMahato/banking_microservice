package com.auth.domain.Device.mapper;


import com.auth.domain.Device.dtos.CreateDeviceDto;
import com.auth.domain.Device.dtos.ResponseDeviceDto;
import com.auth.domain.Device.dtos.UpdateDeviceDto;
import com.auth.domain.Device.entity.Device;
import com.auth.domain.Users.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DeviceMapper {

    public Device toEntity(CreateDeviceDto dto, User user) {
        return Device.builder()
                .deviceName(dto.getDeviceName())
                .deviceType(dto.getDeviceType())
                .os(dto.getOs())
                .browser(dto.getBrowser())
                .ipAddress(dto.getIpAddress())
                .userAgent(dto.getUserAgent())
                .deviceFingerprint(dto.getDeviceFingerprint())
                .deviceHash(dto.getDeviceHash())
                .refreshToken(dto.getRefreshToken())
                .isActive(true)
                .isTrusted(false)
                .isBlocked(false)
                .lastLoginAt(LocalDateTime.now())
                .user(user)
                .build();
    }

    public void updateEntity(Device device, UpdateDeviceDto dto) {

        if (dto.getDeviceName() != null) {
            device.setDeviceName(dto.getDeviceName());
        }

        if (dto.getBrowser() != null) {
            device.setBrowser(dto.getBrowser());
        }

        if (dto.getRefreshToken() != null) {
            device.setRefreshToken(dto.getRefreshToken());
        }

        if (dto.getIsActive() != null) {
            device.setIsActive(dto.getIsActive());
        }

        if (dto.getIsTrusted() != null) {
            device.setIsTrusted(dto.getIsTrusted());
        }

        if (dto.getIsBlocked() != null) {
            device.setIsBlocked(dto.getIsBlocked());
        }
    }


    public  ResponseDeviceDto toResponseDto(Device device) {
        return ResponseDeviceDto.builder()
                .id(device.getId())
                .deviceName(device.getDeviceName())
                .deviceType(device.getDeviceType())
                .os(device.getOs())
                .browser(device.getBrowser())
                .ipAddress(device.getIpAddress())
                .userAgent(device.getUserAgent())
                .deviceFingerprint(device.getDeviceFingerprint())
                .deviceHash(device.getDeviceHash())
                .isActive(device.getIsActive())
                .isTrusted(device.getIsTrusted())
                .isBlocked(device.getIsBlocked())
                .lastLoginAt(device.getLastLoginAt())
                .expiresAt(device.getExpiresAt())
                .userId(device.getUser().getId())
                .createdAt(device.getCreatedAt())
                .updatedAt(device.getUpdatedAt())
                .build();
    }
}
