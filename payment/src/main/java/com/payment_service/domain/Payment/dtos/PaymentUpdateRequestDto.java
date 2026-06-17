package com.payment_service.domain.Payment.dtos;


import com.payment_service.domain.Payment.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentUpdateRequestDto {
    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Boolean refunded;
}
