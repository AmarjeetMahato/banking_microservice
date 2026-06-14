package com.account_service.domain.kyc_details.mapper;

import com.account_service.domain.accounts.entity.Account;
import com.account_service.domain.kyc_details.dtos.KycDetailsDto;
import com.account_service.domain.kyc_details.dtos.ResponseKycDetails;
import com.account_service.domain.kyc_details.dtos.UpdateKycDetails;
import com.account_service.domain.kyc_details.entity.KycDetail;
import com.account_service.domain.kyc_details.enums.KycStatus;
import com.account_service.globalException.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KycDetailsMapper {

    public KycDetail toEntity(KycDetailsDto dto, Account account){

        if (dto == null) {
            throw new BadRequestException("Invalid KYC request payload");
        }

        if (account == null) {
            throw new BadRequestException("Associated account is required");
        }
        return KycDetail.builder()
                .account(account)
                .aadhaarNumber(dto.getAadhaarNumber())
                .panNumber(dto.getPanNumber())
                .kycStatus(KycStatus.PENDING)
                .build();
    }

    /**
     * Convert Entity -> Response DTO
     */
    public ResponseKycDetails toResponse(KycDetail kycDetail) {

        if (kycDetail == null) {
            throw new BadRequestException("KYC details not found");
        }

        return ResponseKycDetails.builder()
                .id(kycDetail.getId())
                .accountId(kycDetail.getAccount().getId())
                /*
                 * In production:
                 * return masked values instead
                 */
                .aadhaarNumber(kycDetail.getAadhaarNumber())
                .panNumber(kycDetail.getPanNumber())

                .kycStatus(kycDetail.getKycStatus())
                .verifiedAt(kycDetail.getVerifiedAt())

                .createdAt(kycDetail.getCreatedAt())
                .updatedAt(kycDetail.getUpdatedAt())
                .build();
    }

    /**
     * Update existing entity from Update DTO
     */
    public void updateEntity(KycDetail kycDetail, UpdateKycDetails dto) {

        if (dto == null) {
            throw new BadRequestException("Invalid update payload");
        }

        /*
         * Update Aadhaar
         */
        if (dto.getAadhaarNumber() != null
                && !dto.getAadhaarNumber().isBlank()) {

            kycDetail.setAadhaarNumber(dto.getAadhaarNumber());
        }

        /*
         * Update PAN
         */
        if (dto.getPanNumber() != null
                && !dto.getPanNumber().isBlank()) {

            kycDetail.setPanNumber(dto.getPanNumber());
        }

        /*
         * Update KYC Status
         */
        if (dto.getKycStatus() != null) {

            kycDetail.setKycStatus(dto.getKycStatus());

            /*
             * Auto-set verification timestamp
             */
            if (dto.getKycStatus() == KycStatus.VERIFIED) {

                kycDetail.setVerifiedAt(LocalDateTime.now());
            }
        }
    }
}
