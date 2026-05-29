package com.account_service.domain.account_limits.repository;

import com.account_service.domain.account_limits.entity.AccountLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountLimitsRepository extends JpaRepository<AccountLimit,String> {
}
