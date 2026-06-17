package com.payment_service.domain.Payment.dtos;

import com.payment_service.domain.Payment.enums.CurrencyCode;
import com.payment_service.domain.Payment.enums.PaymentMethod;
import com.payment_service.domain.Payment.enums.PaymentType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreateRequestDto {


    @NotBlank(message = "Source account ID is required")
    @Size(max = 100, message = "Source account ID cannot exceed 100 characters")
    private String sourceAccountId;

    @NotBlank(message = "Destination account ID is required")
    @Size(max = 100, message = "Destination account ID cannot exceed 100 characters")
    private String destinationAccountId;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero")
    @Digits(integer = 18, fraction = 2, message = "Payment amount format is invalid")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    private CurrencyCode currency;

    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Initiator user ID is required")
    @Size(max = 100, message = "Initiator user ID cannot exceed 100 characters")
    private String initiatedBy;
}
