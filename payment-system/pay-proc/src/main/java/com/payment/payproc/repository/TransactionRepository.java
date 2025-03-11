package com.payment.payproc.repository;

import com.payment.payproc.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<PaymentTransaction, Long> {
}