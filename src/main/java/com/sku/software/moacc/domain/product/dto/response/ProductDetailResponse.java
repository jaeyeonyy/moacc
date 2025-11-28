package com.sku.software.moacc.domain.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(name = "ProductDetailResponse", description = "상품 상세 응답 (SKU 및 옵션 포함)")
public class ProductDetailResponse {
    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품명", example = "폰케이스")
    private String name;

    @Schema(description = "카테고리 ID", example = "3")
    private Long categoryId;

    @Schema(description = "카테고리 코드", example = "PHONE_CASE")
    private String categoryCode;

    @Schema(description = "브랜드명", example = "신지모루")
    private String brandName;

    @Schema(description = "이미지 URL", example = "https://placehold.co/300x300?text=Product")
    private String imageUrl;

    @Schema(description = "설명", example = "스마트폰에 사용하는 폰케이스입니다.")
    private String description;

    @Schema(description = "이 상품에 속한 SKU 리스트")
    private List<SkuResponse> skus;
}

