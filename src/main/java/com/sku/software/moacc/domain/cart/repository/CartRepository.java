package com.sku.software.moacc.domain.cart.repository;

import com.sku.software.moacc.domain.cart.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart, Long> {
  // 특정 사용자의 장바구니를 조회합니다. (Cart 엔티티의 user_id UNIQUE 제약 조건 활용)
  Optional<Cart> findByUserId(Long userId);
}