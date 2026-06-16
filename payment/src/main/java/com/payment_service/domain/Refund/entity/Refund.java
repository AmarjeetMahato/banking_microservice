package com.payment_service.domain.Refund.entity;


import com.payment_service.base.BaseEntity;
import com.payment_service.domain.Payment.entity.Payment;
import com.payment_service.domain.Refund.enums.RefundStatus;
import com.payment_service.domain.Refund.enums.RefundType;
import com.payment_service.domain.Transaction.entity.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "refunds",
        indexes = {
                @Index(name = "idx_refund_reference", columnList = "refundReference"),
                @Index(name = "idx_refund_payment", columnList = "payment_id"),
                @Index(name = "idx_refund_transaction", columnList = "transaction_id"),
                @Index(name = "idx_refund_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Refund extends BaseEntity {

    @NotBlank(message = "Refund reference is required")
    @Size(max = 100, message = "Refund reference cannot exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String refundReference;

    /**
     * Many refunds belong to one payment
     */
    @NotNull(message = "Payment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    /**
     * Optional: refund linked to specific transaction
     * (useful for partial refunds)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @NotNull(message = "Refund amount is required")
    @DecimalMin(value = "0.01", message = "Refund amount must be greater than zero")
    @Digits(integer = 18, fraction = 2, message = "Invalid refund amount format")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    @Column(length = 500)
    private String reason;

    @NotNull(message = "Refund status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status;

    /**
     * Who initiated refund (admin/system/user)
     */
    @NotBlank(message = "Requested by is required")
    @Size(max = 100, message = "Requested by cannot exceed 100 characters")
    @Column(nullable = false)
    private String requestedBy;

    /**
     * External gateway refund reference (Razorpay/Stripe)
     */
    @Size(max = 120, message = "Gateway reference cannot exceed 120 characters")
    @Column(length = 120)
    private String gatewayRefundReference;

    /**
     * When refund was processed successfully
     */
    private LocalDateTime processedAt;

    /**
     * Failure reason if refund fails
     */
    @Size(max = 500, message = "Failure reason cannot exceed 500 characters")
    @Column(length = 500)
    private String failureReason;

    /**
     * Refund type: FULL or PARTIAL
     */
    @NotNull(message = "Refund type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundType type;
}
