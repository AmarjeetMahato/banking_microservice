package com.payment_service.domain.PaymentAuditLog.entity;


import com.payment_service.base.BaseEntity;
import com.payment_service.domain.Payment.entity.Payment;
import com.payment_service.domain.Payment.enums.PaymentStatus;
import com.payment_service.domain.PaymentAuditLog.enums.PaymentAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "payment_audit_logs",
        indexes = {
                @Index(name = "idx_payment_audit_payment", columnList = "payment_id"),
                @Index(name = "idx_payment_audit_action_time", columnList = "actionTime"),
                @Index(name = "idx_payment_audit_performed_by", columnList = "performedBy")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PaymentAuditLog extends BaseEntity {

    @NotNull(message = "Payment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private PaymentStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus newStatus;

    @NotNull(message = "Payment action is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentAction action;

    @NotBlank(message = "Performed by is required")
    @Size(max = 100, message = "Performed by cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String performedBy;

    @Size(max = 50, message = "Performed role cannot exceed 50 characters")
    @Column(length = 50)
    private String performedRole;

    @NotNull(message = "Action time is required")
    @Column(nullable = false)
    private LocalDateTime actionTime;

    @Size(max = 1000, message = "Remarks cannot exceed 1000 characters")
    @Column(length = 1000)
    private String remarks;

    @Size(max = 50, message = "IP address cannot exceed 50 characters")
    @Column(length = 50)
    private String ipAddress;

    @Size(max = 500, message = "Device information cannot exceed 500 characters")
    @Column(length = 500)
    private String deviceInfo;

    @Size(max = 100, message = "Reference ID cannot exceed 100 characters")
    @Column(length = 100)
    private String referenceId;

    @Builder.Default
    @Column(nullable = false)
    private Boolean systemGenerated = false;

}
