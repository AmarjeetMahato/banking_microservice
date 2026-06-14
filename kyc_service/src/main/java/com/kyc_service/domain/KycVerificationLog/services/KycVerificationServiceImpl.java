package com.kyc_service.domain.KycVerificationLog.services;

import com.kyc_service.domain.KycProfile.entity.KycProfile;
import com.kyc_service.domain.KycProfile.enums.KycStatus;
import com.kyc_service.domain.KycProfile.repository.KycProfileRepository;
import com.kyc_service.domain.KycVerificationLog.dtos.CreateKycVerificationLogDto;
import com.kyc_service.domain.KycVerificationLog.dtos.KycVerificationLogResponseDto;
import com.kyc_service.domain.KycVerificationLog.dtos.UpdateKycVerificationLogDto;
import com.kyc_service.domain.KycVerificationLog.entity.KycVerificationLog;
import com.kyc_service.domain.KycVerificationLog.enums.VerificationAction;
import com.kyc_service.domain.KycVerificationLog.mapper.KycVerificationMapper;
import com.kyc_service.domain.KycVerificationLog.repository.KycVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KycVerificationServiceImpl implements KycVerificationService {

    private  final KycVerificationRepository kycVerificationRepository;
    private  final KycVerificationMapper verificationMapper;
    private final KycProfileRepository kycProfileRepository;



    @Override
    public KycVerificationLogResponseDto createVerificationLog(CreateKycVerificationLogDto request) {
        KycProfile profile = kycProfileRepository
                .findById(request.getKycProfileId())
                .orElseThrow(() ->
                        new RuntimeException("KYC profile not found"));

        KycVerificationLog entity =
                verificationMapper.toEntity(request, profile);

        return verificationMapper.toResponseDto(
                kycVerificationRepository.save(entity)
        );
    }

    @Override
    public KycVerificationLogResponseDto updateVerificationLog(String id, UpdateKycVerificationLogDto request) {
        KycVerificationLog entity = kycVerificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Verification log not found"));

        verificationMapper.updateEntity(entity, request);

        return verificationMapper.toResponseDto(entity);
    }

    @Override
    public KycVerificationLogResponseDto getVerificationLogById(String id) {
        return verificationMapper.toResponseDto(
                kycVerificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Verification log not found"))
        );
    }

    @Override
    public Page<KycVerificationLogResponseDto> getAllVerificationLogs(int page, int size) {
        Pageable pageable =
                PageRequest.of(page, size,
                        Sort.by("createdAt").descending());

        return kycVerificationRepository.findAll(pageable)
                .map(verificationMapper::toResponseDto);
    }

    @Override
    public Page<KycVerificationLogResponseDto> getVerificationLogsByKycProfile(String kycProfileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return kycVerificationRepository.findByKycProfile_Id(
                        kycProfileId,
                        pageable
                )
                .map(verificationMapper::toResponseDto);
    }

    @Override
    public Page<KycVerificationLogResponseDto> getVerificationLogsByAction(String action, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return kycVerificationRepository.findByAction(
                        VerificationAction.valueOf(action),
                        pageable
                )
                .map(verificationMapper::toResponseDto);
    }

    @Override
    public Page<KycVerificationLogResponseDto> getVerificationLogsByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return kycVerificationRepository.findByNewStatus(
                        KycStatus.valueOf(status),
                        pageable
                )
                .map(verificationMapper::toResponseDto);
    }

    @Override
    public void deleteVerificationLog(String id) {
        KycVerificationLog entity = kycVerificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Verification log not found"));

        entity.setDeleted(true);
        entity.setIsActive(false);

        kycVerificationRepository.save(entity);
    }

    @Override
    public void activateVerificationLog(String id) {
        KycVerificationLog entity = kycVerificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Verification log not found"));

        entity.setIsActive(true);

        kycVerificationRepository.save(entity);
    }

    @Override
    public void deactivateVerificationLog(String id) {

        KycVerificationLog entity = kycVerificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Verification log not found"));

        entity.setIsActive(false);

        kycVerificationRepository.save(entity);
    }
}
