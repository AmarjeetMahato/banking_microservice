package com.kyc_service.domain.KycAddress.entity;

import com.kyc_service.domain.KycAddress.enums.AddressType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "kyc_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class KycAddress {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kyc_profile_id", nullable = false)
    private KycProfile kycProfile;

    @NotNull(message = "Address type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType addressType;

    @NotBlank(message = "Address line 1 is required")
    @Column(nullable = false)
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "District is required")
    @Column(nullable = false)
    private String district;

    @NotBlank(message = "State is required")
    @Column(nullable = false)
    private String state;

    @NotBlank(message = "Country is required")
    @Column(nullable = false)
    private String country;

    @NotBlank(message = "Postal code is required")
    @Column(nullable = false)
    private String postalCode;

    private String landmark;

    @Builder.Default
    @Column(nullable = false)
    private Boolean verified = false;

    private String addressProofDocument;

    private String verifiedBy;

    private LocalDateTime verifiedAt;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}
