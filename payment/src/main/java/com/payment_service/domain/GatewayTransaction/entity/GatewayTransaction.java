package com.payment_service.domain.GatewayTransaction.entity;


import com.payment_service.base.BaseEntity;
import com.payment_service.domain.GatewayTransaction.enums.GatewayName;
import com.payment_service.domain.GatewayTransaction.enums.GatewayTransactionStatus;
import com.payment_service.domain.GatewayTransaction.enums.GatewayTransactionType;
import com.payment_service.domain.Payment.entity.Payment;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "gateway_transactions",
        indexes = {
                @Index(name = "idx_gateway_transaction_payment", columnList = "payment_id"),
                @Index(name = "idx_gateway_transaction_reference", columnList = "gatewayTransactionReference"),
                @Index(name = "idx_gateway_reference", columnList = "gatewayReference"),
                @Index(name = "idx_gateway_status", columnList = "status"),
                @Index(name = "idx_gateway_name", columnList = "gatewayName")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GatewayTransaction extends BaseEntity {


    @NotNull(message = "Payment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @NotBlank(message = "Gateway transaction reference is required")
    @Size(max = 100, message = "Gateway transaction reference cannot exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String gatewayTransactionReference;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gateway name is required")
    @Column(nullable = false)
    private GatewayName gatewayName;

    @Size(
            max = 150,
            message = "Gateway reference cannot exceed 150 characters"
    )
    @Column(length = 150)
    private String gatewayReference;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gateway transaction type is required")
    @Column(nullable = false)
    private GatewayTransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gateway status is required")
    @Column(nullable = false)
    private GatewayTransactionStatus status;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 18, fraction = 2, message = "Invalid amount format")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Size(max = 10, message = "Currency code cannot exceed 10 characters")
    @Column(length = 10)
    private String currency;

    /**
     * Raw request sent to gateway
     */
    @Lob
    private String requestPayload;

    /**
     * Raw response received
     */
    @Lob
    private String responsePayload;

    /**
     * Gateway error code
     */
    @Size(max = 100, message = "Error code cannot exceed 100 characters")
    @Column(length = 100)
    private String errorCode;

    /**
     * Gateway error message
     */
    @Size(max = 1000, message = "Error message cannot exceed 1000 characters")
    @Column(length = 1000)
    private String errorMessage;

    /**
     * Number of attempts
     */
    @Builder.Default
    @Min(value = 0, message = "Retry count cannot be negative")
    @Column(nullable = false)
    private Integer retryCount = 0;

    /**
     * Processing start time
     */
    private LocalDateTime initiatedAt;

    /**
     * Processing completion time
     */
    private LocalDateTime completedAt;

    /**
     * Round trip duration
     */
    @Min(value = 0, message = "Processing time cannot be negative")
    private Long processingTimeMs;

    /**
     * Callback received?
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean callbackReceived = false;

    /**
     * Callback received time
     */
    private LocalDateTime callbackReceivedAt;

    /**
     * Gateway endpoint URL
     */
    @Size(max = 1000, message = "Endpoint URL cannot exceed 1000 characters")
    @Column(length = 1000)
    private String endpointUrl;

    /**
     * Gateway account / merchant id
     */
    @Size(max = 100, message = "Merchant ID cannot exceed 100 characters")
    @Column(length = 100)
    private String merchantId;
}
