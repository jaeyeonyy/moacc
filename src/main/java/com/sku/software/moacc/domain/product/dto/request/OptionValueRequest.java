package com.sku.software.moacc.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "OptionValueRequest", description = "옵션 값 생성 요청")
public class OptionValueRequest {
    @Schema(description = "옵션 값", example = "iPhone 14")
    @NotBlank
    private String value;

    @Schema(description = "옵션 값 코드(영문, 소문자, 언더스코어)", example = "iphone_14")
    @NotBlank
    private String code;
}
