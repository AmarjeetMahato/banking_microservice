package com.account_service.domain.accounts.kafkaDtos;

import com.account_service.domain.accounts.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreatedEvent {

    private String accountId;

    private String userId;

    private String accountNumber;

    private AccountType accountType;

    private LocalDateTime createdAt;
}
