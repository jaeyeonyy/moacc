package com.sku.software.moacc.domain.product.controller;

import com.sku.software.moacc.domain.product.dto.ProductRequest;
import com.sku.software.moacc.domain.product.dto.ProductResponse;
import com.sku.software.moacc.domain.product.service.ProductService;
import com.sku.software.moacc.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product", description = "상품 관리 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "상품 등록 성공",
            content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    @PostMapping
    public ResponseEntity<BaseResponse<ProductResponse>> createProduct(
            @RequestBody ProductRequest request) {
        return ResponseEntity.ok(BaseResponse.success(productService.createProduct(request)));
    }

    @Operation(summary = "상품 상세 조회", description = "상품 ID로 상품을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> getProduct(
            @Parameter(description = "상품 ID") @PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.success(productService.getProduct(id)));
    }

    @Operation(summary = "전체 상품 목록 조회", description = "모든 상품을 페이지 단위로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공")
    @Parameters({
        @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", schema = @Schema(type = "integer", defaultValue = "0")),
        @Parameter(name = "size", description = "한 페이지당 항목 수", schema = @Schema(type = "integer", defaultValue = "10")),
        @Parameter(name = "sort", description = "정렬 기준 (예: id,desc)", schema = @Schema(type = "string", defaultValue = "id,desc"))
    })
    @GetMapping
    public ResponseEntity<BaseResponse<Page<ProductResponse>>> getAllProducts(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(BaseResponse.success(productService.getAllProducts(pageable)));
    }

    @Operation(summary = "카테고리별 상품 목록 조회", description = "특정 카테고리의 상품을 페이지 단위로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리별 상품 목록 조회 성공")
    @Parameters({
        @Parameter(name = "categoryId", description = "카테고리 ID", required = true),
        @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", schema = @Schema(type = "integer", defaultValue = "0")),
        @Parameter(name = "size", description = "한 페이지당 항목 수", schema = @Schema(type = "integer", defaultValue = "10")),
        @Parameter(name = "sort", description = "정렬 기준 (예: id,desc)", schema = @Schema(type = "string", defaultValue = "id,desc"))
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<BaseResponse<Page<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(BaseResponse.success(productService.getProductsByCategory(categoryId, pageable)));
    }
    @Operation(summary = "상품 정보 수정", description = "상품 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "상품 수정 성공")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> updateProduct(
            @Parameter(description = "상품 ID") @PathVariable Long id,
            @RequestBody ProductRequest request) {
        return ResponseEntity.ok(BaseResponse.success(productService.updateProduct(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
