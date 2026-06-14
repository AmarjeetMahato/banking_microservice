package com.account_service.domain.account_limits.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountLimitResponseDto {
    private String id;

    private String accountId;

    private BigDecimal dailyLimit;

    private BigDecimal monthlyLimit;

    private BigDecimal perTransactionLimit;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
