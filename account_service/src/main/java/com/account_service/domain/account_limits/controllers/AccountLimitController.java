package com.account_service.domain.account_limits.controllers;


import com.account_service.domain.account_limits.dtos.AccountLimitDto;
import com.account_service.domain.account_limits.dtos.AccountLimitResponseDto;
import com.account_service.domain.account_limits.dtos.UpdateAccountLimitRequestDto;
import com.account_service.domain.account_limits.services.AccountLimitService;
import com.account_service.domain.accounts.repository.AccountRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accountLimit")
@RequiredArgsConstructor
public class AccountLimitController {

    private  final AccountLimitService accountLimitService;

    @PostMapping("/create")
    public ResponseEntity<AccountLimitResponseDto> createAccountLimit(@Valid
                                 @RequestBody AccountLimitDto accountLimitDto){
        AccountLimitResponseDto accountLimitResponseDto =
                accountLimitService.createAccountLimitTransaction(accountLimitDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(accountLimitResponseDto);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<AccountLimitResponseDto> updateAccountLimit(@Valid
                           @PathVariable String id,
                           @RequestBody UpdateAccountLimitRequestDto updateAccountLimitRequestDto){
        AccountLimitResponseDto accountLimitResponseDto =
                accountLimitService.updateAccountLimitTransaction(updateAccountLimitRequestDto,id);
        return  ResponseEntity.status(HttpStatus.OK).body(accountLimitResponseDto);
    }

    @GetMapping("/{id}/get_accountLimit")
    public ResponseEntity<AccountLimitResponseDto> getAccountLimitDetails(
            @PathVariable String id
    ){
        AccountLimitResponseDto accountLimitResponseDto =
                accountLimitService.fetchAccountLimitById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(accountLimitResponseDto);
    }

}
