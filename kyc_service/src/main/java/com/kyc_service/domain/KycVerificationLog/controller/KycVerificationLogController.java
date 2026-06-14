package com.kyc_service.domain.KycVerificationLog.controller;

import com.kyc_service.domain.KycVerificationLog.dtos.CreateKycVerificationLogDto;
import com.kyc_service.domain.KycVerificationLog.dtos.KycVerificationLogResponseDto;
import com.kyc_service.domain.KycVerificationLog.dtos.UpdateKycVerificationLogDto;
import com.kyc_service.domain.KycVerificationLog.services.KycVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kyc_verification")
@RequiredArgsConstructor
public class KycVerificationLogController {

    private  final KycVerificationService kycVerificationService;

    /**
     * Create Verification Log
     */
    @PostMapping("/create")
    public ResponseEntity<KycVerificationLogResponseDto> createVerificationLog(
            @Valid @RequestBody CreateKycVerificationLogDto request
    ) {
        KycVerificationLogResponseDto result = kycVerificationService.createVerificationLog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Update Verification Log
     */
    @PatchMapping("/{id}/update")
    public ResponseEntity<KycVerificationLogResponseDto> updateVerificationLog(
            @PathVariable String id,
            @Valid @RequestBody UpdateKycVerificationLogDto request
    ) {

        return ResponseEntity.ok(
                kycVerificationService.updateVerificationLog(id, request)
        );
    }

    /**
     * Get Verification Log By Id
     */
    @GetMapping("/{id}")
    public ResponseEntity<KycVerificationLogResponseDto> getVerificationLogById(@PathVariable String id) {

        return ResponseEntity.ok(kycVerificationService.getVerificationLogById(id));
    }

    /**
     * Get All Verification Logs
     */
    @GetMapping
    public ResponseEntity<Page<KycVerificationLogResponseDto>> getAllVerificationLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                kycVerificationService.getAllVerificationLogs(page, size)
        );
    }

    /**
     * Get Logs By KYC Profile
     */
    @GetMapping("/profile/{kycProfileId}")
    public ResponseEntity<Page<KycVerificationLogResponseDto>> getLogsByKycProfile(
            @PathVariable String kycProfileId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                kycVerificationService.getVerificationLogsByKycProfile(
                        kycProfileId,
                        page,
                        size
                )
        );
    }

    /**
     * Get Logs By Action
     */
    @GetMapping("/action/{action}")
    public ResponseEntity<Page<KycVerificationLogResponseDto>> getLogsByAction(
            @PathVariable String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                kycVerificationService.getVerificationLogsByAction(
                        action,
                        page,
                        size
                )
        );
    }

    /**
     * Get Logs By Status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<KycVerificationLogResponseDto>> getLogsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                kycVerificationService.getVerificationLogsByStatus(
                        status,
                        page,
                        size
                )
        );
    }

    /**
     * Activate Verification Log
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<String> activateVerificationLog(
            @PathVariable String id
    ) {

        kycVerificationService.activateVerificationLog(id);

        return ResponseEntity.ok(
                "Verification log activated successfully"
        );
    }

    /**
     * Deactivate Verification Log
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateVerificationLog(
            @PathVariable String id
    ) {

        kycVerificationService.deactivateVerificationLog(id);

        return ResponseEntity.ok(
                "Verification log deactivated successfully"
        );
    }

    /**
     * Soft Delete Verification Log
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVerificationLog(
            @PathVariable String id
    ) {

        kycVerificationService.deleteVerificationLog(id);

        return ResponseEntity.ok(
                "Verification log deleted successfully"
        );
    }
}
