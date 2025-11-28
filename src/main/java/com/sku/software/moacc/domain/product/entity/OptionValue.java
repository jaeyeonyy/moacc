package com.sku.software.moacc.domain.product.entity;

import com.sku.software.moacc.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "option_values", uniqueConstraints = {
        @UniqueConstraint(name = "uk_option_type_value", columnNames = {"category_option_type_id", "value"}),
        @UniqueConstraint(name = "uk_option_type_code", columnNames = {"category_option_type_id", "code"})
})
public class OptionValue extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_option_type_id", nullable = false)
  private CategoryOptionType categoryOptionType;

  @Column(nullable = false)
  private String value;

  @Column(nullable = false)
  private String code;

  @Builder.Default
  private Boolean active = true;

  public void setActive(Boolean active) {
      this.active = active;
  }

}