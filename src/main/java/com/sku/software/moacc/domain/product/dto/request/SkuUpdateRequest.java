package com.sku.software.moacc.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SkuUpdateRequest", description = "SKU 수정 요청 (옵션은 수정 불가)")
public class SkuUpdateRequest {
    @Schema(description = "가격", example = "19000")
    @Min(0)
    private Integer price;

    @Schema(description = "재고 수량", example = "50")
    @Min(0)
    private Integer stockQty;

    @Schema(description = "안전 재고", example = "5")
    @Min(0)
    private Integer safetyStockQty;

    @Schema(description = "활성 여부", example = "true")
    private Boolean active;

    // 옵션 수정은 허용하지 않습니다. 옵션을 변경하려면 SKU 자체를 삭제하고 재생성하거나
    // 별도의 비즈니스 로직을 통해 처리해야 합니다.
}
