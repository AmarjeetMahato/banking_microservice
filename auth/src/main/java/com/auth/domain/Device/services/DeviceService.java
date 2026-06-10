package com.auth.domain.Device.services;

import com.auth.domain.Device.dtos.CreateDeviceDto;
import com.auth.domain.Device.dtos.ResponseDeviceDto;
import com.auth.domain.Device.entity.Device;
import com.auth.domain.Users.entity.User;

public interface DeviceService {


    Device createDevice(CreateDeviceDto dto, User user, String refreshToken);

    ResponseDeviceDto getByDeviceId(String deviceId);
}
