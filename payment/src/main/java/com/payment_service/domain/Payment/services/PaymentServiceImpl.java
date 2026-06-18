package com.payment_service.domain.Payment.services;


import ch.qos.logback.core.util.StringUtil;
import com.payment_service.domain.GatewayTransaction.entity.GatewayTransaction;
import com.payment_service.domain.GatewayTransaction.repository.GatewayTransactionRepository;
import com.payment_service.domain.Payment.dtos.PaymentCreateRequestDto;
import com.payment_service.domain.Payment.dtos.PaymentResponseDto;
import com.payment_service.domain.Payment.dtos.PaymentStatusUpdateResponseDto;
import com.payment_service.domain.Payment.dtos.PaymentUpdateRequestDto;
import com.payment_service.domain.Payment.entity.Payment;
import com.payment_service.domain.Payment.enums.PaymentStatus;
import com.payment_service.domain.Payment.mappers.PaymentMapper;
import com.payment_service.domain.Payment.repository.PaymentRepository;
import com.payment_service.domain.Refund.entity.Refund;
import com.payment_service.globalException.AccessDeniedException;
import com.payment_service.globalException.BadRequestException;
import com.payment_service.globalException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements  PaymentService {

    private  final PaymentRepository paymentRepository;
    private  final PaymentMapper paymentMapper;
    private  final GatewayTransactionRepository gatewayTransactionRepository;

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
    public PaymentStatusUpdateResponseDto updatePaymentStatus(String paymentId, PaymentStatus status) {

        if (paymentId == null || paymentId.isBlank()) {
            throw new BadRequestException("Payment Id is required");
        }
        if(status == null ){
            throw  new BadRequestException("Payment status is required");
        }
        Payment payment = paymentRepository.findByIdAndDeletedFalse(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (Boolean.TRUE.equals(payment.getDeleted())) {
            throw new ResourceNotFoundException("Payment not found");
        }
        PaymentStatus previousStatus = payment.getStatus();

        if (previousStatus == status) {
            throw new BadRequestException(
                    "Payment is already in status " + status
            );
        }
        payment.setStatus(status);
        Payment savedPayment =  paymentRepository.save(payment);

        return  paymentMapper.toStatusResponseDto(savedPayment,previousStatus);
    }

//    @Override
//    public PaymentResponseDto refundPayment(String paymentId) {
//
//        // 1. Validate Input
//        if(StringUtil.isNullOrEmpty(paymentId)){
//             throw  new BadRequestException("Payment Id can't be null");
//        }
//        // 2. Fetch Payment
//        // 3. Verify Payment Exists
//        Payment payment = paymentRepository.findByIdAndDeletedFalse(paymentId).orElseThrow(
//                ()-> new ResourceNotFoundException("Payment not found"));
//
//        // 4. Verify Payment Status
//        if(payment.getStatus() != PaymentStatus.SUCCESS){
//             throw new BadRequestException("Only successful payments can be refunded");
//        }
//        // 5. Verify Refund Eligibility
//        if(payment.getStatus() == PaymentStatus.SUCCESS){
//            throw new BadRequestException("Payment already refunded");
//        }
//        // 6. Check Refund Window
//        // 7. Check Existing Refunds
//        BigDecimal remainingAmount =
//                payment.getAmount().subtract(payment.getAmount());
//
//        BigDecimal remainingAmount =
//                payment.getAmount()
//                        .subtract(payment.getRefundedAmount());
//
//        if(refundAmount.compareTo(remainingAmount) > 0){
//            throw new BadRequestException(
//                    "Refund amount exceeds remaining balance"
//            );
//        }
//        // 8. Verify Idempotency
//        if(payment.getCreatedAt()
//                .isBefore(LocalDateTime.now().minusDays(30))){
//            throw new BadRequestException("Refund window expired");
//        }
//
//        GatewayTransaction gatewayTransaction =
//                gatewayTransactionRepository.findByIdAndDeletedFalse(paymentId)
//                        .orElseThrow(() ->
//                                new BadRequestException("Gateway transaction not found"));
//
//        Optional<Refund> existingRefund =
//                refundRepository.findByIdempotencyKey(key);
//
//        if(existingRefund.isPresent()){
//            return mapper.toDto(existingRefund.get());
//        }
//    }
//

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
          if(accountId == null || accountId.isEmpty()){
                throw new BadRequestException("Account Id should not be null");
          }

//          TODO: Call the Account service for account details


    }

    @Override
    public Page<PaymentResponseDto> getPaymentsByUser(String userId, int page, int size) {
        return null;
    }

    @Override
    public void deletePayment(String paymentId) {
        if(paymentId == null || paymentId.isEmpty()){
            throw new BadRequestException("payment Id should not be null");
        }

        Payment payment = paymentRepository.findByIdAndDeletedFalse(paymentId).orElseThrow(
                ()-> new ResourceNotFoundException("Payment not found"));

//      TODO: Do some verification if needed

        payment.setDeleted(true);
        paymentRepository.save(payment);

    }
}
