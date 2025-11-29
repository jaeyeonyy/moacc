package com.sku.software.moacc.domain.cart.repository;


import com.sku.software.moacc.domain.cart.entity.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  // 특정 장바구니에 담긴 특정 SKU 항목을 찾습니다. (중복 방지 로직에 사용)
  Optional<CartItem> findByCartIdAndProductSkuId(Long cartId, Long productSkuId);

  // 장바구니 ID를 이용해 모든 항목을 조회합니다.
  List<CartItem> findByCartId(Long cartId);
}