package com.auth.domain.Device.services;

import com.auth.domain.Device.dtos.CreateDeviceDto;
import com.auth.domain.Device.dtos.GeoLocation;
import com.auth.domain.Device.dtos.ResponseDeviceDto;
import com.auth.domain.Device.entity.Device;
import com.auth.domain.Users.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DeviceService {


    ResponseDeviceDto createDevice(CreateDeviceDto dto, User user, String refreshToken);

    ResponseDeviceDto getByDeviceId(String deviceId);

    Device upsertDevice(CreateDeviceDto dto, User user, HttpServletRequest request, String refreshToken);

    Optional<Device> findByFingerprint(String fingerprint, String userId);

    List<ResponseDeviceDto> findTrustedDevices(String userId);

}
