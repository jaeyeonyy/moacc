package com.sku.software.moacc.domain.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_skus")
public class ProductSku {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(nullable = false)
  private Integer price;

  @Builder.Default
  @Column(name = "stock_qty", nullable = false)
  private Integer stockQty= 0;

  @Builder.Default
  @Column(name = "safety_stock_qty", nullable = false)
  private Integer safetyStockQty=5;

  @Builder.Default
  @Column(nullable = false)
  private Boolean active=Boolean.TRUE;

  // 업데이트용 메서드
  public void update(Integer price, Integer stockQty, Integer safetyStockQty, Boolean active) {
    if (price != null) this.price = price;
    if (stockQty != null) this.stockQty = stockQty;
    if (safetyStockQty != null) this.safetyStockQty = safetyStockQty;
    if (active != null) this.active = active;
  }
}