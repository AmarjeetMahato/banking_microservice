package com.account_service.domain.account_balance.repository;

import com.account_service.domain.account_balance.entity.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountBalanceRepository extends JpaRepository<AccountBalance,String> {

    Optional<AccountBalance> findByIdAndDeletedFalse(String id);
    Optional<AccountBalance> findByAccountIdAndDeletedFalse(String accountId);
    boolean existsByAccountId(String accountId);
}
