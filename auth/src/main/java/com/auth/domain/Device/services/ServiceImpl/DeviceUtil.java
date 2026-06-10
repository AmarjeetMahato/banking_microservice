package com.auth.domain.Device.services.ServiceImpl;

import com.auth.domain.Device.dtos.CreateDeviceDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.digest.DigestUtils;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DeviceUtil {

    private final Parser parser;

    public DeviceUtil() throws IOException {
        this.parser = new Parser();
    }

    public CreateDeviceDto extractDevice(HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");

        Client client = parser.parse(userAgent);

        String browser =
                client.userAgent != null
                        ? client.userAgent.family
                        : "Unknown";

        String browserVersion =
                client.userAgent != null
                        ? buildVersion(
                        client.userAgent.major,
                        client.userAgent.minor,
                        client.userAgent.patch)
                        : null;

        String os =
                client.os != null
                        ? client.os.family
                        : "Unknown";

        String osVersion =
                client.os != null
                        ? buildVersion(
                        client.os.major,
                        client.os.minor,
                        client.os.patch)
                        : null;

        String deviceName =
                client.device != null
                        ? client.device.family
                        : "Unknown";

        return CreateDeviceDto.builder()
                .deviceName(deviceName)
                .deviceType(detectDeviceType(userAgent))
                .browser(browser)
                .browserVersion(browserVersion)
                .os(os)
                .osVersion(osVersion)
                .ipAddress(getClientIp(request))
                .userAgent(userAgent)
                .language(request.getHeader("Accept-Language"))
                .platform(request.getHeader("Sec-CH-UA-Platform"))
                .deviceFingerprint(
                        generateFingerprint(
                                request,
                                userAgent))
                .refreshToken("")
                .build();
    }

    private String buildVersion(
            String major,
            String minor,
            String patch) {

        return Stream.of(major, minor, patch)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("."));
    }

    public String getClientIp(
            HttpServletRequest request) {

        String forwarded =
                request.getHeader("X-Forwarded-For");

        if (forwarded != null &&
                !forwarded.isBlank()) {

            return forwarded.split(",")[0];
        }

        return request.getRemoteAddr();
    }

    private String detectDeviceType(
            String userAgent) {

        if (userAgent == null) {
            return "UNKNOWN";
        }

        String ua = userAgent.toLowerCase();

        if (ua.contains("mobile")) {
            return "MOBILE";
        }

        if (ua.contains("tablet")
                || ua.contains("ipad")) {
            return "TABLET";
        }

        return "DESKTOP";
    }

    private String generateFingerprint(
            HttpServletRequest request,
            String userAgent) {

        String raw =
                getClientIp(request)
                        + "|"
                        + userAgent
                        + "|"
                        + request.getHeader("Accept-Language");

        return DigestUtils.sha256Hex(raw);
    }
}