package com.sku.software.moacc.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CategoryOptionTypeActiveRequest", description = "카테고리 옵션 타입 활성화 상태 변경 요청")
public class CategoryOptionTypeActiveRequest {
    @Schema(description = "활성 여부", example = "true")
    @NotNull
    private Boolean active;
}

