package com.kyc_service.domain.kycdocument.service;

import com.kyc_service.domain.kycdocument.dtos.KycDocumentCreateRequestDto;
import com.kyc_service.domain.kycdocument.dtos.KycDocumentResponseDto;
import com.kyc_service.domain.kycdocument.dtos.KycDocumentUpdateRequestDto;
import com.kyc_service.domain.kycdocument.enums.KycDocumentType;
import com.kyc_service.domain.kycdocument.enums.KycVerificationStatus;

import java.util.List;

public interface KycDocumentService {

    /**
     * Upload / Create new KYC document
     */
    KycDocumentResponseDto createDocument(KycDocumentCreateRequestDto dto);

    /**
     * Update existing document (PATCH style)
     */
    KycDocumentResponseDto updateDocument(KycDocumentUpdateRequestDto dto,String docId);

    /**
     * Get document by ID
     */
    KycDocumentResponseDto getDocumentById(String id);

    /**
     * Get all documents for a KYC profile
     */
    List<KycDocumentResponseDto> getDocumentsByKycProfileId(String kycProfileId);

    /**
     * Get documents by type (AADHAR, PAN, etc.)
     */
    List<KycDocumentResponseDto> getDocumentsByType(String kycProfileId, KycDocumentType type);

    /**
     * Verify document manually (admin/KYC officer)
     */
    KycDocumentResponseDto verifyDocument(String id, String verifiedBy, String referenceId);

    /**
     * Reject document with remarks
     */
    KycDocumentResponseDto rejectDocument(String id, String verifiedBy, String remarks);

    /**
     * Update document status (PENDING / VERIFIED / REJECTED)
     */
    KycDocumentResponseDto updateStatus(String id, KycVerificationStatus status, String updatedBy);

    /**
     * Activate / Deactivate document
     */
    KycDocumentResponseDto toggleActive(String id, Boolean active);

    /**
     * Delete document (soft delete recommended in real systems)
     */
    void deleteDocument(String id);

}
