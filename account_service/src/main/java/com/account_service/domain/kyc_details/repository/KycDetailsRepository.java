package com.account_service.domain.kyc_details.repository;


import com.account_service.domain.accounts.entity.Account;
import com.account_service.domain.kyc_details.entity.KycDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KycDetailsRepository extends JpaRepository<KycDetail,String> {

    boolean existsByAccountIdAndDeletedFalse(
            String accountId
    );

    boolean existsByAadhaarNumberAndDeletedFalse(
            String aadhaarNumber
    );

    boolean existsByPanNumberAndDeletedFalse(
            String panNumber
    );

    Optional<Account> findByIdAndDeletedFalse(
            String id
    );
}
