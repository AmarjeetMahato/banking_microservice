package com.kyc_service.domain.KycVerificationLog.services;

import com.kyc_service.domain.KycVerificationLog.dtos.CreateKycVerificationLogDto;
import com.kyc_service.domain.KycVerificationLog.dtos.KycVerificationLogResponseDto;
import com.kyc_service.domain.KycVerificationLog.dtos.UpdateKycVerificationLogDto;
import org.springframework.data.domain.Page;

public interface KycVerificationService {
    KycVerificationLogResponseDto createVerificationLog(CreateKycVerificationLogDto request);

    KycVerificationLogResponseDto updateVerificationLog(
            String id,
            UpdateKycVerificationLogDto request
    );

    KycVerificationLogResponseDto getVerificationLogById(String id);

    Page<KycVerificationLogResponseDto> getAllVerificationLogs(
            int page,
            int size
    );

    Page<KycVerificationLogResponseDto> getVerificationLogsByKycProfile(
            String kycProfileId,
            int page,
            int size
    );

    Page<KycVerificationLogResponseDto> getVerificationLogsByAction(
            String action,
            int page,
            int size
    );

    Page<KycVerificationLogResponseDto> getVerificationLogsByStatus(
            String status,
            int page,
            int size
    );

    void deleteVerificationLog(String id);

    void activateVerificationLog(String id);

    void deactivateVerificationLog(String id);

}
