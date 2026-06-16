package com.payment_service.domain.Payment.entity;


import com.payment_service.base.BaseEntity;
import com.payment_service.domain.GatewayTransaction.entity.GatewayTransaction;
import com.payment_service.domain.IdempotencyKey.entity.IdempotencyKey;
import com.payment_service.domain.Payment.enums.CurrencyCode;
import com.payment_service.domain.Payment.enums.PaymentMethod;
import com.payment_service.domain.Payment.enums.PaymentStatus;
import com.payment_service.domain.Payment.enums.PaymentType;
import com.payment_service.domain.PaymentAuditLog.entity.PaymentAuditLog;
import com.payment_service.domain.PaymentFailure.entity.PaymentFailure;
import com.payment_service.domain.Refund.entity.Refund;
import com.payment_service.domain.Transaction.entity.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(name = "idx_payment_reference", columnList = "paymentReference"),
                @Index(name = "idx_payment_status", columnList = "status"),
                @Index(name = "idx_payment_source_account", columnList = "sourceAccountId"),
                @Index(name = "idx_payment_destination_account", columnList = "destinationAccountId")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Payment extends BaseEntity {

    @NotBlank(message = "Payment reference is required")
    @Size(max = 100, message = "Payment reference cannot exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String paymentReference;

    @NotBlank(message = "Source account ID is required")
    @Size(max = 100, message = "Source account ID cannot exceed 100 characters")
    @Column(nullable = false)
    private String sourceAccountId;

    @NotBlank(message = "Destination account ID is required")
    @Size(max = 100, message = "Destination account ID cannot exceed 100 characters")
    @Column(nullable = false)
    private String destinationAccountId;

    @NotNull(message = "Amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Amount must be greater than zero"
    )
    @Digits(
            integer = 18,
            fraction = 2,
            message = "Amount format is invalid"
    )
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CurrencyCode currency;

    @NotNull(message = "Payment type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @NotNull(message = "Payment method is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;

    @NotBlank(message = "Initiated by is required")
    @Size(max = 100, message = "Initiated by cannot exceed 100 characters")
    @Column(nullable = false)
    private String initiatedBy;

    @Builder.Default
    @Column(nullable = false)
    private Boolean refunded = false;

    @OneToMany(
            mappedBy = "payment",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private java.util.List<Transaction> transactions;

    @OneToMany(
            mappedBy = "payment",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private java.util.List<Refund> refunds;

    @OneToMany(
            mappedBy = "payment",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private java.util.List<PaymentAuditLog> auditLogs;

    @OneToMany(
            mappedBy = "payment",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private java.util.List<PaymentFailure> failures;

    @OneToMany(
            mappedBy = "payment",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private java.util.List<GatewayTransaction> gatewayTransactions;

    @OneToOne(
            mappedBy = "payment",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private IdempotencyKey idempotencyKey;

}
