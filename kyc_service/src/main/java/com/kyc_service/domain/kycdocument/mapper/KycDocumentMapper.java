package com.kyc_service.domain.kycdocument.mapper;

import com.kyc_service.domain.KycProfile.entity.KycProfile;
import com.kyc_service.domain.kycdocument.dtos.KycDocumentCreateRequestDto;
import com.kyc_service.domain.kycdocument.dtos.KycDocumentResponseDto;
import com.kyc_service.domain.kycdocument.dtos.KycDocumentUpdateRequestDto;
import com.kyc_service.domain.kycdocument.entity.KycDocument;
import com.kyc_service.domain.kycdocument.enums.KycVerificationStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KycDocumentMapper {
    /**
     * CREATE DTO -> ENTITY
     */
    public KycDocument toEntity(KycDocumentCreateRequestDto dto, KycProfile kycProfile) {

        if (dto == null) {
            return null;
        }

        return KycDocument.builder()
                .kycProfile(kycProfile)
                .documentType(dto.getDocumentType())
                .documentNumber(dto.getDocumentNumber())
                .frontImageUrl(dto.getFrontImageUrl())
                .backImageUrl(dto.getBackImageUrl())
                .selfieImageUrl(dto.getSelfieImageUrl())
                .issuingAuthority(dto.getIssuingAuthority())
                .issuedDate(dto.getIssuedDate())
                .expiryDate(dto.getExpiryDate())
                .status(KycVerificationStatus.PENDING)
                .ocrMatched(false)
                .active(true)
                .build();
    }

    /**
     * UPDATE DTO -> ENTITY (PATCH style safe update)
     */
    public void updateEntity(KycDocumentUpdateRequestDto dto, KycDocument entity) {

        if (dto == null || entity == null) {
            return;
        }

        if (dto.getDocumentType() != null) {
            entity.setDocumentType(dto.getDocumentType());
        }

        if (dto.getDocumentNumber() != null) {
            entity.setDocumentNumber(dto.getDocumentNumber());
        }

        if (dto.getFrontImageUrl() != null) {
            entity.setFrontImageUrl(dto.getFrontImageUrl());
        }

        if (dto.getBackImageUrl() != null) {
            entity.setBackImageUrl(dto.getBackImageUrl());
        }

        if (dto.getSelfieImageUrl() != null) {
            entity.setSelfieImageUrl(dto.getSelfieImageUrl());
        }

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        if (dto.getRemarks() != null) {
            entity.setRemarks(dto.getRemarks());
        }

        if (dto.getIssuingAuthority() != null) {
            entity.setIssuingAuthority(dto.getIssuingAuthority());
        }

        if (dto.getIssuedDate() != null) {
            entity.setIssuedDate(dto.getIssuedDate());
        }

        if (dto.getExpiryDate() != null) {
            entity.setExpiryDate(dto.getExpiryDate());
        }

        if (dto.getConfidenceScore() != null) {
            entity.setConfidenceScore(dto.getConfidenceScore());
        }

        if (dto.getOcrMatched() != null) {
            entity.setOcrMatched(dto.getOcrMatched());
        }

        if (dto.getActive() != null) {
            entity.setActive(dto.getActive());
        }

        // BUSINESS LOGIC: verification handling
        if (dto.getStatus() != null &&
                dto.getStatus() == KycVerificationStatus.VERIFIED) {

            entity.setVerifiedAt(LocalDateTime.now());
            entity.setVerifiedBy("SYSTEM"); // replace with auth user
        }
    }

    /**
     * ENTITY -> RESPONSE DTO
     */
    public KycDocumentResponseDto toResponseDto(KycDocument entity) {

        if (entity == null) {
            return null;
        }

        return KycDocumentResponseDto.builder()
                .id(entity.getId())
                .kycProfileId(
                        entity.getKycProfile() != null
                                ? entity.getKycProfile().getId()
                                : null
                )
                .documentType(entity.getDocumentType())
                .documentNumber(entity.getDocumentNumber())
                .frontImageUrl(entity.getFrontImageUrl())
                .backImageUrl(entity.getBackImageUrl())
                .selfieImageUrl(entity.getSelfieImageUrl())
                .status(entity.getStatus())
                .verificationReferenceId(entity.getVerificationReferenceId())
                .verifiedBy(entity.getVerifiedBy())
                .verifiedAt(entity.getVerifiedAt())
                .remarks(entity.getRemarks())
                .ocrMatched(entity.getOcrMatched())
                .confidenceScore(entity.getConfidenceScore())
                .issuingAuthority(entity.getIssuingAuthority())
                .issuedDate(entity.getIssuedDate())
                .expiryDate(entity.getExpiryDate())
                .active(entity.getActive())
                .build();
    }
}
