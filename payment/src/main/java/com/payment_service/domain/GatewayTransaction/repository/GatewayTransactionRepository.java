package com.payment_service.domain.GatewayTransaction.repository;

import com.payment_service.domain.GatewayTransaction.entity.GatewayTransaction;
import com.payment_service.domain.Payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatewayTransactionRepository extends JpaRepository<GatewayTransaction,String> {

    Optional<Payment> findByIdAndDeletedFalse(String id);

}
