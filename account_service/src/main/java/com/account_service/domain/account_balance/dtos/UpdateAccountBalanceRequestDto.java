package com.account_service.domain.account_balance.dtos;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountBalanceRequestDto {


    @NotNull(message = "Available balance is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Available balance must be greater than or equal to 0.00"
    )
    private BigDecimal availableBalance;

    @NotNull(message = "Hold balance is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Hold balance must be greater than or equal to 0.00"
    )
    private BigDecimal holdBalance;

}
