package com.payment_service.domain.Payment.services;


import com.payment_service.domain.Payment.dtos.PaymentCreateRequestDto;
import com.payment_service.domain.Payment.dtos.PaymentResponseDto;
import com.payment_service.domain.Payment.dtos.PaymentStatusUpdateResponseDto;
import com.payment_service.domain.Payment.dtos.PaymentUpdateRequestDto;
import com.payment_service.domain.Payment.enums.PaymentStatus;
import org.springframework.data.domain.Page;

public interface PaymentService {
    /**
     * Create a new payment
     */
    PaymentResponseDto createPayment(PaymentCreateRequestDto requestDto);

    /**
     * Get payment by ID
     */
    PaymentResponseDto getPaymentById(String paymentId);

    /**
     * Get payment by reference number
     */
    PaymentResponseDto getPaymentByReference(String paymentReference);

    /**
     * Update payment details
     */
    PaymentResponseDto updatePayment(String paymentId, PaymentUpdateRequestDto requestDto);

    /**
     * Update payment status
     */
    PaymentStatusUpdateResponseDto updatePaymentStatus(String paymentId, PaymentStatus status);

    /**
     * Refund payment
     */
//    PaymentResponseDto refundPayment(String paymentId);

    /**
     * Cancel payment
     */
    PaymentResponseDto cancelPayment(String paymentId);

    /**
     * Get all payments
     */
    Page<PaymentResponseDto> getAllPayments(
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    /**
     * Get payments by status
     */
    Page<PaymentResponseDto> getPaymentsByStatus(
            PaymentStatus status,
            int page,
            int size
    );

    /**
     * Get payments by account
     */
    Page<PaymentResponseDto> getPaymentsByAccount(
            String accountId,
            int page,
            int size
    );

    /**
     * Get payments initiated by user
     */
    Page<PaymentResponseDto> getPaymentsByUser(
            String userId,
            int page,
            int size
    );

    /**
     * Delete payment (soft delete)
     */
    void deletePayment(String paymentId);
}
