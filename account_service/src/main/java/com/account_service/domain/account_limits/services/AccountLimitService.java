package com.account_service.domain.account_limits.services;

import com.account_service.domain.account_limits.dtos.AccountLimitDto;
import com.account_service.domain.account_limits.dtos.AccountLimitResponseDto;
import com.account_service.domain.account_limits.dtos.UpdateAccountLimitRequestDto;

public interface AccountLimitService {

      AccountLimitResponseDto createAccountLimitTransaction(AccountLimitDto accountLimitDto);

      AccountLimitResponseDto updateAccountLimitTransaction(
              UpdateAccountLimitRequestDto accountLimitRequestDto,String accountLimitId);

      AccountLimitResponseDto fetchAccountLimitById(String accountLimitId);
}
