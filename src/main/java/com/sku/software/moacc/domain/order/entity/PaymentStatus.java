package com.sku.software.moacc.domain.order.entity;

public enum PaymentStatus {
    // 결제 요청 생성됨
    READY,
    // 결제 승인 완료
    DONE,
    // 전액 취소
    CANCELED,
    // 부분 취소
    PARTIAL_CANCELED
}