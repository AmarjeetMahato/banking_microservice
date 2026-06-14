package com.kyc_service.domain.KycAddress.dtos;

import com.kyc_service.domain.KycAddress.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KycAddressResponseDto {
    private String id;

    private String kycProfileId;

    private AddressType addressType;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String district;

    private String state;

    private String country;

    private String postalCode;

    private String landmark;

    private Boolean verified;

    private String verifiedBy;

    private LocalDateTime verifiedAt;

    private Boolean active;

}
