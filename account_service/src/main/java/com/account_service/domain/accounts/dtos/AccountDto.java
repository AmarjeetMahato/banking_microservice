package com.account_service.domain.accounts.dtos;

import com.account_service.domain.accounts.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {

    @NotNull(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Account number cannot be blank")
    @Size(
            min = 10,
            max = 20,
            message = "Account number must be between 10 and 20 digits"
    )
    @Pattern(
            regexp = "^[0-9]+$",
            message = "Account number must contain digits only"
    )
    private String accountNumber;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotBlank(message = "Currency cannot be blank")
    @Size(
            min = 3,
            max = 3,
            message = "Currency must be exactly 3 characters"
    )
    private String currency;
}
