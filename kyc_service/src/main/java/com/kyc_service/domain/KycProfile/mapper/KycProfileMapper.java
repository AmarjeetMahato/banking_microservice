package com.kyc_service.domain.KycProfile.mapper;

import com.kyc_service.domain.KycProfile.dtos.CreateKycDetailsRequestDto;
import com.kyc_service.domain.KycProfile.dtos.KycDetailsResponseDto;
import com.kyc_service.domain.KycProfile.dtos.UpdateKycDetailsRequestDto;
import com.kyc_service.domain.KycProfile.entity.KycProfile;
import com.kyc_service.domain.KycProfile.enums.KycLevel;
import com.kyc_service.domain.KycProfile.enums.KycStatus;
import com.kyc_service.globalExceptions.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class KycProfileMapper {

    /**
     * Create DTO -> Entity
     */
    public KycProfile toEntity(CreateKycDetailsRequestDto dto) {

        if (dto == null) {
            throw new BadRequestException("The KYC details request cannot be null. Please provide valid registration data.");
        }

        return KycProfile.builder()
                .userId(dto.getUserId())
                .accountId(dto.getAccountId())
                .kycReferenceNumber(dto.getKycReferenceNumber())
                .kycLevel(
                        dto.getKycLevel() != null
                                ? dto.getKycLevel()
                                : KycLevel.MINIMUM
                )
                .status(KycStatus.PENDING)
                .rejectionReason(dto.getRejectionReason())
                .manualReviewRequired(
                        dto.getManualReviewRequired() != null
                                ? dto.getManualReviewRequired()
                                : false
                )
                .verifiedBy(dto.getVerifiedBy())
                .riskScore(dto.getRiskScore())
                .active(true)
                .submittedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Update DTO -> Existing Entity
     */
    public void updateEntity(
            KycProfile entity,
            UpdateKycDetailsRequestDto dto
    ) {

        if (entity == null || dto == null) {
            return;
        }

        if (dto.getUserId() != null) {
            entity.setUserId(dto.getUserId());
        }

        if (dto.getAccountId() != null) {
            entity.setAccountId(dto.getAccountId());
        }

        if (dto.getKycReferenceNumber() != null) {
            entity.setKycReferenceNumber(dto.getKycReferenceNumber());
        }

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());

            switch (dto.getStatus()) {
                case APPROVED -> {
                    entity.setApprovedAt(LocalDateTime.now());
                    entity.setRejectedAt(null);
                }

                case REJECTED -> {
                    entity.setRejectedAt(LocalDateTime.now());
                    entity.setApprovedAt(null);
                }
            }
        }

        if (dto.getKycLevel() != null) {
            entity.setKycLevel(dto.getKycLevel());
        }

        if (dto.getRejectionReason() != null) {
            entity.setRejectionReason(dto.getRejectionReason());
        }

        if (dto.getManualReviewRequired() != null) {
            entity.setManualReviewRequired(
                    dto.getManualReviewRequired()
            );
        }

        if (dto.getVerifiedBy() != null) {
            entity.setVerifiedBy(dto.getVerifiedBy());
        }

        if (dto.getRiskScore() != null) {
            entity.setRiskScore(dto.getRiskScore());
        }

        if (dto.getActive() != null) {
            entity.setActive(dto.getActive());
        }

        entity.setLastReviewDate(LocalDateTime.now());
    }

    /**
     * Entity -> Response DTO
     */
    public KycDetailsResponseDto toResponseDto(
            KycProfile entity
    ) {

        if (entity == null) {
            return null;
        }

        return KycDetailsResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .accountId(entity.getAccountId())
                .kycReferenceNumber(entity.getKycReferenceNumber())
                .status(entity.getStatus())
                .kycLevel(entity.getKycLevel())
                .rejectionReason(entity.getRejectionReason())
                .manualReviewRequired(entity.getManualReviewRequired())
                .verifiedBy(entity.getVerifiedBy())
                .riskScore(entity.getRiskScore())
                .active(entity.getActive())
                .submittedAt(entity.getSubmittedAt())
                .approvedAt(entity.getApprovedAt())
                .rejectedAt(entity.getRejectedAt())
                .expiresAt(entity.getExpiresAt())
                .lastReviewDate(entity.getLastReviewDate())
                .nextReviewDate(entity.getNextReviewDate())
                .build();
    }

    /**
     * Entity List -> Response DTO List
     */
    public List<KycDetailsResponseDto> toResponseDtoList(
            List<KycProfile> entities
    ) {

        return entities.stream()
                .map(this::toResponseDto)
                .toList();
    }

}
