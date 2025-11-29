package com.sku.software.moacc.domain.cart.dto.response;

import com.sku.software.moacc.domain.cart.entity.CartItem;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartItemResponse {
  private Long cartItemId;
  private Long productSkuId;
  private String productName;
  private String optionValue; // 예를 들어 "색상: 빨강, 사이즈: L"
  private int quantity;
  private BigDecimal price; // 현재 가격 (ProductSku에서 가져옴)

  public static CartItemResponse fromEntity(CartItem item) {
    // 옵션값들을 조합하여 문자열로 생성
    String optionValueString = item.getProductSku().getSkuOptionValues().stream()
        .map(skuOptionValue ->
            skuOptionValue.getCategoryOptionType().getName() + ": " +
            skuOptionValue.getOptionValue().getValue())
        .collect(java.util.stream.Collectors.joining(", "));

    // 엔티티의 데이터를 DTO로 변환하는 로직 (ProductSku 정보 포함)
    return CartItemResponse.builder()
        .cartItemId(item.getId())
        .productSkuId(item.getProductSku().getId())
        .productName(item.getProductSku().getProduct().getName())
        .quantity(item.getQuantity())
        .price(BigDecimal.valueOf(item.getProductSku().getPrice()))
        .optionValue(optionValueString)
        .build();
  }
}
