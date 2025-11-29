package com.sku.software.moacc.domain.order.repository;

import com.sku.software.moacc.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 특별한 로직 없이 JpaRepository 기본 메서드를 사용합니다.
}