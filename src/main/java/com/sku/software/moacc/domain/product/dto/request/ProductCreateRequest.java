package com.sku.software.moacc.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductCreateRequest", description = "상품 + SKU 동시 생성 요청")
public class ProductCreateRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String categoryCode;

    // optional
    private String brand;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String description;

    // 적용할 옵션타입 코드 목록 (optional)
    private List<String> appliedOptionTypeCodes;

    @NotNull
    private List<ProductSkuRequest> skus;
}

