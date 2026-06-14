package com.kyc_service.domain.kycdocument.dtos;

import com.kyc_service.domain.kycdocument.enums.KycDocumentType;
import com.kyc_service.domain.kycdocument.enums.KycVerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class KycDocumentResponseDto {

    private String id;

    private String kycProfileId;

    private KycDocumentType documentType;

    private String documentNumber;

    private String frontImageUrl;

    private String backImageUrl;

    private String selfieImageUrl;

    private KycVerificationStatus status;

    private String verificationReferenceId;

    private String verifiedBy;

    private LocalDateTime verifiedAt;

    private String remarks;

    private Boolean ocrMatched;

    private Double confidenceScore;

    private String issuingAuthority;

    private LocalDate issuedDate;

    private LocalDate expiryDate;

    private Boolean active;

}
