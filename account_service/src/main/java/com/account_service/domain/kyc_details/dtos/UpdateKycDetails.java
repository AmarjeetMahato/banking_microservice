package com.account_service.domain.kyc_details.dtos;


import com.account_service.domain.kyc_details.enums.KycStatus;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateKycDetails {

    @Pattern(
            regexp = "^[0-9]{12}$",
            message = "Aadhaar number must be exactly 12 digits"
    )
    private String aadhaarNumber;

    @Pattern(
            regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$",
            message = "Invalid PAN format. Example: ABCDE1234F"
    )
    private String panNumber;

    private KycStatus kycStatus;
}
