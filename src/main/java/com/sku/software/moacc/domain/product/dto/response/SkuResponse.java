package com.sku.software.moacc.domain.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(name = "SkuResponse", description = "상품 SKU 및 옵션 목록 응답")
public class SkuResponse {
    @Schema(description = "SKU ID", example = "1")
    private Long id;

    @Schema(description = "가격", example = "19000")
    private Integer price;

    @Schema(description = "재고 수량", example = "50")
    private Integer stockQty;

    @Schema(description = "안전 재고", example = "5")
    private Integer safetyStockQty;

    @Schema(description = "판매 가능 수량 (재고 - 안전 재고)", example = "45")
    private Integer availableQuantity;

    @Schema(description = "활성 여부", example = "true")
    private Boolean active;

    @Schema(description = "이 SKU에 할당된 옵션 값들")
    private List<SkuOptionValueResponse> options;
}
