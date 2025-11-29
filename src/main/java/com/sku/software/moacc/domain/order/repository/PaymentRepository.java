package com.sku.software.moacc.domain.order.repository;

import com.sku.software.moacc.domain.order.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    // PaymentKey (String)를 PK로 사용합니다.
}