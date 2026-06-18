package com.payment_service.domain.Transaction.services;

import com.payment_service.domain.Transaction.dtos.CreateTransactionRequestDto;
import com.payment_service.domain.Transaction.dtos.TransactionResponseDto;
import com.payment_service.domain.Transaction.dtos.TransactionStatusUpdateResponseDto;
import com.payment_service.domain.Transaction.dtos.UpdateTransactionRequestDto;
import com.payment_service.domain.Transaction.enums.TransactionStatus;
import com.payment_service.domain.Transaction.enums.TransactionType;
import com.payment_service.domain.Transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements  TransactionService {

    private  final TransactionRepository transactionRepository;

    @Override
    public TransactionResponseDto createTransaction(CreateTransactionRequestDto request) {
        return null;
    }

    @Override
    public TransactionResponseDto getTransactionById(String transactionId) {
        return null;
    }

    @Override
    public TransactionResponseDto getTransactionByReference(String transactionReference) {
        return null;
    }

    @Override
    public TransactionResponseDto updateTransaction(String transactionId, UpdateTransactionRequestDto request) {
        return null;
    }

    @Override
    public TransactionStatusUpdateResponseDto updateTransactionStatus(String transactionId, TransactionStatus status, String failureReason) {
        return null;
    }

    @Override
    public void deleteTransaction(String transactionId) {

    }

    @Override
    public Page<TransactionResponseDto> getAllTransactions(int page, int size) {
        return null;
    }

    @Override
    public Page<TransactionResponseDto> getTransactionsByPaymentId(String paymentId, int page, int size) {
        return null;
    }

    @Override
    public Page<TransactionResponseDto> getTransactionsBySourceAccount(String sourceAccountId, int page, int size) {
        return null;
    }

    @Override
    public Page<TransactionResponseDto> getTransactionsByDestinationAccount(String destinationAccountId, int page, int size) {
        return null;
    }

    @Override
    public Page<TransactionResponseDto> getTransactionsByStatus(TransactionStatus status, int page, int size) {
        return null;
    }

    @Override
    public Page<TransactionResponseDto> getTransactionsByType(TransactionType type, int page, int size) {
        return null;
    }
}
