package com.account_service.domain.accounts.entity;


import com.account_service.base.BaseEntity;
import com.account_service.domain.account_balance.entity.AccountBalance;
import com.account_service.domain.account_limits.entity.AccountLimit;
import com.account_service.domain.accounts.enums.AccountStatus;
import com.account_service.domain.accounts.enums.AccountType;
import com.account_service.domain.beneficiaries.entity.Beneficiary;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Account extends BaseEntity {

    @NotNull(message = "User ID is required")
    @Column(nullable = false)
    private String userId;

    @NotBlank(message = "Account number cannot be blank")
    @Size(min = 10, max = 20, message = "Account number must be between 10 and 20 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Account number must contain digits only")
    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @NotNull(message = "Account type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType accountType;

    @NotNull(message = "Account status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;

    @NotBlank(message = "Currency code cannot be blank")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters (e.g., INR, USD)")
    @Column(nullable = false, length = 10)
    private String currency = "INR";

    @Valid // Cascades validation to the child entity if it's updated/created
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private AccountBalance balance;

    @Valid
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private AccountLimit accountLimit;


    @Valid
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Beneficiary> beneficiaries = new ArrayList<>();
}
