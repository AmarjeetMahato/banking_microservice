package com.account_service.domain.account_balance.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountBalanceResponseDto {

    private String id;

    private String accountId;

    private BigDecimal availableBalance;

    private BigDecimal holdBalance;

    private BigDecimal totalBalance;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
