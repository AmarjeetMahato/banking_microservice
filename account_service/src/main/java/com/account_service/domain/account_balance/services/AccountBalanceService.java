package com.account_service.domain.account_balance.services;

import com.account_service.domain.account_balance.dtos.*;

import java.math.BigDecimal;

public interface AccountBalanceService {

    AccountBalanceResponseDto createAccountBalance(
            CreateAccountBalanceRequestDto requestDto
    );

    AccountBalanceResponseDto getAccountBalanceById(
            String balanceId
    );

    AccountBalanceResponseDto getAccountBalanceByAccountId(
            String accountId
    );

    AccountBalanceResponseDto updateAccountBalance(
            String balanceId,
            UpdateAccountBalanceRequestDto requestDto
    );

    void deleteAccountBalance(
            String balanceId
    );

    AccountBalanceResponseDto creditBalance(
            BalanceAdjustmentRequestDto requestDto
    );

    AccountBalanceResponseDto debitBalance(
            BalanceAdjustmentRequestDto requestDto
    );

    AccountBalanceResponseDto placeHold(
            BalanceHoldRequestDto requestDto
    );

    AccountBalanceResponseDto releaseHold(BalanceReleaseRequestDto requestDto);

    void transferBalance(BalanceTransferRequestDto requestDto);

    AccountBalanceSummaryDto getBalanceSummary(String accountId);

    boolean hasSufficientBalance(String accountId, BigDecimal amount);

    BigDecimal getAvailableBalance(String accountId);

    BigDecimal getHoldBalance(String accountId);

}
