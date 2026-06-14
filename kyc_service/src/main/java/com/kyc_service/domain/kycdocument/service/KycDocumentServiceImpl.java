package com.kyc_service.domain.kycdocument.service;

import com.kyc_service.domain.KycProfile.entity.KycProfile;
import com.kyc_service.domain.KycProfile.repository.KycProfileRepository;
import com.kyc_service.domain.kycdocument.dtos.KycDocumentCreateRequestDto;
import com.kyc_service.domain.kycdocument.dtos.KycDocumentResponseDto;
import com.kyc_service.domain.kycdocument.dtos.KycDocumentUpdateRequestDto;
import com.kyc_service.domain.kycdocument.entity.KycDocument;
import com.kyc_service.domain.kycdocument.enums.KycDocumentType;
import com.kyc_service.domain.kycdocument.enums.KycVerificationStatus;
import com.kyc_service.domain.kycdocument.mapper.KycDocumentMapper;
import com.kyc_service.domain.kycdocument.repository.KycDocumentRepository;
import com.kyc_service.globalExceptions.BadRequestException;
import com.kyc_service.globalExceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KycDocumentServiceImpl implements  KycDocumentService {

    private  final KycDocumentRepository kycDocumentRepository;
    private  final KycDocumentMapper kycDocumentMapper;
    private  final KycProfileRepository kycProfileRepository;

    @Override
    public KycDocumentResponseDto createDocument(KycDocumentCreateRequestDto dto) {

        // 2. Validate KYC Profile
        KycProfile profile = kycProfileRepository.findById(dto.getKycProfileId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "KYC Profile not found: " + dto.getKycProfileId()
                        )
                );

        // 3. Aadhaar validation (ONLY if Aadhaar)
        if (dto.getDocumentType() == KycDocumentType.AADHAAR) {

            if (!AadhaarValidator.isValidAadhaar(dto.getDocumentNumber())) {
                throw new BadRequestException("Invalid Aadhaar number format");
            }
        }

        // 4. Duplicate check
        boolean exists = kycDocumentRepository
                .existsByKycProfileIdAndDocumentTypeAndDocumentNumber(
                        dto.getKycProfileId(),
                        dto.getDocumentType(),
                        dto.getDocumentNumber()
                );

        if (exists) {
            throw new BadRequestException(
                    "Document already exists for this profile"
            );
        }

        // 5. Map DTO -> Entity
        KycDocument entity = kycDocumentMapper.toEntity(dto, profile);

        // 6. Default KYC status
        entity.setStatus(KycVerificationStatus.PENDING);
        entity.setOcrMatched(false);

        // 7. Save
        KycDocument saved = kycDocumentRepository.save(entity);

        // 8. Optional: External KYC API call (commented)
    /*
    kycVerificationClient.verify(dto.getDocumentNumber(), dto.getDocumentType());
    */

        // 9. Return response
        return kycDocumentMapper.toResponseDto(saved);
    }

    @Override
    public KycDocumentResponseDto updateDocument(KycDocumentUpdateRequestDto dto,String docId) {

        // 1. Null check
        if (dto == null) {
            throw new BadRequestException("Request cannot be null");
        }

        // 2. Fetch existing document
        KycDocument document = kycDocumentRepository.findById(docId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "KYC Document not found with id: " + docId
                        )
                );

        // 3. Business rule: Do not allow update if already VERIFIED (optional but real-world correct)
        if (document.getStatus() == KycVerificationStatus.VERIFIED) {
            throw new BadRequestException("Verified documents cannot be updated");
        }

        // 4. Update fields safely (PATCH style via mapper)
        kycDocumentMapper.updateEntity(dto, document);

        // 5. Fraud / validation rules (important layer)
        if (document.getConfidenceScore() != null &&
                document.getConfidenceScore() < 0.6) {
            throw new BadRequestException("Document rejected due to low OCR confidence");
        }

        if (Boolean.FALSE.equals(document.getOcrMatched())) {
            throw new BadRequestException("OCR mismatch detected, cannot update document");
        }

        // 6. If expiry check is provided
        if (document.getExpiryDate() != null &&
                document.getExpiryDate().isBefore(java.time.LocalDate.now())) {
            throw new BadRequestException("Document is expired and cannot be updated");
        }

        // 7. Save updated entity
        KycDocument updated = kycDocumentRepository.save(document);

        // 8. Return response DTO
        return kycDocumentMapper.toResponseDto(updated);
    }



    @Override
    public KycDocumentResponseDto getDocumentById(String id) {

        if (id == null || id.isBlank()) {
            throw new BadRequestException("Document ID is required");
        }

        KycDocument document = kycDocumentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "KYC Document not found with id: " + id
                        )
                );

        return kycDocumentMapper.toResponseDto(document);
    }


    @Override
    public List<KycDocumentResponseDto> getDocumentsByKycProfileId(String kycProfileId) {

        if (kycProfileId == null || kycProfileId.isBlank()) {
            throw new BadRequestException("KYC Profile ID is required");
        }

        return kycDocumentRepository.findByKycProfileId(kycProfileId)
                .stream()
                .map(kycDocumentMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<KycDocumentResponseDto> getDocumentsByType(
            String kycProfileId,
            KycDocumentType type
    ) {

        if (kycProfileId == null || kycProfileId.isBlank()) {
            throw new BadRequestException("KYC Profile ID is required");
        }

        if (type == null) {
            throw new BadRequestException("Document type is required");
        }

        return kycDocumentRepository
                .findByKycProfileIdAndDocumentType(kycProfileId, type)
                .stream()
                .map(kycDocumentMapper::toResponseDto)
                .toList();
    }

    @Override
    public KycDocumentResponseDto verifyDocument(
            String id,
            String verifiedBy,
            String referenceId
    ) {

        if (id == null || id.isBlank()) {
            throw new BadRequestException("Document ID is required");
        }

        if (verifiedBy == null || verifiedBy.isBlank()) {
            throw new BadRequestException("Verified By is required");
        }

        KycDocument document = kycDocumentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "KYC Document not found with id: " + id
                        )
                );

        document.setStatus(KycVerificationStatus.VERIFIED);
        document.setVerifiedBy(verifiedBy);
        document.setVerificationReferenceId(referenceId);
        document.setVerifiedAt(LocalDateTime.now());

        KycDocument saved = kycDocumentRepository.save(document);

        return kycDocumentMapper.toResponseDto(saved);
    }


    @Override
    public KycDocumentResponseDto rejectDocument(
            String id,
            String verifiedBy,
            String remarks
    ) {

        if (id == null || id.isBlank()) {
            throw new BadRequestException("Document ID is required");
        }

        if (verifiedBy == null || verifiedBy.isBlank()) {
            throw new BadRequestException("Verified By is required");
        }

        if (remarks == null || remarks.isBlank()) {
            throw new BadRequestException(
                    "Remarks are required when rejecting a document"
            );
        }

        KycDocument document = kycDocumentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "KYC Document not found with id: " + id
                        )
                );

        document.setStatus(KycVerificationStatus.REJECTED);
        document.setVerifiedBy(verifiedBy);
        document.setVerifiedAt(LocalDateTime.now());
        document.setRemarks(remarks);

        KycDocument saved = kycDocumentRepository.save(document);

        return kycDocumentMapper.toResponseDto(saved);
    }



    @Override
    public KycDocumentResponseDto updateStatus(
            String id,
            KycVerificationStatus status,
            String updatedBy
    ) {

        if (id == null || id.isBlank()) {
            throw new BadRequestException("Document ID is required");
        }

        if (status == null) {
            throw new BadRequestException("Status is required");
        }

        KycDocument document = kycDocumentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "KYC Document not found with id: " + id
                        )
                );

        document.setStatus(status);

        if (status == KycVerificationStatus.VERIFIED) {
            document.setVerifiedBy(updatedBy);
            document.setVerifiedAt(LocalDateTime.now());
        }

        KycDocument saved = kycDocumentRepository.save(document);

        return kycDocumentMapper.toResponseDto(saved);
    }

    @Override
    public KycDocumentResponseDto toggleActive(
            String id,
            Boolean active
    ) {

        if (id == null || id.isBlank()) {
            throw new BadRequestException("Document ID is required");
        }

        if (active == null) {
            throw new BadRequestException("Active status is required");
        }

        KycDocument document = kycDocumentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "KYC Document not found with id: " + id
                        )
                );

        document.setActive(active);

        KycDocument saved = kycDocumentRepository.save(document);

        return kycDocumentMapper.toResponseDto(saved);
    }

    @Override
    public void deleteDocument(String id) {

        if (id == null || id.isBlank()) {
            throw new BadRequestException("Document ID is required");
        }

        KycDocument document = kycDocumentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "KYC Document not found with id: " + id
                        )
                );

        kycDocumentRepository.delete(document);
    }
}
