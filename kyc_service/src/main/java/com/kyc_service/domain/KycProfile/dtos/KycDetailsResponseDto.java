package com.kyc_service.domain.KycProfile.dtos;

import com.kyc_service.domain.KycProfile.enums.KycLevel;
import com.kyc_service.domain.KycProfile.enums.KycStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KycDetailsResponseDto {
    private String id;

    private String userId;

    private String accountId;

    private String kycReferenceNumber;

    private KycStatus status;

    private KycLevel kycLevel;

    private String rejectionReason;

    private Boolean manualReviewRequired;

    private String verifiedBy;

    private Integer riskScore;

    private Boolean active;

    private LocalDateTime submittedAt;

    private LocalDateTime approvedAt;

    private LocalDateTime rejectedAt;

    private LocalDateTime expiresAt;

    private LocalDateTime lastReviewDate;

    private LocalDateTime nextReviewDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
