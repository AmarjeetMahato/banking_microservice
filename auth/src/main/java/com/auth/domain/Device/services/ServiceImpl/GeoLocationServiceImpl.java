package com.auth.domain.Device.services.ServiceImpl;

import com.auth.domain.Device.dtos.GeoLocation;
import com.auth.domain.Device.services.GeoLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeoLocationServiceImpl implements GeoLocationService {
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
