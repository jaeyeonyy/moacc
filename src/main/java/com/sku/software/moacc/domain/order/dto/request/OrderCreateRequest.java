package com.sku.software.moacc.domain.order.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {

    // 상품 금액, 배송비 등이 포함된 최종 금액
    private int totalAmount;

    // 배송지 주소
    private String shippingAddress;

    // 주문 상품 상세 목록 (상품 ID, 수량)
    private List<OrderItemRequest> items;

    // 내부적으로 토스에 전달할 대표 주문명을 생성하는 것이 좋지만,
    // 간단화를 위해 요청에서 받거나 Service에서 생성하도록 할 수 있습니다.
}