package com.account_service.domain.kyc_details.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KycDetailsDto {

    @NotNull(message = "Account ID is required")
    private String accountId;

    @NotBlank(message = "Aadhaar number cannot be blank")
    @Pattern(
            regexp = "^[0-9]{12}$",
            message = "Aadhaar number must be exactly 12 digits"
    )
    private String aadhaarNumber;

    @NotBlank(message = "PAN number cannot be blank")
    @Pattern(
            regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$",
            message = "Invalid PAN format. Example: ABCDE1234F"
    )
    private String panNumber;


}
