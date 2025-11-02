package com.sku.software.moacc.domain.product.dto.response;

import com.sku.software.moacc.domain.product.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

  private String code;
  private String name;

  public static CategoryResponse from(CategoryType categoryType) {
    return CategoryResponse.builder()
        .code(categoryType.getCode())
        .name(categoryType.getDisplayName())
        .build();
  }
}