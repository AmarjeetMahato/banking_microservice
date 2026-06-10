package com.auth.domain.Device.services;


import com.auth.domain.Device.dtos.CreateDeviceDto;
import com.auth.domain.Device.dtos.ResponseDeviceDto;
import com.auth.domain.Device.entity.Device;
import com.auth.domain.Device.mapper.DeviceMapper;
import com.auth.domain.Device.repository.DeviceRepository;
import com.auth.domain.Users.entity.User;
import com.auth.domain.Users.repository.UserRepository;
import com.auth.globalException.BadRequestException;
import com.auth.globalException.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    private  final DeviceRepository deviceRepository;
    private  final UserRepository userRepository;
    private  final DeviceMapper deviceMapper;

    @Override
    @Transactional
    public Device createDevice(CreateDeviceDto deviceDto, User user, String refreshToken) {

        if (deviceRepository.existsByDeviceFingerprint(
                deviceDto.getDeviceFingerprint())) {
            throw new BadRequestException("A device with this fingerprint already exists");
        }

        if (deviceDto.getDeviceHash() != null
                && deviceRepository.existsByDeviceHash(
                deviceDto.getDeviceHash())) {

            throw new BadRequestException("A device with this hash already exists");
        }

        Device device = deviceMapper.toEntity(deviceDto, user);

        device.setRefreshToken(refreshToken);
        device.setIsActive(true);
        device.setIsTrusted(false);
        device.setIsBlocked(false);
        device.setLastLoginAt(LocalDateTime.now());

        return deviceRepository.save(device);
    }

    @Override
    public ResponseDeviceDto getByDeviceId(String deviceId) {
        return null;
    }
}
