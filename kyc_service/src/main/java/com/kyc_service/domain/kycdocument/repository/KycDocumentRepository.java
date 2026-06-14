package com.kyc_service.domain.kycdocument.repository;

import com.kyc_service.domain.kycdocument.entity.KycDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycDocumentRepository extends JpaRepository<KycDocument,String> {
}
