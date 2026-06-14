package com.kyc_service.domain.KycAddress.dtos;

import com.kyc_service.domain.KycAddress.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycAddressCreateRequestDto {


    @NotNull(message = "KYC Profile ID is required")
    private String kycProfileId;

    @NotNull(message = "Address type is required")
    private AddressType addressType;

    @NotBlank(message = "Address Line 1 cannot be empty")
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    private String landmark;

}
