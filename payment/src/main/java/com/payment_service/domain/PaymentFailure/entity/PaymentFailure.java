package com.payment_service.domain.PaymentFailure.entity;


import com.payment_service.base.BaseEntity;
import com.payment_service.domain.Payment.entity.Payment;
import com.payment_service.domain.PaymentFailure.enums.FailureCategory;
import com.payment_service.domain.PaymentFailure.enums.FailureSeverity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "payment_failures",
        indexes = {
                @Index(name = "idx_failure_payment", columnList = "payment_id"),
                @Index(name = "idx_failure_code", columnList = "errorCode"),
                @Index(name = "idx_failure_category", columnList = "failureCategory"),
                @Index(name = "idx_failure_created", columnList = "createdAt")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PaymentFailure extends BaseEntity {

    @NotNull(message = "Payment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Size(
            max = 100,
            message = "Transaction reference cannot exceed 100 characters"
    )
    @Column(length = 100)
    private String transactionReference;

    @NotBlank(message = "Error code is required")
    @Size(max = 100, message = "Error code cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String errorCode;

    @NotBlank(message = "Error message is required")
    @Size(
            max = 1000,
            message = "Error message cannot exceed 1000 characters"
    )
    @Column(nullable = false, length = 1000)
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Failure category is required")
    @Column(nullable = false)
    private FailureCategory failureCategory;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Failure severity is required")
    @Column(nullable = false)
    private FailureSeverity failureSeverity;

    @Size(max = 5000, message = "Technical details cannot exceed 5000 characters")
    @Column(length = 5000)
    private String technicalDetails;

    @Size(max = 5000, message = "Gateway response cannot exceed 5000 characters")
    @Column(length = 5000)
    private String gatewayResponse;

    @Builder.Default
    @Column(nullable = false)
    private Integer retryCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Boolean retryable = false;

    private LocalDateTime nextRetryAt;

    @Size(max = 100, message = "Gateway name cannot exceed 100 characters")
    @Column(length = 100)
    private String gatewayName;

    @Size(max = 100, message = "External reference cannot exceed 100 characters")
    @Column(length = 100)
    private String externalReference;

    @Size(max = 50, message = "IP address cannot exceed 50 characters")
    @Column(length = 50)
    private String ipAddress;

    @Size(max = 500, message = "Device information cannot exceed 500 characters")
    @Column(length = 500)
    private String deviceInfo;

    @NotNull(message = "Failure time is required")
    @Column(nullable = false)
    private LocalDateTime failureTime;

    @Builder.Default
    @Column(nullable = false)
    private Boolean resolved = false;

    private LocalDateTime resolvedAt;

    @Size(max = 1000, message = "Resolution notes cannot exceed 1000 characters")
    @Column(length = 1000)
    private String resolutionNotes;

}
