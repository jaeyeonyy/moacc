package com.sku.software.moacc.domain.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "SkuOptionValueResponse", description = "SKU에 할당된 옵션 값 정보")
public class SkuOptionValueResponse {
    @Schema(description = "옵션 타입 ID", example = "10")
    private Long optionTypeId;

    @Schema(description = "옵션 타입 코드", example = "COLOR")
    private String optionTypeCode;

    @Schema(description = "옵션 타입 이름", example = "색상")
    private String optionTypeName;

    @Schema(description = "옵션 값 ID", example = "100")
    private Long optionValueId;

    @Schema(description = "옵션 값 코드", example = "BLACK")
    private String optionValueCode;

    @Schema(description = "옵션 값", example = "검정")
    private String optionValue;
}

