package com.sku.software.moacc.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "BrandActiveRequest", description = "브랜드 활성화 상태 요청")
public class BrandActiveRequest {
    @Schema(description = "활성 여부", example = "true")
    private Boolean active;
}

