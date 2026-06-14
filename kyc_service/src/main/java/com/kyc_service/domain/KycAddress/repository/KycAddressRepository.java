package com.kyc_service.domain.KycAddress.repository;

import com.kyc_service.domain.KycAddress.entity.KycAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycAddressRepository extends JpaRepository<KycAddress,String> {
}
