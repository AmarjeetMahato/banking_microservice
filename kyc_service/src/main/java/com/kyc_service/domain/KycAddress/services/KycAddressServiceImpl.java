package com.kyc_service.domain.KycAddress.services;

import com.kyc_service.domain.KycAddress.dtos.KycAddressCreateRequestDto;
import com.kyc_service.domain.KycAddress.dtos.KycAddressResponseDto;
import com.kyc_service.domain.KycAddress.dtos.KycAddressUpdateRequestDto;
import com.kyc_service.domain.KycAddress.entity.KycAddress;
import com.kyc_service.domain.KycAddress.mapper.KycAddressMapper;
import com.kyc_service.domain.KycAddress.repository.KycAddressRepository;
import com.kyc_service.domain.KycProfile.entity.KycProfile;
import com.kyc_service.domain.KycProfile.repository.KycProfileRepository;
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
public class KycAddressServiceImpl implements  KycAddressService {

    private final KycAddressRepository kycAddressRepository;
    private  final KycAddressMapper kycAddressMapper;
    private  final KycProfileRepository kycProfileRepository;


    @Override
    public KycAddressResponseDto createAddress(KycAddressCreateRequestDto dto) {


        // 2. Validate KYC Profile existence
        KycProfile kycProfile = kycProfileRepository.findById(dto.getKycProfileId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "KYC Profile not found with id: " + dto.getKycProfileId()
                        )
                );

        // 3. Business validation (optional but recommended)
        boolean exists = kycAddressRepository
                .existsByKycProfileIdAndAddressType(
                        dto.getKycProfileId(),
                        dto.getAddressType()
                );

        if (exists) {
            throw new BadRequestException(
                    "Address of type " + dto.getAddressType() + " already exists for this KYC profile"
            );
        }

        // 4. Map DTO -> Entity
        KycAddress entity = kycAddressMapper.toEntity(dto, kycProfile);

        // 5. Save entity
        KycAddress saved = kycAddressRepository.save(entity);

        // 6. Convert to response DTO
        return kycAddressMapper.toResponseDto(saved);
    }

    @Override
    public KycAddressResponseDto updateAddress(KycAddressUpdateRequestDto dto,String addressId) {

        // 2. Fetch existing address
        KycAddress existingAddress = kycAddressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "KYC Address not found with id: " + addressId
                        )
                );

        // 3. Optional: Validate KYC Profile still exists (extra safety)
        if (existingAddress.getKycProfile() == null) {
            throw new BadRequestException("KYC Profile mapping is missing for this address");
        }

        // 4. Business rule: prevent duplicate address type for same profile (only if type is changing)
        if (dto.getAddressType() != null &&
                !dto.getAddressType().equals(existingAddress.getAddressType())) {

            boolean exists = kycAddressRepository
                    .existsByKycProfileIdAndAddressType(
                            existingAddress.getKycProfile().getId(),
                            dto.getAddressType()
                    );

            if (exists) {
                throw new BadRequestException(
                        "Address of type " + dto.getAddressType() +
                                " already exists for this KYC profile"
                );
            }
        }

        // 5. Apply PATCH-style updates via mapper
        kycAddressMapper.updateEntity(dto, existingAddress);

        // 6. Save updated entity
        KycAddress updated = kycAddressRepository.save(existingAddress);

        // 7. Convert to response DTO
        return kycAddressMapper.toResponseDto(updated);
    }

    @Override
    public KycAddressResponseDto getAddressById(String id) {

        if (id == null) {
            throw new BadRequestException("Address ID cannot be null");
        }

        KycAddress address = kycAddressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("KYC Address not found with id: " + id)
                );

        return kycAddressMapper.toResponseDto(address);
    }

    @Override
    public List<KycAddressResponseDto> getAddressesByKycProfileId(String kycProfileId) {

        if (kycProfileId == null || kycProfileId.isBlank()) {
            throw new BadRequestException("KYC Profile ID cannot be empty");
        }

        List<KycAddress> addresses =
                kycAddressRepository.findByKycProfileId(kycProfileId);

        return addresses.stream()
                .map(kycAddressMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteAddress(String id) {

        if (id == null) {
            throw new BadRequestException("Address ID cannot be null");
        }

        KycAddress address = kycAddressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("KYC Address not found with id: " + id)
                );

        kycAddressRepository.delete(address);
    }


    @Override
    public KycAddressResponseDto verifyAddress(String id, String verifiedBy) {

        if (id == null) {
            throw new BadRequestException("Address ID cannot be null");
        }

        if (verifiedBy == null || verifiedBy.isBlank()) {
            throw new BadRequestException("VerifiedBy cannot be empty");
        }

        KycAddress address = kycAddressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("KYC Address not found with id: " + id)
                );

        address.setVerified(true);
        address.setVerifiedBy(verifiedBy);
        address.setVerifiedAt(LocalDateTime.now());

        KycAddress saved = kycAddressRepository.save(address);

        return kycAddressMapper.toResponseDto(saved);
    }


    @Override
    public KycAddressResponseDto toggleActive(String id, Boolean active) {

        if (id == null) {
            throw new BadRequestException("Address ID cannot be null");
        }

        if (active == null) {
            throw new BadRequestException("Active status cannot be null");
        }

        KycAddress address = kycAddressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("KYC Address not found with id: " + id)
                );

        address.setActive(active);

        KycAddress saved = kycAddressRepository.save(address);

        return kycAddressMapper.toResponseDto(saved);
    }
}
