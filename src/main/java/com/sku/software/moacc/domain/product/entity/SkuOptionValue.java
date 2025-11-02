package com.sku.software.moacc.domain.product.entity;

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
@Table(name = "sku_option_values")
public class SkuOptionValue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sku_id")
  private ProductSku sku;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_option_type_id")
  private CategoryOptionType categoryOptionType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "option_value_id")
  private OptionValue optionValue;
}