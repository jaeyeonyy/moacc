package com.sku.software.moacc.domain.order.entity;

import com.sku.software.moacc.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
// 'ORDER'는 SQL 예약어일 수 있으므로 테이블 이름을 'orders'로 지정
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id; // Auto-Increment (Long)

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // OrderStatus Enum을 문자열(String)로 저장
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    private int totalAmount;

    private String orderName; // 토스 orderName에 사용

    private String shippingAddress;

    // Order:OrderItem = 1:N 관계 매핑
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Order:Payment = 1:1 관계 매핑
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    // 양방향 연관관계 편의 메서드
    public void setPayment(Payment payment) {
        this.payment = payment;
        if (payment != null) {
            payment.setOrder(this);
        }
    }
}