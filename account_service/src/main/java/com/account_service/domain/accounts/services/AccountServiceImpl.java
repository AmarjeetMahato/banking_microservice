package com.account_service.domain.accounts.services;

import com.account_service.config.AccountEventProducer;
import com.account_service.domain.account_balance.entity.AccountBalance;
import com.account_service.domain.account_balance.repository.AccountBalanceRepository;
import com.account_service.domain.account_limits.entity.AccountLimit;
import com.account_service.domain.account_limits.repository.AccountLimitsRepository;
import com.account_service.domain.accounts.dtos.AccountDto;
import com.account_service.domain.accounts.dtos.AccountResponse;
import com.account_service.domain.accounts.dtos.UpdateAccountDto;
import com.account_service.domain.accounts.entity.Account;
import com.account_service.domain.accounts.enums.AccountStatus;
import com.account_service.domain.accounts.kafkaDtos.AccountCreatedEvent;
import com.account_service.domain.accounts.mapper.AccountMapper;
import com.account_service.domain.accounts.repository.AccountRepository;
import com.account_service.globalException.BadRequestException;
import com.account_service.globalException.ResourceAlreadyExistsException;
import com.account_service.globalException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements  AccountService {

    private final AccountEventProducer accountEventProducer;
    private  final AccountRepository accountRepository;
    private  final AccountMapper accountMapper;
    private final AccountBalanceRepository accountBalanceRepository;
    private  final AccountLimitsRepository accountLimitsRepository;

    @Transactional
    @Override
    public AccountResponse createAccount(AccountDto accountDto) {

         try{
             boolean accountExists = accountRepository.existsByAccountNumberAndDeletedFalse(
                     accountDto.getAccountNumber());

             if (accountExists) {
                 throw new ResourceAlreadyExistsException("Account number already exists");
             }
             Account account = accountMapper.toEntity(accountDto);
             Account savedAccount = accountRepository.save(account);

             AccountBalance accountBalance = AccountBalance.builder()
                     .account(savedAccount)
                     .availableBalance(BigDecimal.ZERO)
                     .holdBalance(BigDecimal.ZERO)
                     .build();

             accountBalanceRepository.save(accountBalance);

             AccountLimit accountLimit = AccountLimit.builder()
                     .account(savedAccount)
                     .dailyLimit(new BigDecimal("50000"))
                     .monthlyLimit(new BigDecimal("500000"))
                     .perTransactionLimit(new BigDecimal("20000"))
                     .build();

             accountLimitsRepository.save(accountLimit);
              savedAccount.setBalance(accountBalance);
              savedAccount.setAccountLimit(accountLimit);

             AccountCreatedEvent event = AccountCreatedEvent.builder()
                     .accountId(savedAccount.getId())
                     .userId(savedAccount.getUserId())
                     .accountType(savedAccount.getAccountType())
                     .accountNumber(savedAccount.getAccountNumber())
                     .build();
             return  accountMapper.toResponse(savedAccount);

         } catch (DataIntegrityViolationException ex) {
             throw new RuntimeException(
                     "Database constraint violation occurred while creating account"
             );
         } catch (RuntimeException e) {
             throw new RuntimeException(e);
         }
    }

    @Override
    public AccountResponse getAccountById(String accountId) {
         try {
             Account account = accountRepository.findById(accountId).orElseThrow(
                     ()-> new ResourceNotFoundException("Account not found")
             );
             if(account.getStatus() != AccountStatus.ACTIVE){
                 throw new BadRequestException("Account is not active");
             }
             return accountMapper.toResponse(account);
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
    }

    @Override
    public AccountResponse getAccountByAccountNumber(String accountNumber) {
        try {
            Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                    ()-> new ResourceNotFoundException("Account not found")
            );
            return  accountMapper.toResponse(account);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        return List.of();
    }

    @Override
    public AccountResponse updateAccount(String accountId, UpdateAccountDto updateAccountDto) {
        return null;
    }

    @Override
    public void deleteAccount(String accountId) {
        try {
            Account account = accountRepository.findById(accountId).orElseThrow(
                    ()-> new ResourceNotFoundException("Account not found")
            );

            account.setDeleted(true);

            account.setStatus(AccountStatus.CLOSED);
            accountRepository.save(account);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
