package com.sku.software.moacc.domain.order.entity;

import com.sku.software.moacc.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "order_item")
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id; // Auto-Increment (Long)

    @Column(name = "product_id", nullable = false)
    private Long productId;

    private String productName;

    private int quantity;

    private int price;

    // OrderItem:Order = N:1 관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 양방향 연관관계 편의 메서드
    public void setOrder(Order order) {
        this.order = order;
        order.getOrderItems().add(this);
    }
}