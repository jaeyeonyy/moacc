package com.sku.software.moacc.domain.order.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentConfirmResponse {
    private Long orderId;
    private String paymentKey;
    private int amount;
    private String status; // 최종 결제 상태 (DONE)
    private String message; // 성공 메시지
}