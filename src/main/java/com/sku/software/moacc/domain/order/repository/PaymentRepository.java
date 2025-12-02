package com.sku.software.moacc.domain.order.repository;

import com.sku.software.moacc.domain.order.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    // PaymentKey (String)를 PK로 사용합니다.

    // 비관적 잠금이 적용된 findById 메서드 - 표준 메서드 오버라이드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Payment p WHERE p.paymentKey = :paymentKey")
    Optional<Payment> findById(@Param("paymentKey") String paymentKey);

    // 비관적 잠금이 적용된 findById 메서드 - 명시적 메서드명
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Payment p WHERE p.paymentKey = :paymentKey")
    Optional<Payment> findByIdWithLock(@Param("paymentKey") String paymentKey);
}