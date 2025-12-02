package com.sku.software.moacc.domain.order.dto.response;

import com.sku.software.moacc.domain.order.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponse {
    private Long skuId;
    private String productName;
    private Integer quantity;
    private Long price;

    public static OrderItemResponse fromEntity(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .skuId(orderItem.getSkuId())
                .productName(orderItem.getProductName())
                .quantity(orderItem.getQuantity())
                .price((long) orderItem.getPrice())
                .build();
    }
}

