package com.payment_service.domain.Payment.mappers;

import com.payment_service.domain.Payment.dtos.PaymentCreateRequestDto;
import com.payment_service.domain.Payment.dtos.PaymentResponseDto;
import com.payment_service.domain.Payment.dtos.PaymentUpdateRequestDto;
import com.payment_service.domain.Payment.entity.Payment;
import com.payment_service.globalException.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMapper {

    /**
     * Convert Create DTO to Entity
     */
    public Payment toEntity(PaymentCreateRequestDto dto) {

        if (dto == null) {
            throw  new BadRequestException("Payment  details are required");
        }

        return Payment.builder()
                .sourceAccountId(dto.getSourceAccountId())
                .destinationAccountId(dto.getDestinationAccountId())
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .paymentType(dto.getPaymentType())
                .paymentMethod(dto.getPaymentMethod())
                .description(dto.getDescription())
                .initiatedBy(dto.getInitiatedBy())
                .refunded(false)
                .build();
    }

    /**
     * Convert Entity to Response DTO
     */
    public PaymentResponseDto toResponseDto(Payment payment) {

        if (payment == null) {
            return null;
        }

        return PaymentResponseDto.builder()
                .id(payment.getId())
                .paymentReference(payment.getPaymentReference())
                .sourceAccountId(payment.getSourceAccountId())
                .destinationAccountId(payment.getDestinationAccountId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentType(payment.getPaymentType())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .description(payment.getDescription())
                .initiatedBy(payment.getInitiatedBy())
                .refunded(payment.getRefunded())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    /**
     * Update Existing Entity
     */
    public void updateEntity(
            Payment payment,
            PaymentUpdateRequestDto dto
    ) {

        if (payment == null || dto == null) {
            return;
        }

        if (dto.getStatus() != null) {
            payment.setStatus(dto.getStatus());
        }

        if (dto.getDescription() != null) {
            payment.setDescription(dto.getDescription());
        }

        if (dto.getRefunded() != null) {
            payment.setRefunded(dto.getRefunded());
        }
    }

    /**
     * Convert List<Entity> to List<ResponseDto>
     */
    public List<PaymentResponseDto> toResponseDtoList(
            List<Payment> payments
    ) {

        return payments.stream()
                .map(this::toResponseDto)
                .toList();
    }

    /**
     * Convert Page<Entity> to Page<ResponseDto>
     */
    public Page<PaymentResponseDto> toResponseDtoPage(
            Page<Payment> paymentPage
    ) {

        return paymentPage.map(this::toResponseDto);
    }

}
