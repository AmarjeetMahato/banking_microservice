package com.payment_service.domain.Transaction.dtos;


import com.payment_service.domain.Transaction.enums.TransactionStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatusUpdateRequestDto {

    @NotNull(message = "Transaction status is required")
    private TransactionStatus status;

    @Size(
            max = 500,
            message = "Failure reason cannot exceed 500 characters"
    )
    private String failureReason;
}
