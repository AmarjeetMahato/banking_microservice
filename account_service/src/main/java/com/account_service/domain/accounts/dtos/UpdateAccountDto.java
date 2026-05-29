package com.account_service.domain.accounts.dtos;


import com.account_service.domain.accounts.enums.AccountStatus;
import com.account_service.domain.accounts.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAccountDto {

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Account status is required")
    private AccountStatus status;

    @Size(
            min = 3,
            max = 3,
            message = "Currency must be exactly 3 characters"
    )
    private String currency;
}
