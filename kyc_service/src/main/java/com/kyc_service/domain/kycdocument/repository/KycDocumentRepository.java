package com.kyc_service.domain.kycdocument.repository;

import com.kyc_service.domain.kycdocument.entity.KycDocument;
import com.kyc_service.domain.kycdocument.enums.KycDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KycDocumentRepository extends JpaRepository<KycDocument,String> {

    boolean existsByKycProfileIdAndDocumentTypeAndDocumentNumber(
            String kycProfileId,
            KycDocumentType documentType,
            String documentNumber
    );

    List<KycDocument> findByKycProfileId(String kycProfileId);

    List<KycDocument> findByKycProfileIdAndDocumentType(
            String kycProfileId,
            KycDocumentType documentType
    );
}
