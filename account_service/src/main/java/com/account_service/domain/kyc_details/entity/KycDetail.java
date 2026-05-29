package com.account_service.domain.kyc_details.entity;


import com.account_service.base.BaseEntity;
import com.account_service.domain.accounts.entity.Account;
import com.account_service.domain.kyc_details.enums.KycStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_balance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KycDetail extends BaseEntity {
    @NotNull(message = "Associated account is required")
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @NotBlank(message = "Aadhaar number cannot be blank")
    @Pattern(
            regexp = "^[0-9]{12}$",
            message = "Aadhaar number must be exactly 12 digits"
    )
    @Column(nullable = false, length = 12)
    private String aadhaarNumber;

    @NotBlank(message = "PAN number cannot be blank")
    @Pattern(
            regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$",
            message = "Invalid PAN format. It must follow the standard structure (e.g., ABCDE1234F)"
    )
    @Column(nullable = false, length = 10)
    private String panNumber;

    @NotNull(message = "KYC status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private KycStatus kycStatus = KycStatus.PENDING;

    @PastOrPresent(message = "Verification timestamp must be in the past or present")
    private LocalDateTime verifiedAt;
}
