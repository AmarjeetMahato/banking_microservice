package com.kyc_service.domain.KycAddress.controller;

import com.kyc_service.domain.KycAddress.dtos.KycAddressCreateRequestDto;
import com.kyc_service.domain.KycAddress.dtos.KycAddressResponseDto;
import com.kyc_service.domain.KycAddress.services.KycAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kyc_address")
@RequiredArgsConstructor
public class KycAddressController {

    private  final KycAddressService kycAddressService;

    @PostMapping("/create")
    public ResponseEntity<KycAddressResponseDto> createKycAddress(@Valid
                    @RequestBody KycAddressCreateRequestDto kycAddressCreateRequestDto){

        KycAddressResponseDto result = kycAddressService.createAddress(kycAddressCreateRequestDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(result);
    }




}
