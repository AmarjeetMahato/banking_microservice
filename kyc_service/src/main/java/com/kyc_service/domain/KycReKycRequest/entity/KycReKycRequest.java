package com.kyc_service.domain.KycReKycRequest.entity;

import com.kyc_service.domain.KycProfile.entity.KycProfile;
import com.kyc_service.domain.KycReKycRequest.enums.ReKycReason;
import com.kyc_service.domain.KycReKycRequest.enums.ReKycStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "kyc_rekyc_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class KycReKycRequest {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kyc_profile_id", nullable = false)
    private KycProfile kycProfile;

    @NotNull(message = "Re-KYC reason is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReKycReason reason;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReKycStatus status;

    @NotNull(message = "Request time is required")
    @Column(nullable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime dueDate;

    private LocalDateTime completedAt;

    @NotBlank(message = "Requested by is required")
    @Column(nullable = false)
    private String requestedBy;

    private String reviewedBy;

    @Column(length = 1000)
    private String remarks;

    @Builder.Default
    @Column(nullable = false)
    private Boolean mandatory = false;

    @Builder.Default
    @Column(nullable = false)
    private Integer attemptCount = 0;
}
