package com.sku.software.moacc.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CategoryOptionTypeRequest", description = "카테고리 옵션 타입 생성/수정 요청")
public class CategoryOptionTypeRequest {
    @Schema(description = "옵션 타입 코드", example = "color")
    private String code;

    @Schema(description = "옵션 타입 이름", example = "색상")
    private String name;

    // active 필드는 요청에서 제거되었습니다. 생성 시 기본값(true)을 사용하고, 업데이트 시 활성 상태는 변경되지 않습니다.
}
