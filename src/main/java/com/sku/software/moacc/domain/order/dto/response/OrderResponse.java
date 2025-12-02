package com.sku.software.moacc.domain.order.dto.response;

import com.sku.software.moacc.domain.order.entity.Order;
import com.sku.software.moacc.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderResponse {
    private Long orderId;
    private String orderName;
    private Long totalAmount;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
    private List<OrderItemResponse> orderItems;

    public static OrderResponse fromEntity(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderName(order.getOrderName())
                .totalAmount((long) order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getCreatedAt())
                .orderItems(order.getOrderItems().stream()
                        .map(OrderItemResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
