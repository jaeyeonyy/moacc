package com.sku.software.moacc.domain.product.controller;

import com.sku.software.moacc.domain.product.dto.response.CategoryResponse;
import com.sku.software.moacc.domain.product.enums.CategoryType;
import com.sku.software.moacc.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "카테고리 관리 API")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

  @GetMapping
  public ResponseEntity<BaseResponse<List<CategoryResponse>>> getAllCategories() {
    List<CategoryResponse> categories = Arrays.stream(CategoryType.values())
        .map(CategoryResponse::from)
        .toList();

    return ResponseEntity.ok(
        BaseResponse.success("카테고리 조회에 성공했습니다.", categories)
    );
  }
}
