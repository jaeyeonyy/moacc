package com.sku.software.moacc.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Schema(description = "상품 등록/수정 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;

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
}
