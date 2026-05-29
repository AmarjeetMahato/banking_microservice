package com.account_service.domain.accounts.dtos;

import com.account_service.domain.accounts.enums.AccountStatus;
import com.account_service.domain.accounts.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {

    private String id;

    private String userId;

    private String accountNumber;

    private AccountType accountType;

    private AccountStatus status;

    private String currency;

    private BigDecimal availableBalance;

    private BigDecimal holdBalance;

    private Boolean kycVerified;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
