package com.payment_service.domain.Transaction.dtos;

import com.payment_service.domain.Transaction.enums.TransactionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatusUpdateResponseDto {
    private String transactionId;

    private String transactionReference;

    private TransactionStatus previousStatus;

    private TransactionStatus currentStatus;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private String message;
}
