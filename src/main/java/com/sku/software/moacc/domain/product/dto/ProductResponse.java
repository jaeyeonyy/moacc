package com.sku.software.moacc.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Schema(description = "상품 응답")
@Getter
@AllArgsConstructor
@Builder
public class ProductResponse {
    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "카테고리 정보")
    private CategoryInfo category;

    @Schema(description = "상품명", example = "아이폰 15 프로 케이스")
    private String name;

    @Schema(description = "상품 설명", example = "고급 실리콘 소재로 제작된 아이폰 15 프로용 케이스입니다.")
    private String description;

    @Schema(description = "상품 썸네일 URL", example = "https://example.com/images/iphone-case.jpg")
    private String thumbnailUrl;

    @Schema(description = "상품 가격", example = "25000")
    private Integer price;

    @Schema(description = "재고 수량", example = "100")
    private Integer quantity;

    @Schema(description = "카테고리 정보")
    @Getter
    @AllArgsConstructor
    @Builder
    public static class CategoryInfo {
        @Schema(description = "카테고리 ID", example = "1")
        private Long id;

        @Schema(description = "카테고리 코드명", example = "PHONE_CASE")
        private String name;

        @Schema(description = "카테고리 표시명", example = "휴대폰 케이스")
        private String displayName;
    }
}
