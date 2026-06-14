package com.kyc_service.domain.kycdocument.dtos;

import com.kyc_service.domain.kycdocument.enums.KycDocumentType;
import com.kyc_service.domain.kycdocument.enums.KycVerificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KycDocumentUpdateRequestDto {

    private KycDocumentType documentType;

    private String documentNumber;

    private String frontImageUrl;

    private String backImageUrl;

    private String selfieImageUrl;

    private KycVerificationStatus status;

    private String remarks;

    private Boolean active;

    private String issuingAuthority;

    private LocalDate issuedDate;

    private LocalDate expiryDate;

    private Double confidenceScore;

    private Boolean ocrMatched;
}
