package com.payment_service.domain.GatewayTransaction.repository;

import com.payment_service.domain.GatewayTransaction.entity.GatewayTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayTransactionRepository extends JpaRepository<GatewayTransaction,String> {
}
