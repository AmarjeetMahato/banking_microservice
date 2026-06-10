package com.auth.domain.Device.services.ServiceImpl;


import com.auth.domain.Device.dtos.CreateDeviceDto;
import com.auth.domain.Device.dtos.DeviceContext;
import com.auth.domain.Device.entity.Device;
import com.auth.domain.Device.repository.DeviceRepository;
import com.auth.domain.Device.services.DeviceSecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceSecurityServiceImpl implements DeviceSecurityService {

    private final DeviceRepository deviceRepository;

    @Override
    public boolean isSuspiciousDevice(CreateDeviceDto dto, DeviceContext context, String userId) {

        List<Device> trustedDevices = deviceRepository.findTrustedDevices(userId);

        if (trustedDevices.isEmpty()) {
            return false; // first login → trusted
        }
        for (Device device : trustedDevices) {

            boolean sameFingerprint = device.getDeviceFingerprint()
                            .equals(dto.getDeviceFingerprint());

            if (sameFingerprint) return false;

            boolean sameCountry = Objects.equals(device.getCountry(),context.getCountry());

            boolean sameIP = Objects.equals(device.getIpAddress(), dto.getIpAddress());

            // 🔥 Risk rules (customize later)
            if (!sameCountry && !sameIP) {
                return true; // suspicious
            }
        }

        return false;
    }

}
