package com.sku.software.moacc.domain.order.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateResponse {
    // 상점의 주문 ID (토스 paymentWidget 호출 시 orderId로 사용됨)
    private Long orderId;

    private String tossOrderId;

    // 결제 위젯에 표시될 주문명
    private String orderName;

    // 고객명
    private String customerName;

    // 고객 이메일
    private String customerEmail;

    // 결제 요청 금액
    private int amount;

    // 토스페이먼츠 클라이언트 키 (ClientKey) - 실제로는 설정 파일에서 가져와야 함
    private String clientKey;
}