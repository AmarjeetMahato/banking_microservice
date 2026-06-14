package com.account_service.domain.kyc_details.services;


import com.account_service.domain.accounts.entity.Account;
import com.account_service.domain.accounts.enums.AccountStatus;
import com.account_service.domain.accounts.repository.AccountRepository;
import com.account_service.domain.kyc_details.dtos.KycDetailsDto;
import com.account_service.domain.kyc_details.dtos.ResponseKycDetails;
import com.account_service.domain.kyc_details.dtos.UpdateKycDetails;
import com.account_service.domain.kyc_details.entity.KycDetail;
import com.account_service.domain.kyc_details.mapper.KycDetailsMapper;
import com.account_service.domain.kyc_details.repository.KycDetailsRepository;
import com.account_service.globalException.BadRequestException;
import com.account_service.globalException.ResourceNotFoundException;
import com.account_service.globalException.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KycDetailsServiceImpl implements  KycDetailsService {

    private  final KycDetailsRepository kycDetailsRepository;
    private  final AccountRepository accountRepository;
    private  final KycDetailsMapper kycDetailsMapper;

    @Transactional
    @Override
    public ResponseKycDetails createKycDetails(KycDetailsDto createKycDetailDto) {

        try {
            Account account = accountRepository.findById(createKycDetailDto.getAccountId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Account not found")
            );

            if(account.getStatus() == AccountStatus.BLOCKED || account.getStatus() == AccountStatus.CLOSED){
                  throw  new BadRequestException(  "KYC cannot be created for inactive account");
            }

            /*
             * Check KYC already exists
             */
            boolean kycAlreadyExists = kycDetailsRepository.existsByAccountIdAndDeletedFalse(
                    account.getId()
            );
            if( kycAlreadyExists) { throw new BadRequestException("KYC already exists for this account");
            }

            /*
             * Check Aadhaar uniqueness
             */
            boolean aadhaarExists = kycDetailsRepository.existsByAadhaarNumberAndDeletedFalse(
                                    createKycDetailDto.getAadhaarNumber()
                            );

            if (aadhaarExists) {
                throw new BadRequestException("Aadhaar number already exists");
            }

            /*
             * Check PAN uniqueness
             */
            boolean panExists = kycDetailsRepository.existsByPanNumberAndDeletedFalse(
                                    createKycDetailDto.getPanNumber()
                            );

            if (panExists) {
                throw new BadRequestException("PAN number already exists");
            }

            KycDetail kycDetail = kycDetailsMapper.toEntity(createKycDetailDto,account);
            KycDetail savedJKycDetails = kycDetailsRepository.save(kycDetail);
            return  kycDetailsMapper.toResponse(savedJKycDetails);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    @Transactional(readOnly = true)
    public ResponseKycDetails getKycDetailsById(
            String kycId
    ) {

        try {

            /*
             * Get logged-in user
             */
            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            String loggedInUserId =
                    authentication.getName();
//
       /*
             * Fetch KYC details
             */
            KycDetail kycDetail =
                    kycDetailsRepository
                            .findByIdAndDeletedFalse(kycId)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "KYC details not found"
                                    )
                            );

            /*
             * Check associated account exists
             */
            Account account = kycDetail.getAccount();

            if (account == null) {

                throw new ResourceNotFoundException(
                        "Associated account not found"
                );
            }

            /*
             * Prevent access to deleted accounts
             */
            if (Boolean.TRUE.equals(account.getDeleted())) {

                throw new BadRequestException(
                        "Associated account is deleted"
                );
            }

            /*
             * Authorization check
             */
            if (!account.getUserId().equals(loggedInUserId)) {

                throw new UnauthorizedException(
                        "You are not authorized to access this KYC"
                );
            }

            /*
             * Return response
             */
            return kycDetailsMapper.toResponse(
                    kycDetail
            );

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Unexpected error occurred while fetching KYC details"
            );
        }
    }

@Override
@Transactional(readOnly = true)
public ResponseKycDetails getKycDetailsByAccountId(String accountId) {

    if (accountId == null || accountId.isBlank()) {
        throw new BadRequestException("Account id is required");
    }

    KycDetail kycDetails = kycDetailsRepository
            .findByAccountId(accountId)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "KYC details not found for account id : " + accountId
                    ));

    return kycDetailsMapper.toResponse(kycDetails);
}


    @Override
    @Transactional(readOnly = true)
    public List<ResponseKycDetails> getAllKycDetails() {

        return kycDetailsRepository.findAll()
                .stream()
                .map(kycDetailsMapper::toResponse)
                .toList();
    }

    @Override
    public ResponseKycDetails updateKycDetails(String kycId, UpdateKycDetails updateKycDetailDto) {
        return null;
    }

    @Override
    public void deleteKycDetails(String kycId) {

    }
}
