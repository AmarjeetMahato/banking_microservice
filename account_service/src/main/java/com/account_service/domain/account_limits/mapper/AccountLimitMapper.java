package com.account_service.domain.account_limits.mapper;

import com.account_service.domain.account_limits.dtos.AccountLimitDto;
import com.account_service.domain.account_limits.dtos.AccountLimitResponseDto;
import com.account_service.domain.account_limits.dtos.UpdateAccountLimitRequestDto;
import com.account_service.domain.account_limits.entity.AccountLimit;
import com.account_service.domain.accounts.entity.Account;
import com.account_service.globalException.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class AccountLimitMapper {

    public AccountLimit toEntity(AccountLimitDto dto, Account account) {

        if (dto == null) {
            throw new BadRequestException("Account limit data should not be null");
        }

        if (account == null) {
            throw new BadRequestException("Account should not be null");
        }

        return AccountLimit.builder()
                .account(account)
                .dailyLimit(dto.getDailyLimit())
                .monthlyLimit(dto.getMonthlyLimit())
                .perTransactionLimit(dto.getPerTransactionLimit())
                .build();
    }

    public void toUpdateEntity(
            UpdateAccountLimitRequestDto dto,
            AccountLimit accountLimit
    ) {

        if (dto == null) {
            throw new BadRequestException("Update data should not be null");
        }

        if (accountLimit == null) {
            throw new BadRequestException("Account limit should not be null");
        }

        if (dto.getDailyLimit() != null) {
            accountLimit.setDailyLimit(dto.getDailyLimit());
        }

        if (dto.getMonthlyLimit() != null) {
            accountLimit.setMonthlyLimit(dto.getMonthlyLimit());
        }

        if (dto.getPerTransactionLimit() != null) {
            accountLimit.setPerTransactionLimit(dto.getPerTransactionLimit());
        }
    }

    public AccountLimitResponseDto toResponseDto(AccountLimit accountLimit) {

        if (accountLimit == null) {
            throw new BadRequestException("Account limit should not be null");
        }

        return AccountLimitResponseDto.builder()
                .id(accountLimit.getId())
                .accountId(accountLimit.getAccount().getId())
                .dailyLimit(accountLimit.getDailyLimit())
                .monthlyLimit(accountLimit.getMonthlyLimit())
                .perTransactionLimit(accountLimit.getPerTransactionLimit())
                .createdAt(accountLimit.getCreatedAt())
                .updatedAt(accountLimit.getUpdatedAt())
                .build();
    }
}
