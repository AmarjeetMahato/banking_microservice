package com.payment_service.domain.Payment.repository;

import com.payment_service.domain.Payment.entity.Payment;
import com.payment_service.domain.Payment.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository  extends JpaRepository<Payment,String> {
    Optional<Payment> findByPaymentReference(String paymentReference);

    Optional<Payment> findByIdAndDeletedFalse(String id);

    Optional<Payment> findByPaymentReferenceAndDeletedFalse(
            String paymentReference
    );

    Page<Payment> findByStatus(
            PaymentStatus status,
            Pageable pageable
    );

    Page<Payment> findBySourceAccountIdOrDestinationAccountId(
            String sourceAccountId,
            String destinationAccountId,
            Pageable pageable
    );

    Page<Payment> findByInitiatedBy(
            String initiatedBy,
            Pageable pageable
    );

    boolean existsByPaymentReference(
            String paymentReference
    );
}
