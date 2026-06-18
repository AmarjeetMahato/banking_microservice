package com.payment_service.domain.Transaction.dtos;


import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequestDto {

    @Size(max = 120, message = "External reference cannot exceed 120 characters")
    private String externalReference;

    @Size(max = 500, message = "Failure reason cannot exceed 500 characters")
    private String failureReason;
}
