package com.account_service.domain.account_limits.dtos;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAccountLimitRequestDto {

    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Daily transaction limit must be greater than or equal to 0"
    )
    private BigDecimal dailyLimit;

    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Monthly transaction limit must be greater than or equal to 0"
    )
    private BigDecimal monthlyLimit;

    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Per transaction limit must be greater than or equal to 0"
    )
    private BigDecimal perTransactionLimit;

}
