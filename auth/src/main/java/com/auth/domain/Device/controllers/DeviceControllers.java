package com.auth.domain.Device.controllers;


import com.auth.domain.Device.dtos.CreateDeviceDto;
import com.auth.domain.Device.dtos.ResponseDeviceDto;
import com.auth.domain.Device.services.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/device")
@RequiredArgsConstructor
public class DeviceControllers {

    private final DeviceService deviceService;


    @GetMapping("/{deviceId}/get_by_Id")
    public  ResponseEntity<ResponseDeviceDto>GetDeviceById(@PathVariable String deviceId){
        ResponseDeviceDto deviceDto = deviceService.getByDeviceId(deviceId);
        return ResponseEntity.status(HttpStatus.OK).body(deviceDto);
    }

    @GetMapping("/findTrustedDevices")
    public  ResponseEntity<List<ResponseDeviceDto>> findTrustedDevices(){

        List<ResponseDeviceDto> getAllDevices = deviceService.findTrustedDevices("");
        return  ResponseEntity.status(HttpStatus.OK).body(getAllDevices);
    }




}
