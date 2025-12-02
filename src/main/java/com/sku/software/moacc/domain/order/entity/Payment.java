package com.sku.software.moacc.domain.order.entity;

import com.sku.software.moacc.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "payment")
public class Payment extends BaseTimeEntity {

    // 토스 결제 고유 키 (paymentKey)를 Primary Key로 사용
    @Id
    @Column(name = "payment_key", length = 200)
    private String paymentKey;

    // PaymentStatus Enum을 문자열(String)로 저장
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    private String method; // 결제 수단 (카드, 가상계좌 등)

    private int amount; // 실제 결제된 금액

    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

    // Order:Payment = 1:1 관계 매핑 (FK로 order_id 사용)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;

    // 생성자 추가 - paymentKey 설정용
    public Payment(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    // 양방향 연관관계 편의 메서드
    public void setOrder(Order order) {
        this.order = order;
        if (order != null && order.getPayment() != this) {
            order.setPayment(this);
        }
    }
}