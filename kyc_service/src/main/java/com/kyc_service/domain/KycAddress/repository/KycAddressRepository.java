package com.kyc_service.domain.KycAddress.repository;

import com.kyc_service.domain.KycAddress.entity.KycAddress;
import com.kyc_service.domain.KycAddress.enums.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KycAddressRepository extends JpaRepository<KycAddress,String> {

    boolean existsByKycProfileIdAndAddressType(String kycProfileId, AddressType addressType);

    List<KycAddress> findByKycProfileId(String kycProfileId);
}
