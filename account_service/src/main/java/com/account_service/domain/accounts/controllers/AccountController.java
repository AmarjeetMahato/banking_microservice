package com.account_service.domain.accounts.controllers;


import com.account_service.domain.account_balance.services.AccountBalance;
import com.account_service.domain.accounts.dtos.AccountDto;
import com.account_service.domain.accounts.dtos.AccountResponse;
import com.account_service.domain.accounts.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private  final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountDto dto){
          AccountResponse account = accountService.createAccount(dto);
          return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/{accountId}/get_account")
    public ResponseEntity<AccountResponse> getAccountDetailsById(@PathVariable String accountId){
        AccountResponse account = accountService.getAccountById(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }



}
