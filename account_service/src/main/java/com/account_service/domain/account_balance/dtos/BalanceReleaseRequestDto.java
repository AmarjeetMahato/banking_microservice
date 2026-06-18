package com.account_service.domain.account_balance.dtos;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceReleaseRequestDto {
    @NotBlank(message = "Account ID is required")
    private String accountId;

    @NotNull(message = "Release amount is required")
    @DecimalMin(
            value = "0.01",
            inclusive = true,
            message = "Release amount must be greater than 0.00"
    )
    private BigDecimal amount;

    @NotBlank(message = "Reference number is required")
    private String referenceNumber;
}
