package com.sku.software.moacc.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SkuOptionRequest", description = "SKU의 옵션 지정 (옵션타입 코드 + 옵션값 코드)")
public class SkuOptionRequest {
    @Schema(description = "옵션 타입 코드", example = "COLOR")
    @NotBlank
    private String typeCode;

    @Schema(description = "옵션 값 코드", example = "BLACK")
    @NotBlank
    private String valueCode;
}

