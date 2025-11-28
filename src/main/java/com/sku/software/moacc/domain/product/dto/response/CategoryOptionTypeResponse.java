package com.sku.software.moacc.domain.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "CategoryOptionTypeResponse", description = "카테고리 옵션 타입 응답")
public class CategoryOptionTypeResponse {
    @Schema(description = "아이디", example = "1")
    private Long id;

    @Schema(description = "카테고리 코드", example = "electronics")
    private String categoryCode;

    @Schema(description = "카테고리 이름", example = "전자제품")
    private String categoryName;

    @Schema(description = "옵션 타입 코드", example = "color")
    private String code;

    @Schema(description = "옵션 타입 이름", example = "색상")
    private String name;

    @Schema(description = "활성 여부", example = "true")
    private Boolean active;

    public static CategoryOptionTypeResponse of(Long id, String categoryCode, String categoryName, String code, String name, Boolean active) {
        return CategoryOptionTypeResponse.builder()
                .id(id)
                .categoryCode(categoryCode)
                .categoryName(categoryName)
                .code(code)
                .name(name)
                .active(active)
                .build();
    }
}
