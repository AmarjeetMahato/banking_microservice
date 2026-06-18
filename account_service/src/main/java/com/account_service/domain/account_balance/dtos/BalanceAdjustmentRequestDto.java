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
public class BalanceAdjustmentRequestDto {

    @NotBlank(message = "Account ID is required")
    private String accountId;

    @NotNull(message = "Adjustment amount is required")
    @DecimalMin(
            value = "0.01",
            inclusive = true,
            message = "Adjustment amount must be greater than 0.00"
    )
    private BigDecimal amount;

    @NotBlank(message = "Reason is required")
    @Size(
            min = 5,
            max = 500,
            message = "Reason must contain between 5 and 500 characters"
    )
    private String reason;
}
