package com.kyc_service.domain.KycRiskAssessment.entity;

import com.kyc_service.base.BaseEntity;
import com.kyc_service.domain.KycProfile.entity.KycProfile;
import com.kyc_service.domain.KycRiskAssessment.enums.RiskLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Entity
@Table(name = "kyc_risk_assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class KycRiskAssessment extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kyc_profile_id", nullable = false)
    private KycProfile kycProfile;

    @NotNull(message = "Risk level is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskLevel riskLevel;

    @NotNull(message = "Risk score is required")
    @Column(nullable = false)
    private Integer riskScore;

    @Builder.Default
    @Column(nullable = false)
    private Boolean politicallyExposedPerson = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean sanctionListMatch = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean adverseMediaFound = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean amlFlagged = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean fraudSuspected = false;

    @Column(length = 1000)
    private String riskReason;

    private String assessedBy;

    private LocalDateTime assessedAt;

    private LocalDateTime nextReviewDate;
}
