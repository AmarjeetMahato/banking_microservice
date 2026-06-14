package com.kyc_service.domain.KycAddress.services;

import com.kyc_service.domain.KycAddress.dtos.KycAddressCreateRequestDto;
import com.kyc_service.domain.KycAddress.dtos.KycAddressResponseDto;
import com.kyc_service.domain.KycAddress.dtos.KycAddressUpdateRequestDto;

import java.util.List;

public interface KycAddressService {

    /**
     * Create new KYC address
     */
    KycAddressResponseDto createAddress(KycAddressCreateRequestDto dto);

    /**
     * Update existing address
     */
    KycAddressResponseDto updateAddress(KycAddressUpdateRequestDto dto,String addressId);

    /**
     * Get address by ID
     */
    KycAddressResponseDto getAddressById(String id);

    /**
     * Get all addresses for a KYC profile
     */
    List<KycAddressResponseDto> getAddressesByKycProfileId(String kycProfileId);

    /**
     * Delete address by ID (soft delete recommended)
     */
    void deleteAddress(String id);

    /**
     * Verify address manually (admin/KYC officer)
     */
    KycAddressResponseDto verifyAddress(String id, String verifiedBy);

    /**
     * Activate / Deactivate address
     */
    KycAddressResponseDto toggleActive(String id, Boolean active);
}