package com.sku.software.moacc.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "BrandCreateRequest", description = "브랜드 생성 요청 DTO")
public class BrandCreateRequest {
    @NotBlank
    @Schema(description = "브랜드명", example = "신지모루")
    private String name;
}
