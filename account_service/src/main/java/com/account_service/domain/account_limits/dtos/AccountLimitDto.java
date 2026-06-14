package com.account_service.domain.account_limits.dtos;

import com.account_service.domain.accounts.entity.Account;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountLimitDto {

    @NotBlank(message = "Account ID is required")
    private String accountId;

    @NotNull(message = "Daily transaction limit cannot be null")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Daily transaction limit must be greater than or equal to 0"
    )
    private BigDecimal dailyLimit;

    @NotNull(message = "Monthly transaction limit cannot be null")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Monthly transaction limit must be greater than or equal to 0"
    )
    private BigDecimal monthlyLimit;

    @NotNull(message = "Per transaction limit cannot be null")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Per transaction limit must be greater than or equal to 0"
    )
    private BigDecimal perTransactionLimit;
}
