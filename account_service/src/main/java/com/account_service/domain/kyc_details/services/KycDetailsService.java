package com.account_service.domain.kyc_details.services;

import com.account_service.domain.kyc_details.dtos.KycDetailsDto;
import com.account_service.domain.kyc_details.dtos.ResponseKycDetails;
import com.account_service.domain.kyc_details.dtos.UpdateKycDetails;

import java.util.List;

public interface KycDetailsService {

    /**
     * Create KYC details for an account
     */
    ResponseKycDetails createKycDetails(
            KycDetailsDto createKycDetailDto
    );

    /**
     * Get KYC details by ID
     */
//    ResponseKycDetails getKycDetailsById(String kycId);

    /**
     * Get KYC details using account ID
     */
    ResponseKycDetails getKycDetailsByAccountId(
            String accountId
    );

    /**
     * Get all KYC records
     */
    List<ResponseKycDetails> getAllKycDetails();

    /**
     * Update KYC details
     */
    ResponseKycDetails updateKycDetails(
            String kycId,
            UpdateKycDetails updateKycDetailDto
    );

    /**
     * Delete KYC details (soft delete)
     */
    void deleteKycDetails(String kycId);

}
