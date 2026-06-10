package com.auth.domain.Device.services.ServiceImpl;


import com.auth.domain.Device.dtos.CreateDeviceDto;
import com.auth.domain.Device.dtos.GeoLocation;
import com.auth.domain.Device.dtos.ResponseDeviceDto;
import com.auth.domain.Device.entity.Device;
import com.auth.domain.Device.mapper.DeviceMapper;
import com.auth.domain.Device.repository.DeviceRepository;
import com.auth.domain.Device.services.DeviceService;
import com.auth.domain.Device.services.GeoLocationService;
import com.auth.domain.Users.entity.User;
import com.auth.domain.Users.repository.UserRepository;
import com.auth.globalException.BadRequestException;
import com.auth.globalException.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    private  final DeviceRepository deviceRepository;
    private  final DeviceMapper deviceMapper;
    private  final GeoLocationService geoLocationService;

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
        try {
            Device device = deviceRepository.findById(deviceId).orElseThrow(()->
                    new ResourceNotFoundException("Device not found")
            );
            return  deviceMapper.toResponseDto(device);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Device upsertDevice(CreateDeviceDto dto, User user, HttpServletRequest request, String refreshToken) {
        String ip = getClientIp(request);
        GeoLocation geo = geoLocationService.getLocation(ip);
        Optional<Device> existingDevice =
                deviceRepository.findByDeviceFingerprintAndUserId(
                        dto.getDeviceFingerprint(),
                        user.getId()
                );

        Device device = existingDevice.orElseGet(Device::new);

        device.setUser(user);

        device.setDeviceFingerprint(dto.getDeviceFingerprint());
        device.setDeviceHash(dto.getDeviceHash());

        device.setDeviceName(dto.getDeviceName());
        device.setDeviceType(dto.getDeviceType());
        device.setOs(dto.getOs());
        device.setOsVersion(dto.getOsVersion());
        device.setPlatform(dto.getPlatform());
        device.setBrowser(dto.getBrowser());
        device.setBrowserVersion(dto.getBrowserVersion());

        device.setIpAddress(dto.getIpAddress());
        device.setUserAgent(dto.getUserAgent());

        device.setCountry(geo.getCountry());
        device.setRegion(geo.getRegion());
        device.setCity(geo.getCity());
        device.setTimezone(geo.getTimezone());
        device.setLanguage(geo.getLanguage());

        device.setRefreshToken(refreshToken);
        device.setIsActive(true);
        device.setIsBlocked(false);
        device.setIsTrusted(true);

        device.setLastLoginAt(LocalDateTime.now());
        device.setExpiresAt(LocalDateTime.now().plusDays(30));

        return deviceRepository.save(device);
    }



    @Override
    public Optional<Device> findByFingerprint(String fingerprint, String userId) {
        return deviceRepository.findByDeviceFingerprintAndUserId(fingerprint, userId);
    }

    @Override
    public List<Device> findTrustedDevices(String userId) {
        return deviceRepository.findTrustedDevices(userId);
    }

    private String getClientIp(HttpServletRequest request) {

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
