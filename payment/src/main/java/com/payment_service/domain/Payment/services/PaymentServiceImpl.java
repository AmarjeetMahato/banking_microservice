package com.payment_service.domain.Payment.services;


import com.payment_service.domain.Payment.dtos.PaymentCreateRequestDto;
import com.payment_service.domain.Payment.dtos.PaymentResponseDto;
import com.payment_service.domain.Payment.dtos.PaymentUpdateRequestDto;
import com.payment_service.domain.Payment.entity.Payment;
import com.payment_service.domain.Payment.enums.PaymentStatus;
import com.payment_service.domain.Payment.mappers.PaymentMapper;
import com.payment_service.domain.Payment.repository.PaymentRepository;
import com.payment_service.globalException.AccessDeniedException;
import com.payment_service.globalException.BadRequestException;
import com.payment_service.globalException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements  PaymentService {

    private  final PaymentRepository paymentRepository;
    private  final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponseDto createPayment(PaymentCreateRequestDto requestDto) {

        try {
            if(requestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0){
               throw new BadRequestException("Invalid amount");
            }

//       TODO: call Account Service for Account Details for both Source and destination account
//       TODO: Is both account active or  not Payment limit check

            if(requestDto.getSourceAccountId().equals(requestDto.getDestinationAccountId())){
                 throw new BadRequestException("Source and destination accounts cannot be same");
            }

//        TODO: KYC verification check by UserId
//        TODO: 8. AML Check
//        TODO: Anti-Money Laundering validation. Call Account for Daily Transaction Limit,Monthly Transaction Limit
            //       TODO:  Account Balance Check
//        TODO:  Minimum Balance Check (for Zero balance account or minimum balance account)



            Payment payment = paymentMapper.toEntity(requestDto);
            Payment savedPayment = paymentRepository.save(payment);
            return  paymentMapper.toResponseDto(savedPayment);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentResponseDto getPaymentById(String paymentId) {
        // 1. Validate input
        if(paymentId == null || paymentId.isEmpty()){
             throw  new BadRequestException("Payment Id shouldn't be null");
        }
        // 2. Find and check id present or not also should not delete
        Payment payment = paymentRepository.findByIdAndDeletedFalse(paymentId).orElseThrow(()->
                new ResourceNotFoundException("Payment details not found"));

//       TODO: 7. Audit Logging


        return  paymentMapper.toResponseDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentByReference(String paymentReference) {
        if (paymentReference == null || paymentReference.isBlank()) {
            throw new BadRequestException("Payment reference is required");
        }


        Payment payment = paymentRepository.findByPaymentReferenceAndDeletedFalse(paymentReference)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (Boolean.TRUE.equals(payment.getDeleted())) {
            throw new ResourceNotFoundException("Payment not found");
        }

//        TODO: 4. Authorization Check A user should only access:Their own payment,Admin access,Support access

//        Check Payment Status
     if(payment.getStatus() == PaymentStatus.UNDER_INVESTIGATION){
         throw new AccessDeniedException( "Payment unavailable");
     }

        //       TODO: 7. Audit Logging
        return  paymentMapper.toResponseDto(payment);
    }

    @Override
    public PaymentResponseDto updatePayment(String paymentId, PaymentUpdateRequestDto requestDto) {
        return null;
    }

    @Override
    public PaymentResponseDto updatePaymentStatus(String paymentId, PaymentStatus status) {
        return null;
    }

    @Override
    public PaymentResponseDto refundPayment(String paymentId) {
        return null;
    }

    @Override
    public PaymentResponseDto cancelPayment(String paymentId) {
        return null;
    }

    @Override
    public Page<PaymentResponseDto> getAllPayments(int page, int size, String sortBy, String sortDirection) {
        return null;
    }

    @Override
    public Page<PaymentResponseDto> getPaymentsByStatus(PaymentStatus status, int page, int size) {
        return null;
    }

    @Override
    public Page<PaymentResponseDto> getPaymentsByAccount(String accountId, int page, int size) {
        return null;
    }

    @Override
    public Page<PaymentResponseDto> getPaymentsByUser(String userId, int page, int size) {
        return null;
    }

    @Override
    public void deletePayment(String paymentId) {

    }
}
