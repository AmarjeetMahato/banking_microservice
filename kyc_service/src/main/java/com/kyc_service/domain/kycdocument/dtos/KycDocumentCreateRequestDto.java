package com.kyc_service.domain.kycdocument.dtos;


import com.kyc_service.domain.kycdocument.enums.KycDocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycDocumentCreateRequestDto {


    @NotNull(message = "KYC Profile ID is required")
    private String kycProfileId;

    @NotNull(message = "Document type is required")
    private KycDocumentType documentType;

    @NotBlank(message = "Document number is required")
    private String documentNumber;

    @NotBlank(message = "Front image URL is required")
    private String frontImageUrl;

    private String backImageUrl;

    private String selfieImageUrl;

    private String issuingAuthority;

    private LocalDate issuedDate;

    private LocalDate expiryDate;
}

