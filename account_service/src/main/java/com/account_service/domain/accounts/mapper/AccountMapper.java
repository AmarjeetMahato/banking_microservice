package com.account_service.domain.accounts.mapper;

import com.account_service.domain.account_balance.entity.AccountBalance;
import com.account_service.domain.accounts.dtos.AccountDto;
import com.account_service.domain.accounts.dtos.AccountResponse;
import com.account_service.domain.accounts.dtos.UpdateAccountDto;
import com.account_service.domain.accounts.entity.Account;
import com.account_service.domain.accounts.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {


    /**
     * Convert AccountDto -> Account Entity
     */
    public Account toEntity(AccountDto accountDto) {

        return Account.builder()
                .userId(accountDto.getUserId())
                .accountNumber(accountDto.getAccountNumber())
                .accountType(accountDto.getAccountType())
                .status(AccountStatus.ACTIVE)
                .currency(accountDto.getCurrency())
                .build();
    }

    /**
     * Convert Account Entity -> AccountResponse
     */
    public AccountResponse toResponse(Account account) {

        AccountBalance balance = account.getBalance();

        return AccountResponse.builder()
                .id(account.getId())
                .userId(account.getUserId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .currency(account.getCurrency())

                .availableBalance(
                        balance != null
                                ? balance.getAvailableBalance()
                                : null
                )

                .holdBalance(
                        balance != null
                                ? balance.getHoldBalance()
                                : null
                )

                .kycVerified(
                        account.getKycDetail() != null
                                && account.getKycDetail()
                                .getKycStatus()
                                .name()
                                .equals("VERIFIED")
                )

                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    /**
     * Update existing Account entity from UpdateAccountDto
     */
    public void updateEntity(
            Account account,
            UpdateAccountDto updateAccountDto
    ) {

        account.setAccountType(updateAccountDto.getAccountType());

        account.setStatus(updateAccountDto.getStatus());

        account.setCurrency(updateAccountDto.getCurrency());
    }
}

