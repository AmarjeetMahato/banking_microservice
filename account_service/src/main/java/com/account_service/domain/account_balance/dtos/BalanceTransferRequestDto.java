package com.account_service.domain.account_balance.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceTransferRequestDto {

    @NotBlank(message = "Source account ID is required")
    private String fromAccountId;

    @NotBlank(message = "Destination account ID is required")
    private String toAccountId;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(
            value = "0.01",
            inclusive = true,
            message = "Transfer amount must be greater than 0.00"
    )
    private BigDecimal amount;

    @NotBlank(message = "Transfer reference is required")
    @Size(min = 5, max = 100,
            message = "Transfer reference must contain between 5 and 100 characters"
    )
    private String referenceNumber;

    @Size(
            max = 500,
            message = "Description cannot exceed 500 characters"
    )
    private String description;
}
