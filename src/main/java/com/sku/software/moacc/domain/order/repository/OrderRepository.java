package com.sku.software.moacc.domain.order.repository;

import com.sku.software.moacc.domain.order.entity.Order;
import com.sku.software.moacc.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 특별한 로직 없이 JpaRepository 기본 메서드를 사용합니다.

    // tossOrderId로 주문을 조회하는 메서드
    Optional<Order> findByTossOrderId(String tossOrderId);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END " +
           "FROM Order o JOIN o.orderItems oi " +
           "JOIN ProductSku ps ON ps.id = oi.skuId " +
           "WHERE o.userId = :userId AND ps.product.id = :productId AND o.orderStatus = :orderStatus")
    boolean existsByUserIdAndOrderItems_ProductIdAndOrderStatus(@Param("userId") Long userId, @Param("productId") Long productId, @Param("orderStatus") OrderStatus orderStatus);

    List<Order> findFirst200ByUserIdOrderByIdDesc(Long userId);
}
