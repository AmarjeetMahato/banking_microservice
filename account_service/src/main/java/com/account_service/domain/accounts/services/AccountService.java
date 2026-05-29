package com.account_service.domain.accounts.services;


import com.account_service.domain.accounts.dtos.AccountDto;
import com.account_service.domain.accounts.dtos.AccountResponse;
import com.account_service.domain.accounts.dtos.UpdateAccountDto;

import java.util.List;

public interface AccountService {

    /**
     * Create new bank account
     */
    AccountResponse createAccount(AccountDto accountDto);

    /**
     * Get account by ID
     */
    AccountResponse getAccountById(String accountId);

    /**
     * Get account by account number
     */
    AccountResponse getAccountByAccountNumber(String accountNumber);

    /**
     * Get all accounts
     */
    List<AccountResponse> getAllAccounts();

    /**
     * Update account details
     */
    AccountResponse updateAccount(
            String accountId,
            UpdateAccountDto updateAccountDto
    );

    /**
     * Delete/Deactivate account
     */
    void deleteAccount(String accountId);
}