package com.auth.domain.Device.services;

import com.auth.domain.Device.dtos.CreateDeviceDto;
import com.auth.domain.Device.dtos.DeviceContext;

public interface DeviceSecurityService {

    boolean isSuspiciousDevice(CreateDeviceDto dto, DeviceContext context, String userId);

}
