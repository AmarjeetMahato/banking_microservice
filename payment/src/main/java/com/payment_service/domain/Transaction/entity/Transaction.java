package com.payment_service.domain.Transaction.entity;

import com.payment_service.base.BaseEntity;
import com.payment_service.domain.Payment.entity.Payment;
import com.payment_service.domain.Transaction.enums.TransactionStatus;
import com.payment_service.domain.Transaction.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;


@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(name = "idx_transaction_reference", columnList = "transactionReference"),
                @Index(name = "idx_transaction_payment", columnList = "payment_id"),
                @Index(name = "idx_transaction_status", columnList = "status"),
                @Index(name = "idx_transaction_source", columnList = "sourceAccountId"),
                @Index(name = "idx_transaction_destination", columnList = "destinationAccountId")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Transaction extends BaseEntity {

    @NotBlank(message = "Transaction reference is required")
    @Size(max = 100, message = "Transaction reference cannot exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String transactionReference;

    /**
     * Many transactions belong to one payment
     */
    @NotNull(message = "Payment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    /**
     * Source Account (from Account Service - only ID stored)
     */
    @NotBlank(message = "Source account ID is required")
    @Size(max = 100, message = "Source account ID cannot exceed 100 characters")
    @Column(nullable = false)
    private String sourceAccountId;

    /**
     * Destination Account (from Account Service - only ID stored)
     */
    @NotBlank(message = "Destination account ID is required")
    @Size(max = 100, message = "Destination account ID cannot exceed 100 characters")
    @Column(nullable = false)
    private String destinationAccountId;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @NotNull(message = "Transaction status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 18, fraction = 2, message = "Invalid amount format")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    /**
     * Fee charged for this transaction (if any)
     */
    @DecimalMin(value = "0.00", message = "Fee cannot be negative")
    @Digits(integer = 18, fraction = 2, message = "Invalid fee format")
    @Column(precision = 18, scale = 2)
    private BigDecimal feeAmount = BigDecimal.ZERO;

    /**
     * Tax on transaction (GST, etc.)
     */
    @DecimalMin(value = "0.00", message = "Tax cannot be negative")
    @Digits(integer = 18, fraction = 2, message = "Invalid tax format")
    @Column(precision = 18, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    /**
     * Final amount after fee/tax
     */
    @DecimalMin(value = "0.00", message = "Net amount cannot be negative")
    @Digits(integer = 18, fraction = 2, message = "Invalid net amount format")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal netAmount;

    /**
     * External reference from gateway/bank
     */
    @Size(max = 120, message = "External reference cannot exceed 120 characters")
    @Column(length = 120)
    private String externalReference;

    /**
     * Failure reason if transaction fails
     */
    @Size(max = 500, message = "Failure reason cannot exceed 500 characters")
    @Column(length = 500)
    private String failureReason;

}
