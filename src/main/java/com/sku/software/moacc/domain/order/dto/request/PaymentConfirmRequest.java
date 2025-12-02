package com.sku.software.moacc.domain.order.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentConfirmRequest {
    // 토스에서 생성된 결제 고유 키
    private String paymentKey;

    // 상점에서 보낸 주문 ID
    private String tossOrderId;

    // 결제 요청 금액
    private Integer amount;
}