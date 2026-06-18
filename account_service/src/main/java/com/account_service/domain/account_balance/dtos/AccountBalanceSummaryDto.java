package com.account_service.domain.account_balance.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountBalanceSummaryDto {

    private String accountId;

    private BigDecimal availableBalance;

    private BigDecimal holdBalance;

    private BigDecimal totalBalance;

    private String currency;

    private String accountNumber;
}
