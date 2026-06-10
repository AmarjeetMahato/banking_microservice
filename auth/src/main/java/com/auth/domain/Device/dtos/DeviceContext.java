package com.auth.domain.Device.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceContext {

    private CreateDeviceDto device;
    private String ipAddress;
    private String country;
    private String region;
    private String city;
    private String timezone;
}
