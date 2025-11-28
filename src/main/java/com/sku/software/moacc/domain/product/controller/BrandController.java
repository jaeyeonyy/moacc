package com.sku.software.moacc.domain.product.controller;

import com.sku.software.moacc.domain.product.dto.request.BrandActiveRequest;
import com.sku.software.moacc.domain.product.dto.request.BrandCreateRequest;
import com.sku.software.moacc.domain.product.dto.response.BrandResponse;
import com.sku.software.moacc.domain.product.service.BrandService;
import com.sku.software.moacc.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Tag(name = "Brand", description = "브랜드 관리 API")
@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @Operation(summary = "브랜드 목록 조회")
    @GetMapping
    public ResponseEntity<BaseResponse<List<BrandResponse>>> listBrands() {
        List<BrandResponse> list = brandService.listAllBrands();
        return ResponseEntity.ok(BaseResponse.success("브랜드 목록 조회에 성공했습니다.", list));
    }

    @Operation(summary = "활성 브랜드 목록 조회")
    @GetMapping("/active")
    public ResponseEntity<BaseResponse<List<BrandResponse>>> listActiveBrands() {
        List<BrandResponse> list = brandService.listActiveBrands();
        return ResponseEntity.ok(BaseResponse.success("활성 브랜드 목록 조회에 성공했습니다.", list));
    }

    @Operation(summary = "브랜드 활성화 상태 변경")
    @PatchMapping("/{id}/active")
    public ResponseEntity<BaseResponse<BrandResponse>> setActive(@PathVariable Long id, @RequestBody BrandActiveRequest req) {
        BrandResponse resp = brandService.setActive(id, req.getActive());
        return ResponseEntity.ok(BaseResponse.success("브랜드 활성 상태 변경에 성공했습니다.", resp));
    }

    @Operation(summary = "브랜드 생성")
    @PostMapping
    public ResponseEntity<BaseResponse<BrandResponse>> createBrand(@Valid @RequestBody BrandCreateRequest req) {
        BrandResponse resp = brandService.createBrand(req.getName());
        return ResponseEntity.ok(BaseResponse.success("브랜드 생성에 성공했습니다.", resp));
    }
}
