package com.payment_service.domain.Transaction.dtos;

import com.payment_service.domain.Transaction.enums.TransactionStatus;
import com.payment_service.domain.Transaction.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private String id;

    private String transactionReference;

    private String paymentId;

    private String paymentReference;

    private String sourceAccountId;

    private String destinationAccountId;

    private TransactionType type;

    private TransactionStatus status;

    private BigDecimal amount;

    private BigDecimal feeAmount;

    private BigDecimal taxAmount;

    private BigDecimal netAmount;

    private String externalReference;

    private String failureReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

}
