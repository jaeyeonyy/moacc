package com.sku.software.moacc.domain.cart.dto.response;

import com.sku.software.moacc.domain.cart.entity.CartItem;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public class CartItemResponse {
  private Long cartItemId;
  private Long productSkuId;
  private String productName;
  private String optionValue; // 예를 들어 "색상: 빨강, 사이즈: L"
  private int quantity;
  private BigDecimal price; // 현재 가격 (ProductSku에서 가져옴)

  public static CartItemResponse fromEntity(CartItem item) {
    // 엔티티의 데이터를 DTO로 변환하는 로직 (ProductSku 정보 포함)
    return CartItemResponse.builder()
        .cartItemId(item.getId())
        .productSkuId(item.getProductSku().getId())
        .productName(item.getProductSku().getProduct().getName()) // ProductSku를 통해 Product 정보 접근
        .quantity(item.getQuantity())
        // ... 나머지 필드 채우기
        .build();
  }
}
