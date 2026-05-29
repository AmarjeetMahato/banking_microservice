package com.account_service.domain.kyc_details.dtos;

import com.account_service.domain.kyc_details.enums.KycStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseKycDetails {

    private String id;

    private String accountId;

    private String aadhaarNumber;

    private String panNumber;

    private KycStatus kycStatus;

    private LocalDateTime verifiedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
