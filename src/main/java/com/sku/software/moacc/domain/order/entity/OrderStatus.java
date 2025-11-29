package com.sku.software.moacc.domain.order.entity;

public enum OrderStatus {
    // 결제 대기 (주문 생성 직후)
    READY,
    // 결제 완료 (승인 성공)
    PAID,
    // 배송 준비 중
    SHIPPING,
    // 배송 완료
    DELIVERED,
    // 주문 전체 취소
    CANCELED
}