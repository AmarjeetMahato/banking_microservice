package com.account_service.domain.account_balance.entity;


import com.account_service.domain.accounts.entity.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "account_balance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBalance {
    @NotNull(message = "Associated account is required")
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @NotNull(message = "Available balance cannot be null")
    @PositiveOrZero(message = "Available balance must be zero or a positive amount")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @NotNull(message = "Hold balance cannot be null")
    @PositiveOrZero(message = "Hold balance must be zero or a positive amount")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal holdBalance = BigDecimal.ZERO;
}
