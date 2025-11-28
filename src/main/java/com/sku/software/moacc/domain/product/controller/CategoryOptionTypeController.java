package com.sku.software.moacc.domain.product.controller;

import com.sku.software.moacc.domain.product.dto.request.CategoryOptionTypeActiveRequest;
import com.sku.software.moacc.domain.product.dto.request.CategoryOptionTypeRequest;
import com.sku.software.moacc.domain.product.dto.response.CategoryOptionTypeResponse;
import com.sku.software.moacc.domain.product.service.CategoryOptionTypeService;
import com.sku.software.moacc.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "CategoryOptionType", description = "카테고리 옵션 타입 관리 API")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryOptionTypeController {

    private final CategoryOptionTypeService categoryOptionTypeService;

    @Operation(summary = "카테고리 옵션 타입 생성")
    @PostMapping("/{categoryCode}/option-types")
    public ResponseEntity<BaseResponse<CategoryOptionTypeResponse>> createOptionType(
            @PathVariable String categoryCode,
            @RequestBody @Valid CategoryOptionTypeRequest request
    ) {
        CategoryOptionTypeResponse response = categoryOptionTypeService.create(categoryCode, request);
        return ResponseEntity.ok(BaseResponse.success("옵션 타입 생성에 성공했습니다.", response));
    }

    @Operation(summary = "카테고리의 옵션 타입 목록 조회")
    @GetMapping("/{categoryCode}/option-types")
    public ResponseEntity<BaseResponse<List<CategoryOptionTypeResponse>>> listOptionTypesByCategory(
            @PathVariable String categoryCode
    ) {
        List<CategoryOptionTypeResponse> responses = categoryOptionTypeService.listByCategory(categoryCode);
        return ResponseEntity.ok(BaseResponse.success("카테고리 옵션 타입 조회에 성공했습니다.", responses));
    }

    @Operation(summary = "카테고리 상관없이 전체 옵션 타입 목록 조회")
    @GetMapping("/option-types")
    public ResponseEntity<BaseResponse<List<CategoryOptionTypeResponse>>> listAllOptionTypes() {
        List<CategoryOptionTypeResponse> responses = categoryOptionTypeService.listAll();
        return ResponseEntity.ok(BaseResponse.success("전체 옵션 타입 조회에 성공했습니다.", responses));
    }

    @Operation(summary = "카테고리의 활성 옵션 타입 목록 조회")
    @GetMapping("/{categoryCode}/option-types/active")
    public ResponseEntity<BaseResponse<List<CategoryOptionTypeResponse>>> listActiveOptionTypesByCategory(
            @PathVariable String categoryCode
    ) {
        List<CategoryOptionTypeResponse> responses = categoryOptionTypeService.listActiveByCategory(categoryCode);
        return ResponseEntity.ok(BaseResponse.success("활성 옵션 타입 조회에 성공했습니다.", responses));
    }

    @Operation(summary = "카테고리 옵션 타입 활성/비활성 변경")
    @PatchMapping("/{categoryCode}/option-types/{optionCode}/active")
    public ResponseEntity<BaseResponse<CategoryOptionTypeResponse>> setOptionTypeActive(
            @PathVariable String categoryCode,
            @PathVariable String optionCode,
            @RequestBody @Valid CategoryOptionTypeActiveRequest request
    ) {
        CategoryOptionTypeResponse response = categoryOptionTypeService.setActive(categoryCode, optionCode, request.getActive());
        return ResponseEntity.ok(BaseResponse.success("옵션 타입 활성 상태 변경에 성공했습니다.", response));
    }
}
