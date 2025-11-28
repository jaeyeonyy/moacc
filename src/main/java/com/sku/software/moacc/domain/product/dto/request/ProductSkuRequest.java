package com.sku.software.moacc.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductSkuRequest", description = "상품 SKU 생성 요청")
public class ProductSkuRequest {
    @Schema(description = "옵션 목록", example = "[{typeCode:\"COLOR\", valueCode:\"BLACK\"}]")
    private List<SkuOptionRequest> options;

    @NotNull
    @Min(0)
    private Integer price;

    @NotNull
    @Min(0)
    private Integer stockQty;

    @NotNull
    @Min(0)
    private Integer safetyStockQty;
}

