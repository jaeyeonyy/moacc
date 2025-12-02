package com.sku.software.moacc.domain.order.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    private Long skuId;
    private int quantity;
    private int price; // 개당 가격
    private String productName;
}