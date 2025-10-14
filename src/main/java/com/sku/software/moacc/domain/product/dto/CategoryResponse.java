package com.sku.software.moacc.domain.product.dto;

import com.sku.software.moacc.domain.product.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "카테고리 응답")
@Getter
@AllArgsConstructor
@Builder
public class CategoryResponse {
    @Schema(description = "카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "카테고리 코드명", example = "PHONE_CASE")
    private String name;

    @Schema(description = "카테고리 표시명", example = "휴대폰 케이스")
    private String displayName;

    @Schema(description = "카테고리 내 상품 수", example = "42")
    private long productCount;

    public static CategoryResponse of(Category category, long productCount) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .displayName(category.getDisplayName())
                .productCount(productCount)
                .build();
    }
}
