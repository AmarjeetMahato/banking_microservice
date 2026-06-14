package com.kyc_service.domain.KycAddress.mapper;

import com.kyc_service.domain.KycAddress.dtos.KycAddressCreateRequestDto;
import com.kyc_service.domain.KycAddress.dtos.KycAddressResponseDto;
import com.kyc_service.domain.KycAddress.dtos.KycAddressUpdateRequestDto;
import com.kyc_service.domain.KycAddress.entity.KycAddress;
import com.kyc_service.domain.KycProfile.entity.KycProfile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KycAddressMapper {

    /**
     * Convert Create DTO -> Entity
     */
    public KycAddress toEntity(KycAddressCreateRequestDto dto, KycProfile kycProfile) {
        if (dto == null) {
            return null;
        }

        return KycAddress.builder()
                .kycProfile(kycProfile)
                .addressType(dto.getAddressType())
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .city(dto.getCity())
                .district(dto.getDistrict())
                .state(dto.getState())
                .country(dto.getCountry())
                .postalCode(dto.getPostalCode())
                .landmark(dto.getLandmark())
                .verified(false)
                .active(true)
                .build();
    }

    /**
     * Convert Update DTO -> existing Entity (PATCH style update)
     * Only updates non-null fields
     */
    public void updateEntity(KycAddressUpdateRequestDto dto, KycAddress entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getAddressType() != null) {
            entity.setAddressType(dto.getAddressType());
        }

        if (dto.getAddressLine1() != null) {
            entity.setAddressLine1(dto.getAddressLine1());
        }

        if (dto.getAddressLine2() != null) {
            entity.setAddressLine2(dto.getAddressLine2());
        }

        if (dto.getCity() != null) {
            entity.setCity(dto.getCity());
        }

        if (dto.getDistrict() != null) {
            entity.setDistrict(dto.getDistrict());
        }

        if (dto.getState() != null) {
            entity.setState(dto.getState());
        }

        if (dto.getCountry() != null) {
            entity.setCountry(dto.getCountry());
        }

        if (dto.getPostalCode() != null) {
            entity.setPostalCode(dto.getPostalCode());
        }

        if (dto.getLandmark() != null) {
            entity.setLandmark(dto.getLandmark());
        }

        if (dto.getActive() != null) {
            entity.setActive(dto.getActive());
        }

        // Verification logic (important business rule)
        if (dto.getVerified() != null) {
            entity.setVerified(dto.getVerified());

            if (Boolean.TRUE.equals(dto.getVerified())) {
                entity.setVerifiedAt(LocalDateTime.now());
                entity.setVerifiedBy("SYSTEM"); // replace with logged-in user
            } else {
                entity.setVerifiedAt(null);
                entity.setVerifiedBy(null);
            }
        }
    }

    /**
     * Convert Entity -> Response DTO
     */
    public KycAddressResponseDto toResponseDto(KycAddress entity) {
        if (entity == null) {
            return null;
        }

        return KycAddressResponseDto.builder()
                .id(entity.getId())
                .kycProfileId(
                        entity.getKycProfile() != null
                                ? entity.getKycProfile().getId()
                                : null
                )
                .addressType(entity.getAddressType())
                .addressLine1(entity.getAddressLine1())
                .addressLine2(entity.getAddressLine2())
                .city(entity.getCity())
                .district(entity.getDistrict())
                .state(entity.getState())
                .country(entity.getCountry())
                .postalCode(entity.getPostalCode())
                .landmark(entity.getLandmark())
                .verified(entity.getVerified())
                .verifiedBy(entity.getVerifiedBy())
                .verifiedAt(entity.getVerifiedAt())
                .active(entity.getActive())
                .build();
    }

}
