package com.kyc_service.domain.KycVerificationLog.mapper;

import com.kyc_service.domain.KycProfile.entity.KycProfile;
import com.kyc_service.domain.KycVerificationLog.dtos.CreateKycVerificationLogDto;
import com.kyc_service.domain.KycVerificationLog.dtos.KycVerificationLogResponseDto;
import com.kyc_service.domain.KycVerificationLog.dtos.UpdateKycVerificationLogDto;
import com.kyc_service.domain.KycVerificationLog.entity.KycVerificationLog;
import org.springframework.stereotype.Component;

@Component
public class KycVerificationMapper {

    /**
     * Create DTO -> Entity
     */
    public KycVerificationLog toEntity(CreateKycVerificationLogDto dto, KycProfile kycProfile) {

        if (dto == null) {
            return null;
        }

        return KycVerificationLog.builder()
                .kycProfile(kycProfile)
                .action(dto.getAction())
                .oldStatus(dto.getOldStatus())
                .newStatus(dto.getNewStatus())
                .performedBy(dto.getPerformedBy())
                .performedRole(dto.getPerformedRole())
                .actionTime(dto.getActionTime())
                .remarks(dto.getRemarks())
                .ipAddress(dto.getIpAddress())
                .deviceInfo(dto.getDeviceInfo())
                .referenceId(dto.getReferenceId())
                .systemGenerated(
                        dto.getSystemGenerated() != null
                                ? dto.getSystemGenerated()
                                : Boolean.FALSE
                )
                .build();
    }

    /**
     * Update Existing Entity
     */
    public void updateEntity(KycVerificationLog entity, UpdateKycVerificationLogDto dto) {

        if (entity == null || dto == null) {
            return;
        }

        entity.setAction(dto.getAction());
        entity.setOldStatus(dto.getOldStatus());
        entity.setNewStatus(dto.getNewStatus());
        entity.setPerformedBy(dto.getPerformedBy());
        entity.setPerformedRole(dto.getPerformedRole());
        entity.setActionTime(dto.getActionTime());
        entity.setRemarks(dto.getRemarks());
        entity.setIpAddress(dto.getIpAddress());
        entity.setDeviceInfo(dto.getDeviceInfo());
        entity.setReferenceId(dto.getReferenceId());

        if (dto.getSystemGenerated() != null) {
            entity.setSystemGenerated(dto.getSystemGenerated());
        }
    }

    /**
     * Entity -> Response DTO
     */
    public KycVerificationLogResponseDto toResponseDto(KycVerificationLog entity) {

        if (entity == null) {
            return null;
        }

        return KycVerificationLogResponseDto.builder()
                .id(entity.getId())
                .kycProfileId(
                        entity.getKycProfile() != null
                                ? entity.getKycProfile().getId()
                                : null
                )
                .action(entity.getAction())
                .oldStatus(entity.getOldStatus())
                .newStatus(entity.getNewStatus())
                .performedBy(entity.getPerformedBy())
                .performedRole(entity.getPerformedRole())
                .actionTime(entity.getActionTime())
                .remarks(entity.getRemarks())
                .ipAddress(entity.getIpAddress())
                .deviceInfo(entity.getDeviceInfo())
                .referenceId(entity.getReferenceId())
                .systemGenerated(entity.getSystemGenerated())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .active(entity.getIsActive())
                .deleted(entity.getDeleted())
                .build();
    }
}
