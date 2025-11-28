package com.sku.software.moacc.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
@Schema(name = "ProductCreateWrapper", description = "상품 생성 요청 래퍼 (product + skus)")
public class ProductCreateWrapper {
    @NotNull
    @Valid
    private ProductCreateRequest product;

    @NotNull
    @Valid
    private List<ProductSkuRequest> skus;
}

