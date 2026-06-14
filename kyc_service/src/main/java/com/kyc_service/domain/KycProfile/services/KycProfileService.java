package com.kyc_service.domain.KycProfile.services;

import com.kyc_service.domain.KycProfile.dtos.CreateKycDetailsRequestDto;
import com.kyc_service.domain.KycProfile.dtos.KycDetailsResponseDto;
import com.kyc_service.domain.KycProfile.dtos.UpdateKycDetailsRequestDto;

import java.util.List;

public interface KycProfileService {

    /**
     * Create a new KYC profile.
     *
     * @param requestDto KYC profile data
     * @return Created KYC profile
     */
    KycDetailsResponseDto createKycProfile(CreateKycDetailsRequestDto requestDto);

    /**
     * Update an existing KYC profile.
     *
     * @param kycId KYC profile ID
     * @param requestDto Updated KYC profile data
     * @return Updated KYC profile
     */
    KycDetailsResponseDto updateKycProfile(String kycId, UpdateKycDetailsRequestDto requestDto);

    /**
     * Get KYC profile by ID.
     *
     * @param kycId KYC profile ID
     * @return KYC profile details
     */
    KycDetailsResponseDto getKycProfileById(String kycId);

    /**
     * Get KYC profile by User ID.
     *
     * @param userId User ID
     * @return KYC profile details
     */
    KycDetailsResponseDto getKycProfileByUserId(String userId);

    /**
     * Get KYC profile by Account ID.
     *
     * @param accountId Account ID
     * @return KYC profile details
     */
    KycDetailsResponseDto getKycProfileByAccountId(String accountId);

    /**
     * Get all KYC profiles.
     *
     * @return List of KYC profiles
     */
    List<KycDetailsResponseDto> getAllKycProfiles(int page, int size);

    /**
     * Approve KYC profile.
     *
     * @param kycId KYC profile ID
     * @param verifiedBy Admin/Officer ID
     * @return Updated KYC profile
     */
    KycDetailsResponseDto approveKyc(String kycId, String verifiedBy);

    /**
     * Reject KYC profile.
     *
     * @param kycId KYC profile ID
     * @param rejectionReason Rejection reason
     * @param verifiedBy Admin/Officer ID
     * @return Updated KYC profile
     */
    KycDetailsResponseDto rejectKyc(String kycId, String rejectionReason, String verifiedBy);

    /**
     * Mark profile for manual review.
     *
     * @param kycId KYC profile ID
     * @return Updated KYC profile
     */
    KycDetailsResponseDto markForManualReview(String kycId);

    /**
     * Activate KYC profile.
     *
     * @param kycId KYC profile ID
     * @return Updated KYC profile
     */
    KycDetailsResponseDto activateKycProfile(String kycId);

    /**
     * Deactivate KYC profile.
     *
     * @param kycId KYC profile ID
     * @return Updated KYC profile
     */
    KycDetailsResponseDto deactivateKycProfile(String kycId);

    /**
     * Soft delete KYC profile.
     *
     * @param kycId KYC profile ID
     */
    void deleteKycProfile(String kycId);
}
