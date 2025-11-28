package com.sku.software.moacc.domain.product.controller;

import com.sku.software.moacc.domain.product.dto.request.SkuUpdateRequest;
import com.sku.software.moacc.domain.product.dto.response.SkuResponse;
import com.sku.software.moacc.domain.product.service.ProductService;
import com.sku.software.moacc.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ProductSku", description = "SKU 관리 API")
@RestController
@RequestMapping("/api/skus")
@RequiredArgsConstructor
public class ProductSkuController {

    private final ProductService productService;

    @Operation(summary = "SKU 수정 (가격/재고/안전재고/활성/옵션) - 옵션을 제공하면 기존 옵션은 대체됩니다")
    @PatchMapping("/{skuId}")
    public ResponseEntity<BaseResponse<SkuResponse>> updateSku(
            @PathVariable Long skuId,
            @RequestBody @Valid SkuUpdateRequest request
    ) {
        SkuResponse resp = productService.updateSku(skuId, request);
        return ResponseEntity.ok(BaseResponse.success("SKU 수정에 성공했습니다.", resp));
    }
}

