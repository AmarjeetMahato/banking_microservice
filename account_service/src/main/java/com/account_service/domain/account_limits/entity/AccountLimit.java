package com.account_service.domain.account_limits.entity;

import com.account_service.base.BaseEntity;
import com.account_service.domain.accounts.entity.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "account_limits")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountLimit extends BaseEntity {
    @NotNull(message = "Associated account is required")
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @NotNull(message = "Daily transaction limit cannot be null")
    @PositiveOrZero(message = "Daily limit must be zero or a positive amount")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal dailyLimit = new BigDecimal("50000");

    @NotNull(message = "Monthly transaction limit cannot be null")
    @PositiveOrZero(message = "Monthly limit must be zero or a positive amount")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyLimit = new BigDecimal("500000");

    @NotNull(message = "Per-transaction limit cannot be null")
    @PositiveOrZero(message = "Per-transaction limit must be zero or a positive amount")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal perTransactionLimit = new BigDecimal("20000");
}
