package com.sku.software.moacc.domain.product.controller;

import com.sku.software.moacc.domain.product.dto.request.ProductCreateWrapper;
import com.sku.software.moacc.domain.product.dto.response.ProductCreateResponse;
import com.sku.software.moacc.domain.product.dto.response.ProductDetailResponse;
import com.sku.software.moacc.domain.product.dto.response.ProductListItemResponse;
import com.sku.software.moacc.domain.product.service.ProductService;
import com.sku.software.moacc.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Product", description = "상품 관리 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "상품 + SKU 동시 생성")
    @PostMapping
    public ResponseEntity<BaseResponse<ProductCreateResponse>> createProduct(
            @RequestBody @Valid ProductCreateWrapper wrapper
    ) {
        ProductCreateResponse resp = productService.createProduct(wrapper);
        return ResponseEntity.ok(BaseResponse.success("상품 생성에 성공했습니다.", resp));
    }

    @Operation(summary = "상품 목록 조회 (페이징 없이 전체 반환)")
    @GetMapping
    public ResponseEntity<BaseResponse<List<ProductListItemResponse>>> listProducts(
            @RequestParam(required = false) String categoryCode
    ) {
        List<ProductListItemResponse> items = productService.listAllProducts(categoryCode);
        return ResponseEntity.ok(BaseResponse.success("상품 목록 조회에 성공했습니다.", items));
    }

    @Operation(summary = "상품 상세 조회 (SKU 및 옵션 포함)")
    @GetMapping("/{productId}")
    public ResponseEntity<BaseResponse<ProductDetailResponse>> getProductDetail(
            @PathVariable Long productId
    ) {
        ProductDetailResponse resp = productService.getProductDetail(productId);
        return ResponseEntity.ok(BaseResponse.success("상품 상세 조회에 성공했습니다.", resp));
    }

}
