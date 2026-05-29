package com.account_service.domain.accounts.services;

import com.account_service.domain.account_balance.entity.AccountBalance;
import com.account_service.domain.account_balance.repository.AccountBalanceRepository;
import com.account_service.domain.account_limits.entity.AccountLimit;
import com.account_service.domain.account_limits.repository.AccountLimitsRepository;
import com.account_service.domain.accounts.dtos.AccountDto;
import com.account_service.domain.accounts.dtos.AccountResponse;
import com.account_service.domain.accounts.dtos.UpdateAccountDto;
import com.account_service.domain.accounts.entity.Account;
import com.account_service.domain.accounts.enums.AccountStatus;
import com.account_service.domain.accounts.mapper.AccountMapper;
import com.account_service.domain.accounts.repository.AccountRepository;
import com.account_service.domain.kyc_details.entity.KycDetail;
import com.account_service.domain.kyc_details.enums.KycStatus;
import com.account_service.domain.kyc_details.repository.KycDetailsRepository;
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

    private  final AccountRepository accountRepository;
    private  final AccountMapper accountMapper;
    private final AccountBalanceRepository accountBalanceRepository;
    private  final AccountLimitsRepository accountLimitsRepository;
    private  final KycDetailsRepository kycDetailsRepository;

    @Transactional
    @Override
    public AccountResponse createAccount(AccountDto accountDto) {

         try{
             /*
              * Check duplicate account number
              */
             boolean accountExists = accountRepository
                     .existsByAccountNumberAndDeletedFalse(
                             accountDto.getAccountNumber()
                     );

             if (accountExists) {
                 throw new ResourceAlreadyExistsException(
                         "Account number already exists"
                 );
             }
             /*
              * Convert DTO -> Entity
              */
             Account account = accountMapper.toEntity(accountDto);
             Account savedAccount = accountRepository.save(account);

             /*
              * Create Default Balance
              */
             AccountBalance accountBalance = AccountBalance.builder()
                     .account(savedAccount)
                     .availableBalance(BigDecimal.ZERO)
                     .holdBalance(BigDecimal.ZERO)
                     .build();

             accountBalanceRepository.save(accountBalance);

             /*
              * Create Default Account Limits
              */
             AccountLimit accountLimit = AccountLimit.builder()
                     .account(savedAccount)
                     .dailyLimit(new BigDecimal("50000"))
                     .monthlyLimit(new BigDecimal("500000"))
                     .perTransactionLimit(new BigDecimal("20000"))
                     .build();

             accountLimitsRepository.save(accountLimit);

             /*
              * Create Default KYC Record
              */
             KycDetail kycDetail = KycDetail.builder()
                     .account(savedAccount)
                     .kycStatus(KycStatus.PENDING)
                     .build();

             kycDetailsRepository.save(kycDetail);

             /*
              * Attach relations manually (optional but useful)
              */

              savedAccount.setBalance(accountBalance);
              savedAccount.setAccountLimit(accountLimit);
              savedAccount.setKycDetail(kycDetail);

             return  accountMapper.toResponse(savedAccount);

         } catch (DataIntegrityViolationException ex) {
             /*
              * Database constraint violation
              */
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
