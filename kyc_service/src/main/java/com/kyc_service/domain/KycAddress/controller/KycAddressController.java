package com.kyc_service.domain.KycAddress.controller;

import com.kyc_service.domain.KycAddress.dtos.KycAddressCreateRequestDto;
import com.kyc_service.domain.KycAddress.dtos.KycAddressResponseDto;
import com.kyc_service.domain.KycAddress.dtos.KycAddressUpdateRequestDto;
import com.kyc_service.domain.KycAddress.services.KycAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // =========================
    // UPDATE ADDRESS
    // =========================
    @PatchMapping("/{addressId}/update")
    public ResponseEntity<KycAddressResponseDto> updateKycAddress(
            @Valid @RequestBody KycAddressUpdateRequestDto dto,
            @PathVariable String addressId
            ) {

        KycAddressResponseDto result = kycAddressService.updateAddress(dto,addressId);
        return ResponseEntity.ok(result);
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<KycAddressResponseDto> getAddressById(
            @PathVariable String id) {

        KycAddressResponseDto result = kycAddressService.getAddressById(id);
        return ResponseEntity.ok(result);
    }

    // =========================
    // GET BY KYC PROFILE ID
    // =========================
    @GetMapping("/profile/{kycProfileId}")
    public ResponseEntity<List<KycAddressResponseDto>> getAddressesByKycProfileId(
            @PathVariable String kycProfileId) {

        List<KycAddressResponseDto> result =
                kycAddressService.getAddressesByKycProfileId(kycProfileId);

        return ResponseEntity.ok(result);
    }

    // =========================
    // DELETE ADDRESS
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String id) {

        kycAddressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // VERIFY ADDRESS
    // =========================
    @PutMapping("/{id}/verify")
    public ResponseEntity<KycAddressResponseDto> verifyAddress(
            @PathVariable String id,
            @RequestParam String verifiedBy) {

        KycAddressResponseDto result =
                kycAddressService.verifyAddress(id, verifiedBy);

        return ResponseEntity.ok(result);
    }

    // =========================
    // ACTIVATE / DEACTIVATE
    // =========================
    @PatchMapping("/{id}/active")
    public ResponseEntity<KycAddressResponseDto> toggleActive(
            @PathVariable String id,
            @RequestParam Boolean active) {

        KycAddressResponseDto result =
                kycAddressService.toggleActive(id, active);

        return ResponseEntity.ok(result);
    }




}
