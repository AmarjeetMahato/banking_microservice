package com.kyc_service.domain.KycProfile.dtos;

import com.kyc_service.domain.KycProfile.enums.KycLevel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateKycDetailsRequestDto {
    @NotBlank(message = "User ID is required")
    @Size(max = 100, message = "User ID cannot exceed 100 characters")
    private String userId;

    @NotBlank(message = "Account ID is required")
    @Size(max = 100, message = "Account ID cannot exceed 100 characters")
    private String accountId;

    @NotBlank(message = "KYC reference number is required")
    @Size(max = 50, message = "KYC reference number cannot exceed 50 characters")
    private String kycReferenceNumber;

    @NotNull(message = "KYC level is required")
    private KycLevel kycLevel;

    @Size(max = 1000, message = "Rejection reason cannot exceed 1000 characters")
    private String rejectionReason;

    @Size(max = 100, message = "Verified by cannot exceed 100 characters")
    private String verifiedBy;

    @Min(value = 0, message = "Risk score cannot be less than 0")
    @Max(value = 100, message = "Risk score cannot exceed 100")
    private Integer riskScore;

    private Boolean manualReviewRequired;
}
