package com.payment_service.domain.IdempotencyKey.entity;


import com.payment_service.base.BaseEntity;
import com.payment_service.domain.Payment.entity.Payment;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "idempotency_keys",
        indexes = {
                @Index(name = "idx_idempotency_key", columnList = "idempotencyKey"),
                @Index(name = "idx_idempotency_expiry", columnList = "expiresAt"),
                @Index(name = "idx_idempotency_payment", columnList = "payment_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_idempotency_key", columnNames = "idempotencyKey")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class IdempotencyKey extends BaseEntity {

    /**
     * Associated payment
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, unique = true)
    @NotNull(message = "Payment is required")
    private Payment payment;

    /**
     * Client-generated unique key
     */
    @NotBlank(message = "Idempotency key is required")
    @Size(min = 10, max = 255, message = "Idempotency key must be between 10 and 255 characters")
    @Column(nullable = false, unique = true, length = 255)
    private String idempotencyKey;

    /**
     * Hash of request payload
     */
    @NotBlank(message = "Request hash is required")
    @Size(max = 128, message = "Request hash cannot exceed 128 characters")
    @Column(nullable = false, length = 128)
    private String requestHash;

    /**
     * Serialized response
     */
    @Lob
    private String responseBody;

    /**
     * HTTP status returned
     */
    @Min(value = 100, message = "HTTP status must be at least 100")
    @Max(value = 599, message = "HTTP status cannot exceed 599")
    private Integer responseStatus;

    /**
     * Request endpoint
     */
    @NotBlank(message = "Request path is required")
    @Size(max = 500, message = "Request path cannot exceed 500 characters")
    @Column(nullable = false, length = 500)
    private String requestPath;

    /**
     * POST, PUT, PATCH
     */
    @NotBlank(message = "HTTP method is required")
    @Size(max = 20, message = "HTTP method cannot exceed 20 characters")
    @Column(nullable = false, length = 20)
    private String httpMethod;

    /**
     * Client identifier
     */
    @Size(max = 100, message = "Client ID cannot exceed 100 characters")
    @Column(length = 100)
    private String clientId;

    /**
     * User identifier
     */
    @Size(max = 100, message = "User ID cannot exceed 100 characters")
    @Column(length = 100)
    private String userId;

    /**
     * Expiry time
     */
    @NotNull(message = "Expiry time is required")
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Indicates whether request processing completed
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean completed = false;

    /**
     * First request timestamp
     */
    @NotNull(message = "Created request time is required")
    @Column(nullable = false)
    private LocalDateTime requestTime;

    /**
     * Last access time
     */
    private LocalDateTime lastAccessedAt;

    /**
     * Device information
     */
    @Size(max = 500, message = "Device information cannot exceed 500 characters")
    @Column(length = 500)
    private String deviceInfo;

    /**
     * Source IP
     */
    @Size(max = 50, message = "IP address cannot exceed 50 characters")
    @Column(length = 50)
    private String ipAddress;

}
