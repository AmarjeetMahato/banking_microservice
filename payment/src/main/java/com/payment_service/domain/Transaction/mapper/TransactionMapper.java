package com.payment_service.domain.Transaction.mapper;

import com.payment_service.domain.Payment.entity.Payment;
import com.payment_service.domain.Transaction.dtos.CreateTransactionRequestDto;
import com.payment_service.domain.Transaction.dtos.TransactionResponseDto;
import com.payment_service.domain.Transaction.dtos.TransactionStatusUpdateResponseDto;
import com.payment_service.domain.Transaction.dtos.UpdateTransactionRequestDto;
import com.payment_service.domain.Transaction.entity.Transaction;
import com.payment_service.domain.Transaction.enums.TransactionStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionMapper {

    public Transaction toEntity(
            CreateTransactionRequestDto request,
            Payment payment
    ) {

        if (request == null) {
            return null;
        }

        BigDecimal feeAmount = request.getFeeAmount() == null
                ? BigDecimal.ZERO
                : request.getFeeAmount();

        BigDecimal taxAmount = request.getTaxAmount() == null
                ? BigDecimal.ZERO
                : request.getTaxAmount();

        BigDecimal netAmount = request.getAmount()
                .subtract(feeAmount)
                .subtract(taxAmount);

        return Transaction.builder()
                .payment(payment)
                .sourceAccountId(request.getSourceAccountId())
                .destinationAccountId(request.getDestinationAccountId())
                .type(request.getType())
                .status(TransactionStatus.PENDING)
                .amount(request.getAmount())
                .feeAmount(feeAmount)
                .taxAmount(taxAmount)
                .netAmount(netAmount)
                .externalReference(request.getExternalReference())
                .build();
    }

    public TransactionResponseDto toResponseDto(
            Transaction transaction
    ) {

        if (transaction == null) {
            return null;
        }

        return TransactionResponseDto.builder()
                .id(transaction.getId())
                .transactionReference(transaction.getTransactionReference())

                .paymentId(
                        transaction.getPayment() != null
                                ? transaction.getPayment().getId()
                                : null
                )

                .paymentReference(
                        transaction.getPayment() != null
                                ? transaction.getPayment().getPaymentReference()
                                : null
                )

                .sourceAccountId(transaction.getSourceAccountId())
                .destinationAccountId(transaction.getDestinationAccountId())

                .type(transaction.getType())
                .status(transaction.getStatus())

                .amount(transaction.getAmount())
                .feeAmount(transaction.getFeeAmount())
                .taxAmount(transaction.getTaxAmount())
                .netAmount(transaction.getNetAmount())

                .externalReference(transaction.getExternalReference())
                .failureReason(transaction.getFailureReason())

                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .updatedBy("")
                .build();
    }

    public void updateEntity(Transaction transaction, UpdateTransactionRequestDto request) {

        if (transaction == null || request == null) {
            return;
        }

        if (request.getExternalReference() != null) {
            transaction.setExternalReference(
                    request.getExternalReference()
            );
        }

        if (request.getFailureReason() != null) {
            transaction.setFailureReason(
                    request.getFailureReason()
            );
        }
    }

    public TransactionStatusUpdateResponseDto toStatusResponseDto(
            Transaction transaction,
            TransactionStatus previousStatus
    ) {

        if (transaction == null) {
            return null;
        }

        return TransactionStatusUpdateResponseDto.builder()
                .transactionId(transaction.getId())
                .transactionReference(
                        transaction.getTransactionReference()
                )
                .previousStatus(previousStatus)
                .currentStatus(transaction.getStatus())
                .updatedAt(transaction.getUpdatedAt())
                .updatedBy("")
                .message("Transaction status updated successfully")
                .build();
    }

}
