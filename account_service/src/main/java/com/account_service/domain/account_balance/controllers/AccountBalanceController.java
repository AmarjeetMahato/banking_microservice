package com.account_service.domain.account_balance.controllers;

import com.account_service.domain.account_balance.dtos.AccountBalanceResponseDto;
import com.account_service.domain.account_balance.dtos.CreateAccountBalanceRequestDto;
import com.account_service.domain.account_balance.dtos.UpdateAccountBalanceRequestDto;
import com.account_service.domain.account_balance.services.AccountBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account_balance")
@RequiredArgsConstructor
public class AccountBalanceController {

    private  final AccountBalanceService accountBalanceService;


    @PostMapping("/create")
    public ResponseEntity<AccountBalanceResponseDto> createAccountBalance(
            @RequestBody CreateAccountBalanceRequestDto dto){

        AccountBalanceResponseDto result = accountBalanceService.createAccountBalance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{balanceId}/get_by_balanceId")
    public ResponseEntity<AccountBalanceResponseDto> getAccountBalance(
            @PathVariable String balanceId){
        AccountBalanceResponseDto result = accountBalanceService.getAccountBalanceById(balanceId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{accountId}/get_by_accountId")
    public ResponseEntity<AccountBalanceResponseDto> getAccountBalanceBYAccountId(
            @PathVariable String accountId){
        AccountBalanceResponseDto result = accountBalanceService.getAccountBalanceByAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/{balanceId}/update")
    public ResponseEntity<AccountBalanceResponseDto> updateAccountBalance(
            @PathVariable String balanceId,
            @RequestBody UpdateAccountBalanceRequestDto dto
            ){
        AccountBalanceResponseDto result = accountBalanceService.updateAccountBalance(balanceId,dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
