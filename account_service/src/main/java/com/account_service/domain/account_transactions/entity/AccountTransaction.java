package com.account_service.domain.account_transactions.entity;


import com.account_service.base.BaseEntity;
import com.account_service.domain.account_transactions.enums.TransactionStatus;
import com.account_service.domain.account_transactions.enums.TransactionType;
import com.account_service.domain.accounts.entity.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "account_transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountTransaction extends BaseEntity {
    @NotNull(message = "Associated account is required")
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull(message = "Transaction type is required (CREDIT/DEBIT)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionType transactionType;

    @NotNull(message = "Transaction amount cannot be null")
    @Positive(message = "Transaction amount must be strictly greater than zero")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Reference ID is required")
    @Column(nullable = false)
    private UUID referenceId;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;

    @NotNull(message = "Transaction status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;
}
