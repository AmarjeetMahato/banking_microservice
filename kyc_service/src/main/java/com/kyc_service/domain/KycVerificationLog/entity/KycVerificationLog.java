package com.kyc_service.domain.KycVerificationLog.entity;

import com.kyc_service.base.BaseEntity;
import com.kyc_service.domain.KycProfile.entity.KycProfile;
import com.kyc_service.domain.KycProfile.enums.KycStatus;
import com.kyc_service.domain.KycVerificationLog.enums.VerificationAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "kyc_verification_logs", indexes = {
        @Index(name = "idx_kyc_log_profile", columnList = "kycProfileId"),
        @Index(name = "idx_kyc_log_action", columnList = "action")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class KycVerificationLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kyc_profile_id", nullable = false)
    private KycProfile kycProfile;

    @NotNull(message = "Action is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationAction action;

    @Enumerated(EnumType.STRING)
    private KycStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private KycStatus newStatus;

    @NotBlank(message = "Performer name is required")
    @Column(nullable = false)
    private String performedBy;

    private String performedRole;

    @NotNull(message = "Action time is required")
    @Column(nullable = false)
    private LocalDateTime actionTime;

    @Column(length = 1000)
    private String remarks;

    private String ipAddress;

    private String deviceInfo;

    private String referenceId;

    @Builder.Default
    @Column(nullable = false)
    private Boolean systemGenerated = false;
}
