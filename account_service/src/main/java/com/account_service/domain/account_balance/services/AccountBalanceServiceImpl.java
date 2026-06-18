package com.account_service.domain.account_balance.services;


import com.account_service.domain.account_balance.dtos.*;
import com.account_service.domain.account_balance.entity.AccountBalance;
import com.account_service.domain.account_balance.mapper.AccountBalanceMapper;
import com.account_service.domain.account_balance.repository.AccountBalanceRepository;
import com.account_service.domain.accounts.entity.Account;
import com.account_service.domain.accounts.enums.AccountStatus;
import com.account_service.domain.accounts.repository.AccountRepository;
import com.account_service.globalException.BadRequestException;
import com.account_service.globalException.InternalServerError;
import com.account_service.globalException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountBalanceServiceImpl implements AccountBalanceService {

    private  final AccountBalanceRepository accountBalanceRepository;
    private  final AccountBalanceMapper accountBalanceMapper;
    private  final AccountRepository accountRepository;

    @Override
    public AccountBalanceResponseDto createAccountBalance(CreateAccountBalanceRequestDto requestDto) {

        try {
            Account account  = accountRepository.findByIdAndDeletedFalse(requestDto.getAccountId()).orElseThrow(()->
                     new ResourceNotFoundException("Account not found")
                    );
            if (accountBalanceRepository.existsByAccountId(account.getId())) {
                throw new RuntimeException("Balance already exists for account");
            }

            AccountBalance accountBalance =  accountBalanceMapper.toEntity(requestDto,account);
            AccountBalance saved = accountBalanceRepository.save(accountBalance);

            return accountBalanceMapper.toResponseDto(saved);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AccountBalanceResponseDto getAccountBalanceById(String balanceId) {

        // 1. Validate input
        if (balanceId == null || balanceId.trim().isEmpty()) {
            throw new BadRequestException("Balance ID cannot be null or empty");
        }

        // 2. Fetch from DB
        AccountBalance accountBalance = accountBalanceRepository.findByIdAndDeletedFalse(balanceId)
                .orElseThrow(() ->
                        new RuntimeException("Account balance not found")
                );
        // 4. Map to response DTO
        return accountBalanceMapper.toResponseDto(accountBalance);
    }

    @Override
    public AccountBalanceResponseDto getAccountBalanceByAccountId(String accountId) {
        // 1. Validate input
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new BadRequestException("Account ID cannot be null or empty");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found with id")
                );
        if(account.getStatus() != AccountStatus.ACTIVE){
            throw new BadRequestException("Account is not active");
        }

        // 2. Fetch from DB
        AccountBalance accountBalance = accountBalanceRepository.findByAccountIdAndDeletedFalse(accountId)
                .orElseThrow(() -> new RuntimeException("Account balance not found"));
        return accountBalanceMapper.toResponseDto(accountBalance);

    }

    @Override
    public AccountBalanceResponseDto updateAccountBalance(String balanceId, UpdateAccountBalanceRequestDto requestDto) {
        try {
            if(balanceId == null || balanceId.trim().isEmpty()){
                throw  new BadRequestException("Balance ID cannot be null or empty");
            }
            if (requestDto == null) {
                throw new BadRequestException("Request body cannot be null");
            }

            // 2. Fetch balance
            AccountBalance accountBalance = accountBalanceRepository.findById(balanceId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Account balance not found ")
                    );
            // 3. Soft delete check (if applicable)
            if (accountBalance.getDeleted()) {
                throw new BadRequestException("Cannot update deleted account balance");
            }

            // 4. Banking validation (NO NEGATIVE VALUES)
            if (requestDto.getAvailableBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("Available balance cannot be negative");
            }

            if (requestDto.getHoldBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("Hold balance cannot be negative");
            }

            accountBalanceMapper.updateEntity(accountBalance,requestDto);
            AccountBalance savedAccountBalance = accountBalanceRepository.save(accountBalance);

            return  accountBalanceMapper.toResponseDto(savedAccountBalance);
        }  catch (InternalServerError e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteAccountBalance(String balanceId) {

    }

    @Override
    public AccountBalanceResponseDto creditBalance(BalanceAdjustmentRequestDto requestDto) {
        return null;
    }

    @Override
    public AccountBalanceResponseDto debitBalance(BalanceAdjustmentRequestDto requestDto) {
        return null;
    }

    @Override
    public AccountBalanceResponseDto placeHold(BalanceHoldRequestDto requestDto) {
        return null;
    }

    @Override
    public AccountBalanceResponseDto releaseHold(BalanceReleaseRequestDto requestDto) {
        return null;
    }

    @Override
    public void transferBalance(BalanceTransferRequestDto requestDto) {

    }

    @Override
    public AccountBalanceSummaryDto getBalanceSummary(String accountId) {
        return null;
    }

    @Override
    public boolean hasSufficientBalance(String accountId, BigDecimal amount) {
        return false;
    }

    @Override
    public BigDecimal getAvailableBalance(String accountId) {
        return null;
    }

    @Override
    public BigDecimal getHoldBalance(String accountId) {
        return null;
    }
}
