package com.kyc_service.domain.kycdocument.entity;


import com.kyc_service.domain.kycdocument.enums.KycVerificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "kyc_documents", indexes = {
        // FIXED: Changed 'kycProfileId' to 'kyc_profile_id' to match @JoinColumn
        @Index(name = "idx_kyc_doc_profile", columnList = "kyc_profile_id"),
        @Index(name = "idx_kyc_doc_type", columnList = "documentType"),
        @Index(name = "idx_kyc_doc_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class KycDocument {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kyc_profile_id", nullable = false)
    private KycProfile kycProfile;

    @NotNull(message = "Document type is required")
    @Enumerated(EnumType.STRING)
    private KycDocumentType documentType;

    @NotBlank(message = "Document number cannot be empty")
    @Column(nullable = false)
    private String documentNumber;

    @NotBlank(message = "Front image URL is required")
    @Column(nullable = false)
    private String frontImageUrl;

    private String backImageUrl;

    private String selfieImageUrl;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KycVerificationStatus status;

    private String verificationReferenceId;

    private String verifiedBy;

    private LocalDateTime verifiedAt;

    @Column(length = 1000)
    private String remarks;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ocrMatched = false;

    private Double confidenceScore;

    private String issuingAuthority;

    private LocalDate issuedDate;

    private LocalDate expiryDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
