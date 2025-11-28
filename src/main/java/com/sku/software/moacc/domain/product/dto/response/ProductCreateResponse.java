package com.sku.software.moacc.domain.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(name = "ProductCreateResponse", description = "상품 생성 응답(상품ID와 SKU ID 목록)")
public class ProductCreateResponse {
    @Schema(description = "생성된 상품 ID", example = "1")
    private Long productId;

    @Schema(description = "생성된 SKU ID 목록")
    private List<Long> skuIds;
}

