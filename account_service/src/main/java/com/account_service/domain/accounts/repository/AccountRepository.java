package com.account_service.domain.accounts.repository;

import com.account_service.domain.accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account , String> {

    boolean existsByAccountNumberAndDeletedFalse(
            String accountNumber
    );

    Optional<Account> findByAccountNumber(String accountNumber);

}
