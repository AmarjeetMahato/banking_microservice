package com.account_service.domain.account_limits.services;

import com.account_service.domain.account_limits.dtos.AccountLimitDto;
import com.account_service.domain.account_limits.dtos.AccountLimitResponseDto;
import com.account_service.domain.account_limits.dtos.UpdateAccountLimitRequestDto;
import com.account_service.domain.account_limits.entity.AccountLimit;
import com.account_service.domain.account_limits.mapper.AccountLimitMapper;
import com.account_service.domain.account_limits.repository.AccountLimitsRepository;
import com.account_service.domain.accounts.entity.Account;
import com.account_service.domain.accounts.repository.AccountRepository;
import com.account_service.globalException.BadRequestException;
import com.account_service.globalException.ResourceAlreadyExistsException;
import com.account_service.globalException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountLimitServiceImpl implements  AccountLimitService {

    private  final AccountLimitsRepository accountLimitsRepository;
    private final AccountLimitMapper accountLimitMapper;
    private  final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountLimitResponseDto createAccountLimitTransaction(
            AccountLimitDto accountLimitDto
    ) {

        if (accountLimitDto == null) {
            throw new BadRequestException("Account limit data should not be null");
        }

        Account account = accountRepository.findById(accountLimitDto.getAccountId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found"));

        boolean exists = accountLimitsRepository.existsByAccountId(account.getId());

        if (exists) {
            throw new ResourceAlreadyExistsException(
                    "Account limit already exists for this account"
            );
        }

        validateLimits(
                accountLimitDto.getDailyLimit(),
                accountLimitDto.getMonthlyLimit(),
                accountLimitDto.getPerTransactionLimit()
        );

        AccountLimit accountLimit =
                accountLimitMapper.toEntity(accountLimitDto, account);

        AccountLimit savedAccountLimit =
                accountLimitsRepository.save(accountLimit);

        return accountLimitMapper.toResponseDto(savedAccountLimit);
    }

    @Override
    @Transactional
    public AccountLimitResponseDto updateAccountLimitTransaction(
            UpdateAccountLimitRequestDto accountLimitRequestDto,
            String accountLimitId
    ) {

        if (accountLimitRequestDto == null) {
            throw new BadRequestException("Update request should not be null");
        }

        AccountLimit accountLimit = accountLimitsRepository
                .findById(accountLimitId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account limit not found"
                        ));

        validateLimitsForUpdate(
                accountLimitRequestDto,
                accountLimit
        );

        accountLimitMapper.toUpdateEntity(
                accountLimitRequestDto,
                accountLimit
        );

        AccountLimit updatedAccountLimit =
                accountLimitsRepository.save(accountLimit);

        return accountLimitMapper.toResponseDto(updatedAccountLimit);
    }

    @Override
    public AccountLimitResponseDto fetchAccountLimitById(String accountLimitId) {
        try {
            AccountLimit accountLimit = accountLimitsRepository.findById(accountLimitId).orElseThrow(
                    ()-> new ResourceNotFoundException("Account limit data not found")
            );
            return  accountLimitMapper.toResponseDto(accountLimit);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateLimits(
            BigDecimal dailyLimit,
            BigDecimal monthlyLimit,
            BigDecimal perTransactionLimit
    ) {

        if (dailyLimit.compareTo(monthlyLimit) > 0) {
            throw new BadRequestException(
                    "Daily limit cannot exceed monthly limit"
            );
        }

        if (perTransactionLimit.compareTo(dailyLimit) > 0) {
            throw new BadRequestException(
                    "Per transaction limit cannot exceed daily limit"
            );
        }
    }


    private void validateLimitsForUpdate(
            UpdateAccountLimitRequestDto dto,
            AccountLimit accountLimit
    ) {

        BigDecimal dailyLimit =
                dto.getDailyLimit() != null
                        ? dto.getDailyLimit()
                        : accountLimit.getDailyLimit();

        BigDecimal monthlyLimit =
                dto.getMonthlyLimit() != null
                        ? dto.getMonthlyLimit()
                        : accountLimit.getMonthlyLimit();

        BigDecimal perTransactionLimit =
                dto.getPerTransactionLimit() != null
                        ? dto.getPerTransactionLimit()
                        : accountLimit.getPerTransactionLimit();

        validateLimits(
                dailyLimit,
                monthlyLimit,
                perTransactionLimit
        );
    }
}
