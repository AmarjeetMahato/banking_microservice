package com.account_service.domain.beneficiaries.entity;


import com.account_service.domain.accounts.entity.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "beneficiaries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Beneficiary {

    @NotNull(message = "Associated account is required")
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotBlank(message = "Beneficiary name cannot be blank")
    @Size(min = 2, max = 100, message = "Beneficiary name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String beneficiaryName;

    @NotBlank(message = "Bank account number cannot be blank")
    @Size(min = 9, max = 30, message = "Bank account number must be between 9 and 30 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Bank account number must contain digits only")
    @Column(nullable = false, length = 30)
    private String bankAccountNumber;

    @NotBlank(message = "IFSC code cannot be blank")
    @Pattern(
            regexp = "^[A-Z]{4}0[A-Z0-9]{6}$",
            message = "Invalid IFSC code format. It should be 11 characters (e.g., SBIN0012345)"
    )
    @Column(nullable = false, length = 15)
    private String ifscCode;

    @Size(max = 50, message = "Nickname cannot exceed 50 characters")
    @Column(length = 50)
    private String nickname;
}
