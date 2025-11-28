package com.sku.software.moacc.domain.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "BrandResponse", description = "브랜드 응답 DTO")
public class BrandResponse {
    @Schema(description = "브랜드 ID", example = "1")
    private Long id;

    @Schema(description = "브랜드명", example = "신지모루")
    private String name;

    @Schema(description = "활성 여부", example = "true")
    private Boolean active;

    public static BrandResponse of(Long id, String name, Boolean active) {
        return new BrandResponse(id, name, active);
    }
}
