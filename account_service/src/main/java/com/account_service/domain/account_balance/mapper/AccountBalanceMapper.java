package com.account_service.domain.account_balance.mapper;

import com.account_service.domain.account_balance.dtos.AccountBalanceResponseDto;
import com.account_service.domain.account_balance.dtos.AccountBalanceSummaryDto;
import com.account_service.domain.account_balance.dtos.CreateAccountBalanceRequestDto;
import com.account_service.domain.account_balance.dtos.UpdateAccountBalanceRequestDto;
import com.account_service.domain.account_balance.entity.AccountBalance;
import com.account_service.domain.accounts.entity.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountBalanceMapper {
    /**
     * Create DTO -> Entity
     */
    public AccountBalance toEntity(
            CreateAccountBalanceRequestDto dto, Account account) {

        if (dto == null) {
            return null;
        }

        return AccountBalance.builder()
                .account(account)
                .availableBalance(dto.getAvailableBalance())
                .holdBalance(dto.getHoldBalance())
                .build();
    }

    /**
     * Update existing entity
     */
    public void updateEntity(
            AccountBalance accountBalance,
            UpdateAccountBalanceRequestDto dto
    ) {

        if (accountBalance == null || dto == null) {
            return;
        }

        accountBalance.setAvailableBalance(
                dto.getAvailableBalance()
        );

        accountBalance.setHoldBalance(
                dto.getHoldBalance()
        );
    }

    /**
     * Entity -> Response DTO
     */
    public AccountBalanceResponseDto toResponseDto(
            AccountBalance accountBalance
    ) {

        if (accountBalance == null) {
            return null;
        }

        BigDecimal available =
                accountBalance.getAvailableBalance() == null
                        ? BigDecimal.ZERO
                        : accountBalance.getAvailableBalance();

        BigDecimal hold =
                accountBalance.getHoldBalance() == null
                        ? BigDecimal.ZERO
                        : accountBalance.getHoldBalance();

        return AccountBalanceResponseDto.builder()
                .id(accountBalance.getId())
                .accountId(accountBalance.getAccount().getId())
                .availableBalance(available)
                .holdBalance(hold)
                .totalBalance(available.add(hold))
                .createdAt(accountBalance.getCreatedAt())
                .updatedAt(accountBalance.getUpdatedAt())
                .build();
    }

    /**
     * Entity -> Summary DTO
     */
    public AccountBalanceSummaryDto toSummaryDto(
            AccountBalance accountBalance
    ) {

        if (accountBalance == null) {
            return null;
        }

        BigDecimal available =
                accountBalance.getAvailableBalance() == null
                        ? BigDecimal.ZERO
                        : accountBalance.getAvailableBalance();

        BigDecimal hold =
                accountBalance.getHoldBalance() == null
                        ? BigDecimal.ZERO
                        : accountBalance.getHoldBalance();

        return AccountBalanceSummaryDto.builder()
                .accountId(accountBalance.getAccount().getId())
                .availableBalance(available)
                .holdBalance(hold)
                .totalBalance(available.add(hold))
                .build();
    }
}
