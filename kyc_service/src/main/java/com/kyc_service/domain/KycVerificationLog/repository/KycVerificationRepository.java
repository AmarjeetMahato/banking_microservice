package com.kyc_service.domain.KycVerificationLog.repository;

import com.kyc_service.domain.KycProfile.enums.KycStatus;
import com.kyc_service.domain.KycVerificationLog.entity.KycVerificationLog;
import com.kyc_service.domain.KycVerificationLog.enums.VerificationAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycVerificationRepository  extends JpaRepository<KycVerificationLog,String> {

    Page<KycVerificationLog> findByKycProfile_Id(
            String kycProfileId,
            Pageable pageable
    );

    Page<KycVerificationLog> findByAction(
            VerificationAction action,
            Pageable pageable
    );

    Page<KycVerificationLog> findByNewStatus(
            KycStatus status,
            Pageable pageable
    );
}
