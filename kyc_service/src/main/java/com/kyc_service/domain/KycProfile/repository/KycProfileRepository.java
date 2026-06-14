package com.kyc_service.domain.KycProfile.repository;

import com.kyc_service.domain.KycProfile.entity.KycProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KycProfileRepository extends JpaRepository<KycProfile,String> {

    boolean existsByUserId(String userId);
    boolean existsByAccountId(String accountId);
    boolean existsByKycReferenceNumber(String kycReferenceNumber);
    Optional<KycProfile> findByUserId(String userId);
    Optional<KycProfile> findByAccountId(String accountId);
}
