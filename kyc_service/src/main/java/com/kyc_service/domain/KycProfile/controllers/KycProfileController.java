package com.kyc_service.domain.KycProfile.controllers;

import com.kyc_service.domain.KycProfile.dtos.CreateKycDetailsRequestDto;
import com.kyc_service.domain.KycProfile.dtos.KycDetailsResponseDto;
import com.kyc_service.domain.KycProfile.dtos.UpdateKycDetailsRequestDto;
import com.kyc_service.domain.KycProfile.services.KycProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kyc_profile")
@RequiredArgsConstructor
public class KycProfileController {

    private  final KycProfileService kycProfileService;


    @PostMapping("/create")
    public ResponseEntity<KycDetailsResponseDto> createKycUserProfile(
            @Valid @RequestBody CreateKycDetailsRequestDto dto){
        KycDetailsResponseDto responseDto = kycProfileService.createKycProfile(dto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/{kycId}/update")
    public  ResponseEntity<KycDetailsResponseDto> updateKycUserProfileUpdate(
            @Valid @RequestBody UpdateKycDetailsRequestDto dto,
            @PathVariable String kycId
            ){
        KycDetailsResponseDto responseDto = kycProfileService.updateKycProfile(kycId,dto);
        return  ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{kycId}/get_by_KycId")
    public  ResponseEntity<KycDetailsResponseDto> getByKycId(@PathVariable String kycId){
        KycDetailsResponseDto responseDto = kycProfileService.getKycProfileById(kycId);
        return  ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<KycDetailsResponseDto> getByUserId(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                kycProfileService.getKycProfileByUserId(userId)
        );
    }

    // ---------------- GET BY ACCOUNT ID ----------------

    @GetMapping("/account/{accountId}")
    public ResponseEntity<KycDetailsResponseDto> getByAccountId(
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                kycProfileService.getKycProfileByAccountId(accountId)
        );
    }

    // ---------------- GET ALL (with optional filters) ----------------

    @GetMapping
    public ResponseEntity<List<KycDetailsResponseDto>> getAllKycProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                kycProfileService.getAllKycProfiles( page, size)
        );
    }

    // ---------------- APPROVE KYC ----------------

    @PostMapping("/{kycId}/approve")
    public ResponseEntity<KycDetailsResponseDto> approveKyc(
            @PathVariable String kycId,
            @RequestParam String verifiedBy) {

        return ResponseEntity.ok(
                kycProfileService.approveKyc(kycId, verifiedBy)
        );
    }

    // ---------------- REJECT KYC ----------------

    @PostMapping("/{kycId}/reject")
    public ResponseEntity<KycDetailsResponseDto> rejectKyc(
            @PathVariable String kycId,
            @RequestParam String verifiedBy,
            @RequestParam String rejectionReason) {

        return ResponseEntity.ok(
                kycProfileService.rejectKyc(kycId, rejectionReason, verifiedBy)
        );
    }

    // ---------------- MANUAL REVIEW ----------------

    @PostMapping("/{kycId}/manual-review")
    public ResponseEntity<KycDetailsResponseDto> markForManualReview(
            @PathVariable String kycId) {

        return ResponseEntity.ok(
                kycProfileService.markForManualReview(kycId)
        );
    }

    // ---------------- ACTIVATE ----------------

    @PatchMapping("/{kycId}/activate")
    public ResponseEntity<KycDetailsResponseDto> activate(
            @PathVariable String kycId) {

        return ResponseEntity.ok(
                kycProfileService.activateKycProfile(kycId)
        );
    }

    // ---------------- DEACTIVATE ----------------

    @PatchMapping("/{kycId}/deactivate")
    public ResponseEntity<KycDetailsResponseDto> deactivate(
            @PathVariable String kycId) {

        return ResponseEntity.ok(
                kycProfileService.deactivateKycProfile(kycId)
        );
    }

    // ---------------- DELETE (SOFT DELETE) ----------------

    @DeleteMapping("/{kycId}")
    public ResponseEntity<Void> deleteKyc(
            @PathVariable String kycId) {

        kycProfileService.deleteKycProfile(kycId);
        return ResponseEntity.noContent().build();
    }



}
