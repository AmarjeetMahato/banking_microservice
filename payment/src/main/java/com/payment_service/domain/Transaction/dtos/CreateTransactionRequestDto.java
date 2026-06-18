package com.payment_service.domain.Transaction.dtos;


import com.payment_service.domain.Transaction.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {

    @NotBlank(message = "Payment ID is required")
    private String paymentId;

    @NotBlank(message = "Source account ID is required")
    @Size(
            max = 100,
            message = "Source account ID cannot exceed 100 characters"
    )
    private String sourceAccountId;

    @NotBlank(message = "Destination account ID is required")
    @Size(
            max = 100,
            message = "Destination account ID cannot exceed 100 characters"
    )
    private String destinationAccountId;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Transaction amount must be greater than zero"
    )
    @Digits(
            integer = 18,
            fraction = 2,
            message = "Amount format is invalid"
    )
    private BigDecimal amount;

    @DecimalMin(
            value = "0.00",
            message = "Fee amount cannot be negative"
    )
    @Digits(
            integer = 18,
            fraction = 2,
            message = "Fee amount format is invalid"
    )
    private BigDecimal feeAmount;

    @DecimalMin(
            value = "0.00",
            message = "Tax amount cannot be negative"
    )
    @Digits(
            integer = 18,
            fraction = 2,
            message = "Tax amount format is invalid"
    )
    private BigDecimal taxAmount;

    @Size(
            max = 120,
            message = "External reference cannot exceed 120 characters"
    )
    private String externalReference;

}
