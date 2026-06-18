package com.payment_service.domain.Payment.dtos;

import com.payment_service.domain.Payment.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusUpdateResponseDto {
    private String paymentId;

    private String paymentReference;

    private PaymentStatus previousStatus;

    private PaymentStatus currentStatus;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private String message;
}
