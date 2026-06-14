package com.kyc_service.domain.KycProfile.entity;


import com.kyc_service.base.BaseEntity;
import com.kyc_service.domain.KycAddress.entity.KycAddress;
import com.kyc_service.domain.KycProfile.enums.KycLevel;
import com.kyc_service.domain.KycProfile.enums.KycStatus;
import com.kyc_service.domain.KycReKycRequest.entity.KycReKycRequest;
import com.kyc_service.domain.KycRiskAssessment.entity.KycRiskAssessment;
import com.kyc_service.domain.KycVerificationLog.entity.KycVerificationLog;
import com.kyc_service.domain.kycdocument.entity.KycDocument;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "kyc_profiles",
        indexes = {
                @Index(name = "idx_kyc_profile_user_id", columnList = "userId"),
                @Index(name = "idx_kyc_profile_account_id", columnList = "accountId"),
                @Index(name = "idx_kyc_profile_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class KycProfile extends BaseEntity {
    @NotBlank(message = "User ID is required")
    @Size(max = 100, message = "User ID cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String userId;

    @NotBlank(message = "Account ID is required")
    @Size(max = 100, message = "Account ID cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String accountId;

    @NotBlank(message = "KYC reference number is required")
    @Size(max = 50, message = "KYC reference number cannot exceed 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String kycReferenceNumber;

    @NotNull(message = "KYC status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private KycStatus status = KycStatus.PENDING;

    @NotNull(message = "KYC level is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private KycLevel kycLevel = KycLevel.MINIMUM;

    @Size(max = 1000, message = "Rejection reason cannot exceed 1000 characters")
    @Column(length = 1000)
    private String rejectionReason;

    @Builder.Default
    @Column(nullable = false)
    private Boolean manualReviewRequired = false;

    @Size(max = 100, message = "Verified by cannot exceed 100 characters")
    @Column(length = 100)
    private String verifiedBy;

    @Min(value = 0, message = "Risk score cannot be negative")
    @Max(value = 100, message = "Risk score cannot exceed 100")
    @Column
    private Integer riskScore;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(updatable = false)
    private LocalDateTime submittedAt;

    private LocalDateTime approvedAt;

    private LocalDateTime rejectedAt;

    private LocalDateTime expiresAt;

    private LocalDateTime lastReviewDate;

    private LocalDateTime nextReviewDate;

    // ===============================
    // Relationships
    // ===============================

    @OneToMany(mappedBy = "kycProfile", fetch = FetchType.LAZY)
    private List<KycDocument> documents;

    @OneToMany(mappedBy = "kycProfile", fetch = FetchType.LAZY)
    private List<KycAddress> addresses;

    @OneToMany(mappedBy = "kycProfile", fetch = FetchType.LAZY)
    private List<KycVerificationLog> verificationLogs;

    @OneToOne(mappedBy = "kycProfile", fetch = FetchType.LAZY)
    private KycRiskAssessment riskAssessment;

    @OneToMany(mappedBy = "kycProfile", fetch = FetchType.LAZY)
    private List<KycReKycRequest> reKycRequests;

}
