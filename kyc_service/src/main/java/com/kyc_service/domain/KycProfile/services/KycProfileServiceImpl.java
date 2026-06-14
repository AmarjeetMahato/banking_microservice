package com.kyc_service.domain.KycProfile.services;

import com.kyc_service.domain.KycProfile.dtos.CreateKycDetailsRequestDto;
import com.kyc_service.domain.KycProfile.dtos.KycDetailsResponseDto;
import com.kyc_service.domain.KycProfile.dtos.UpdateKycDetailsRequestDto;
import com.kyc_service.domain.KycProfile.entity.KycProfile;
import com.kyc_service.domain.KycProfile.enums.KycStatus;
import com.kyc_service.domain.KycProfile.mapper.KycProfileMapper;
import com.kyc_service.domain.KycProfile.repository.KycProfileRepository;
import com.kyc_service.globalExceptions.BadRequestException;
import com.kyc_service.globalExceptions.ResourceAlreadyExistsException;
import com.kyc_service.globalExceptions.ResourceNotFoundException;
import com.kyc_service.globalExceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KycProfileServiceImpl implements  KycProfileService {

    private final KycProfileRepository repository;
    private final KycProfileMapper mapper;

    @Override
    public KycDetailsResponseDto createKycProfile(CreateKycDetailsRequestDto requestDto) {
        try {
            // 2. Business validation: userId + accountId should not already have KYC
            boolean existsByUserId = repository.existsByUserId(requestDto.getUserId());
            if (existsByUserId) {
                throw new ResourceAlreadyExistsException(
                        "KYC profile already exists for userId: " + requestDto.getUserId()
                );
            }

            boolean existsByAccountId = repository.existsByAccountId(requestDto.getAccountId());
            if (existsByAccountId) {
                throw new ResourceAlreadyExistsException(
                        "KYC profile already exists for accountId: " + requestDto.getAccountId()
                );
            }

            // 3. Unique KYC reference validation
            boolean existsByRef = repository.existsByKycReferenceNumber(requestDto.getKycReferenceNumber());
            if (existsByRef) {
                throw new ResourceAlreadyExistsException(
                        "KYC reference number already exists: " + requestDto.getKycReferenceNumber()
                );
            }

            // 4. Additional business rule validation
            validateRiskScore(requestDto.getRiskScore());

            // 5. Map DTO -> Entity
            KycProfile entity = mapper.toEntity(requestDto);

            // 6. System-level defaults (override safe values)
            entity.setStatus(KycStatus.PENDING);
            entity.setActive(true);
            entity.setSubmittedAt(LocalDateTime.now());
            entity.setManualReviewRequired(
                    Boolean.TRUE.equals(requestDto.getManualReviewRequired())
            );

            // 7. Save to DB
            KycProfile savedEntity = repository.save(entity);

            log.info("KYC profile created successfully with id={}", savedEntity.getId());

            // 8. Map Entity -> Response
            return mapper.toResponseDto(savedEntity);
        } catch (ResourceAlreadyExistsException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateRiskScore(Integer riskScore) {

        if (riskScore == null) return;

        if (riskScore < 0 || riskScore > 100) {
            throw new IllegalArgumentException(
                    "Risk score must be between 0 and 100"
            );
        }

        // Optional business rule
        if (riskScore > 80) {
            log.warn("High risk score detected: {}", riskScore);
        }
    }

    @Override
    public KycDetailsResponseDto updateKycProfile(String kycId, UpdateKycDetailsRequestDto requestDto) {

        log.info("Updating KYC profile for kycId={}", kycId);

        // 1. Validate input
        if (kycId == null || kycId.isBlank()) {
            throw new BadRequestException("KYC ID cannot be null or empty");
        }

        if (requestDto == null) {
            throw new BadRequestException("Update request cannot be null");
        }

        // 2. Fetch existing entity
        KycProfile entity = repository.findById(kycId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "KYC profile not found for id: " + kycId
                ));

        // 3. Business rule: prevent update if already approved/rejected (optional but realistic)
        if (entity.getStatus() == KycStatus.APPROVED) {
            throw new BadRequestException("Cannot update an already APPROVED KYC profile");
        }

        if (entity.getStatus() == KycStatus.REJECTED) {
            log.warn("Updating previously rejected KYC profile id={}", kycId);
        }

        // 4. Field-level validations (only if values are provided)

        if (requestDto.getRiskScore() != null) {
            validateRiskScore(requestDto.getRiskScore());
        }

        if (requestDto.getKycReferenceNumber() != null) {
            boolean exists = repository.existsByKycReferenceNumber(requestDto.getKycReferenceNumber());
            if (exists && !requestDto.getKycReferenceNumber().equals(entity.getKycReferenceNumber())) {
                throw new ResourceAlreadyExistsException(
                        "KYC reference number already exists: " + requestDto.getKycReferenceNumber()
                );
            }
        }

        // 5. Apply partial update using mapper
        mapper.updateEntity(entity, requestDto);

        // 6. Update system fields
        entity.setLastReviewDate(LocalDateTime.now());

        // 7. Save updated entity
        KycProfile updatedEntity = repository.save(entity);

        log.info("KYC profile updated successfully for id={}", updatedEntity.getId());

        // 8. Convert to response DTO
        return mapper.toResponseDto(updatedEntity);
    }

    @Override
    public KycDetailsResponseDto getKycProfileById(String kycId) {

        log.info("Fetching KYC profile by id={}", kycId);

        // 1. Validate input
        if (kycId == null || kycId.isBlank()) {
            throw new IllegalArgumentException("KYC ID cannot be null or empty");
        }

        // 2. Fetch entity
        KycProfile entity = repository.findById(kycId)
                .orElseThrow(() -> new IllegalStateException(
                        "KYC profile not found for id: " + kycId
                ));

        // 3. Check active status (soft delete handling)
        if (!Boolean.TRUE.equals(entity.getActive())) {
            throw new IllegalStateException("KYC profile is inactive or deleted");
        }

        // 4. Optional: expired check
        if (entity.getExpiresAt() != null &&
                entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Accessing expired KYC profile id={}", kycId);
        }

        // 5. Return response
        return mapper.toResponseDto(entity);
    }

    @Override
    public KycDetailsResponseDto getKycProfileByUserId(String userId) {

        log.info("Fetching KYC profile by userId={}", userId);

        // 1. Validate input
        if (userId == null || userId.isBlank()) {
            throw new BadRequestException("User ID cannot be null or empty");
        }

        // 2. Fetch profile
        KycProfile entity = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "KYC profile not found for userId: " + userId
                ));

        // 3. Soft delete / inactive check
        if (!Boolean.TRUE.equals(entity.getActive())) {
            throw new BadRequestException("KYC profile is inactive or deleted for userId: " + userId);
        }

        // 4. Optional business warning (data integrity check)
        if (entity.getUserId() == null) {
            throw new UnauthorizedException("Data corruption detected: missing userId in KYC record");
        }

        // 5. Return response
        return mapper.toResponseDto(entity);
    }

    @Override
    public KycDetailsResponseDto getKycProfileByAccountId(String accountId) {

        log.info("Fetching KYC profile by accountId={}", accountId);

        // 1. Validate input
        if (accountId == null || accountId.isBlank()) {
            throw new BadRequestException("Account ID cannot be null or empty");
        }

        // 2. Fetch profile
        KycProfile entity = repository.findByAccountId(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "KYC profile not found for accountId: " + accountId
                ));

        // 3. Active check
        if (!Boolean.TRUE.equals(entity.getActive())) {
            throw new BadRequestException("KYC profile is inactive or deleted for accountId: " + accountId);
        }

        // 4. Optional integrity check
        if (entity.getAccountId() == null) {
            throw new BadRequestException("Data corruption detected: missing accountId in KYC record");
        }

        // 5. Return response
        return mapper.toResponseDto(entity);
    }


    public List<KycDetailsResponseDto> getAllKycProfiles(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<KycProfile> result = repository.findAll(pageable);

        return result.getContent()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }
    @Override
    public KycDetailsResponseDto approveKyc(String kycId, String verifiedBy) {

        log.info("Approving KYC id={} by user={}", kycId, verifiedBy);

        // 1. Validate input
        if (kycId == null || kycId.isBlank()) {
            throw new BadRequestException("KYC ID cannot be null or empty");
        }

        if (verifiedBy == null || verifiedBy.isBlank()) {
            throw new BadRequestException("VerifiedBy is required for approval");
        }

        // 2. Fetch entity
        KycProfile entity = repository.findById(kycId)
                .orElseThrow(() -> new ResourceNotFoundException("KYC not found: " + kycId));

        // 3. Business rule: already approved
        if (entity.getStatus() == KycStatus.APPROVED) {
            log.warn("KYC already approved id={}", kycId);
            return mapper.toResponseDto(entity);
        }

        // 4. Business rule: cannot approve inactive
        if (!Boolean.TRUE.equals(entity.getActive())) {
            throw new BadRequestException("Cannot approve inactive KYC profile");
        }

        // 5. Update state
        entity.setStatus(KycStatus.APPROVED);
        entity.setVerifiedBy(verifiedBy);
        entity.setApprovedAt(LocalDateTime.now());
        entity.setRejectedAt(null);
        entity.setRejectionReason(null);
        entity.setLastReviewDate(LocalDateTime.now());

        // 6. Save
        KycProfile updated = repository.save(entity);

        log.info("KYC approved successfully id={}", kycId);

        return mapper.toResponseDto(updated);
    }

    @Override
    public KycDetailsResponseDto rejectKyc(String kycId, String rejectionReason, String verifiedBy) {

        log.info("Rejecting KYC id={} by user={}", kycId, verifiedBy);

        // 1. Validate input
        if (kycId == null || kycId.isBlank()) {
            throw new BadRequestException("KYC ID cannot be null or empty");
        }

        if (rejectionReason == null || rejectionReason.isBlank()) {
            throw new BadRequestException("Rejection reason is required");
        }

        if (verifiedBy == null || verifiedBy.isBlank()) {
            throw new BadRequestException("VerifiedBy is required for rejection");
        }

        // 2. Fetch entity
        KycProfile entity = repository.findById(kycId)
                .orElseThrow(() -> new ResourceNotFoundException("KYC not found: " + kycId));

        // 3. Business rule: already approved cannot be rejected (optional strict rule)
        if (entity.getStatus() == KycStatus.APPROVED) {
            throw new BadRequestException("Cannot reject an already APPROVED KYC");
        }

        // 4. Update state
        entity.setStatus(KycStatus.REJECTED);
        entity.setRejectionReason(rejectionReason);
        entity.setVerifiedBy(verifiedBy);
        entity.setRejectedAt(LocalDateTime.now());
        entity.setApprovedAt(null);
        entity.setLastReviewDate(LocalDateTime.now());

        // 5. Save
        KycProfile updated = repository.save(entity);

        log.info("KYC rejected successfully id={}", kycId);

        return mapper.toResponseDto(updated);
    }

    @Override
    public KycDetailsResponseDto markForManualReview(String kycId) {

        log.info("Marking KYC for manual review id={}", kycId);

        // 1. Validate input
        if (kycId == null || kycId.isBlank()) {
            throw new BadRequestException("KYC ID cannot be null or empty");
        }

        // 2. Fetch entity
        KycProfile entity = repository.findById(kycId)
                .orElseThrow(() -> new ResourceNotFoundException("KYC not found: " + kycId));

        // 3. Idempotency check
        if (Boolean.TRUE.equals(entity.getManualReviewRequired())) {
            log.warn("KYC already marked for manual review id={}", kycId);
            return mapper.toResponseDto(entity);
        }

        // 4. Update state
        entity.setManualReviewRequired(true);
        entity.setStatus(KycStatus.UNDER_REVIEW);
        entity.setLastReviewDate(LocalDateTime.now());

        // 5. Save
        KycProfile updated = repository.save(entity);

        log.info("KYC marked for manual review id={}", kycId);

        return mapper.toResponseDto(updated);
    }

    @Override
    public KycDetailsResponseDto activateKycProfile(String kycId) {

        log.info("Activating KYC profile id={}", kycId);

        // 1. Validate input
        if (kycId == null || kycId.isBlank()) {
            throw new BadRequestException("KYC ID cannot be null or empty");
        }

        // 2. Fetch entity
        KycProfile entity = repository.findById(kycId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "KYC profile not found for id: " + kycId
                ));

        // 3. Idempotency check
        if (Boolean.TRUE.equals(entity.getActive())) {
            log.warn("KYC profile already active id={}", kycId);
            return mapper.toResponseDto(entity);
        }

        // 4. Business update
        entity.setActive(true);
        entity.setLastReviewDate(LocalDateTime.now());

        // Optional business rule: if reactivated after rejection
        if (entity.getStatus() == KycStatus.REJECTED) {
            entity.setStatus(KycStatus.PENDING);
            entity.setRejectionReason(null);
        }

        // 5. Save
        KycProfile updated = repository.save(entity);

        log.info("KYC profile activated successfully id={}", kycId);

        return mapper.toResponseDto(updated);
    }


    @Override
    public KycDetailsResponseDto deactivateKycProfile(String kycId) {

        log.info("Deactivating KYC profile id={}", kycId);

        // 1. Validate input
        if (kycId == null || kycId.isBlank()) {
            throw new BadRequestException("KYC ID cannot be null or empty");
        }

        // 2. Fetch entity
        KycProfile entity = repository.findById(kycId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "KYC profile not found for id: " + kycId
                ));

        // 3. Idempotency check
        if (Boolean.FALSE.equals(entity.getActive())) {
            log.warn("KYC profile already inactive id={}", kycId);
            return mapper.toResponseDto(entity);
        }

        // 4. Business rule (optional but realistic)
        if (entity.getStatus() == KycStatus.APPROVED) {
            log.warn("Deactivating approved KYC profile id={}", kycId);
            // you can either block or allow depending on compliance rules
        }

        // 5. Update state
        entity.setActive(false);
        entity.setLastReviewDate(LocalDateTime.now());

        // Optional audit trace
        entity.setNextReviewDate(LocalDateTime.now().plusMonths(6));

        // 6. Save
        KycProfile updated = repository.save(entity);

        log.info("KYC profile deactivated successfully id={}", kycId);

        return mapper.toResponseDto(updated);
    }


    @Override
    public void deleteKycProfile(String kycId) {

        log.info("Deleting KYC profile id={}", kycId);

        // 1. Validate input
        if (kycId == null || kycId.isBlank()) {
            throw new IllegalArgumentException("KYC ID cannot be null or empty");
        }

        // 2. Fetch entity
        KycProfile entity = repository.findById(kycId)
                .orElseThrow(() -> new IllegalStateException(
                        "KYC profile not found: " + kycId
                ));

        // 3. Idempotency check
        if (!Boolean.TRUE.equals(entity.getActive())) {
            log.warn("KYC already deleted/inactive id={}", kycId);
            return;
        }

        // 4. Soft delete (IMPORTANT)
        entity.setActive(false);
        entity.setStatus(KycStatus.REJECTED); // or ARCHIVED if you have it
        entity.setLastReviewDate(LocalDateTime.now());

        repository.save(entity);

        log.info("KYC profile soft deleted successfully id={}", kycId);
    }
}
