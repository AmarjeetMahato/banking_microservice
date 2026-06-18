package com.payment_service.domain.Transaction.services;

import com.payment_service.domain.Transaction.dtos.CreateTransactionRequestDto;
import com.payment_service.domain.Transaction.dtos.TransactionResponseDto;
import com.payment_service.domain.Transaction.dtos.TransactionStatusUpdateResponseDto;
import com.payment_service.domain.Transaction.dtos.UpdateTransactionRequestDto;
import com.payment_service.domain.Transaction.enums.TransactionStatus;
import com.payment_service.domain.Transaction.enums.TransactionType;
import org.springframework.data.domain.Page;

public interface TransactionService {
    /**
     * Create a new transaction
     */
    TransactionResponseDto createTransaction(
            CreateTransactionRequestDto request
    );

    /**
     * Get transaction by ID
     */
    TransactionResponseDto getTransactionById(
            String transactionId
    );

    /**
     * Get transaction by reference
     */
    TransactionResponseDto getTransactionByReference(
            String transactionReference
    );

    /**
     * Update transaction metadata
     */
    TransactionResponseDto updateTransaction(
            String transactionId,
            UpdateTransactionRequestDto request
    );

    /**
     * Update transaction status
     */
    TransactionStatusUpdateResponseDto updateTransactionStatus(
            String transactionId,
            TransactionStatus status,
            String failureReason
    );

    /**
     * Soft delete transaction
     */
    void deleteTransaction(
            String transactionId
    );

    /**
     * Get all transactions
     */
    Page<TransactionResponseDto> getAllTransactions(
            int page,
            int size
    );

    /**
     * Get transactions by payment
     */
    Page<TransactionResponseDto> getTransactionsByPaymentId(
            String paymentId,
            int page,
            int size
    );

    /**
     * Get transactions by source account
     */
    Page<TransactionResponseDto> getTransactionsBySourceAccount(
            String sourceAccountId,
            int page,
            int size
    );

    /**
     * Get transactions by destination account
     */
    Page<TransactionResponseDto> getTransactionsByDestinationAccount(
            String destinationAccountId,
            int page,
            int size
    );

    /**
     * Get transactions by status
     */
    Page<TransactionResponseDto> getTransactionsByStatus(
            TransactionStatus status,
            int page,
            int size
    );

    /**
     * Get transactions by type
     */
    Page<TransactionResponseDto> getTransactionsByType(
            TransactionType type,
            int page,
            int size
    );

}
