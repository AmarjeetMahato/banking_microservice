package com.kyc_service.domain.kycdocument.controller;

import com.kyc_service.domain.kycdocument.dtos.KycDocumentCreateRequestDto;
import com.kyc_service.domain.kycdocument.dtos.KycDocumentResponseDto;
import com.kyc_service.domain.kycdocument.dtos.KycDocumentUpdateRequestDto;
import com.kyc_service.domain.kycdocument.enums.KycDocumentType;
import com.kyc_service.domain.kycdocument.enums.KycVerificationStatus;
import com.kyc_service.domain.kycdocument.service.KycDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kyc_document")
@RequiredArgsConstructor
public class KycDocumentController {

    private  final KycDocumentService kycDocumentService;

    /**
     * Create Document
     */
    @PostMapping("/create")
    public ResponseEntity<KycDocumentResponseDto> createDocument(
            @Valid @RequestBody KycDocumentCreateRequestDto dto) {

        KycDocumentResponseDto result = kycDocumentService.createDocument(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    /**
     * Update Document
     */
    @PutMapping("/{id}/update")
    public ResponseEntity<KycDocumentResponseDto> updateDocument(
            @Valid @RequestBody KycDocumentUpdateRequestDto dto,
            @PathVariable String id) {

        KycDocumentResponseDto result = kycDocumentService.updateDocument(dto,id);
        return ResponseEntity.ok(result);
    }

    /**
     * Get Document By Id
     */
    @GetMapping("/{id}")
    public ResponseEntity<KycDocumentResponseDto> getDocumentById(@PathVariable String id) {

        KycDocumentResponseDto result = kycDocumentService.getDocumentById(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Get Documents By KYC Profile Id
     */
    @GetMapping("/profile/{kycProfileId}")
    public ResponseEntity<List<KycDocumentResponseDto>> getDocumentsByKycProfileId(
            @PathVariable String kycProfileId) {

        List<KycDocumentResponseDto> result = kycDocumentService.getDocumentsByKycProfileId(kycProfileId);
        return ResponseEntity.ok(result);
    }

    /**
     * Get Documents By Type
     */
    @GetMapping("/profile/{kycProfileId}/type/{documentType}")
    public ResponseEntity<List<KycDocumentResponseDto>> getDocumentsByType(
            @PathVariable String kycProfileId,
            @PathVariable KycDocumentType documentType) {

        List<KycDocumentResponseDto> result =
                kycDocumentService.getDocumentsByType(kycProfileId, documentType);
                return ResponseEntity.ok(result);
    }

    /**
     * Verify Document
     */
    @PatchMapping("/{id}/verify")
    public ResponseEntity<KycDocumentResponseDto> verifyDocument(
            @PathVariable String id,
            @RequestParam String verifiedBy,
            @RequestParam(required = false) String referenceId) {

        KycDocumentResponseDto result =
                kycDocumentService.verifyDocument(id, verifiedBy, referenceId);
                return ResponseEntity.ok(result);
    }

    /**
     * Reject Document
     */
    @PatchMapping("/{id}/reject")
    public ResponseEntity<KycDocumentResponseDto> rejectDocument(
            @PathVariable String id,
            @RequestParam String verifiedBy,
            @RequestParam String remarks) {

        KycDocumentResponseDto result =
                kycDocumentService.rejectDocument(id, verifiedBy, remarks);
                return ResponseEntity.ok(result);
    }

    /**
     * Update Status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<KycDocumentResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam KycVerificationStatus status,
            @RequestParam String updatedBy) {

        KycDocumentResponseDto result = kycDocumentService.updateStatus(id, status, updatedBy);
        return ResponseEntity.ok(result);
    }

    /**
     * Activate / Deactivate
     */
    @PatchMapping("/{id}/active")
    public ResponseEntity<KycDocumentResponseDto> toggleActive(
            @PathVariable String id,
            @RequestParam Boolean active) {

        KycDocumentResponseDto result = kycDocumentService.toggleActive(id, active);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete Document
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {

        kycDocumentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
