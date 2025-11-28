package com.sku.software.moacc.domain.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "OptionValueResponse", description = "옵션 값 응답")
public class OptionValueResponse {
    @Schema(description = "옵션 값 ID", example = "1")
    private Long id;

    @Schema(description = "카테고리 옵션 타입 ID", example = "10")
    private Long categoryOptionTypeId;

    @Schema(description = "값", example = "iPhone 14")
    private String value;

    @Schema(description = "값 코드", example = "iphone_14")
    private String code;

    @Schema(description = "활성 여부", example = "true")
    private Boolean active;

    public static OptionValueResponse of(Long id, Long categoryOptionTypeId, String value, String code, Boolean active) {
        return new OptionValueResponse(id, categoryOptionTypeId, value, code, active);
    }
}
