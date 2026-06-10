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


    Device createDevice(CreateDeviceDto dto, User user, String refreshToken);

    ResponseDeviceDto getByDeviceId(String deviceId);

    Device upsertDevice(CreateDeviceDto dto, User user, HttpServletRequest request, String refreshToken);

    Optional<Device> findByFingerprint(String fingerprint, String userId);

    List<Device> findTrustedDevices(String userId);

    @Service
    @Slf4j
    @RequiredArgsConstructor
    class GeoLocationServiceImpl  implements GeoLocationService {

        private final RestTemplate restTemplate = new RestTemplate();

        @Override
        public GeoLocation getLocation(String ip) {

            String url = "https://ipapi.co/" + ip + "/json/";

            ResponseEntity<Map> response =
                    restTemplate.getForEntity(url, Map.class);

            Map body = response.getBody();

            if (body == null) {
                return GeoLocation.builder().ip(ip).build();
            }

            return GeoLocation.builder()
                    .ip(ip)
                    .country((String) body.get("country_name"))
                    .region((String) body.get("region"))
                    .city((String) body.get("city"))
                    .timezone((String) body.get("timezone"))
                    .build();
        }
    }
}
