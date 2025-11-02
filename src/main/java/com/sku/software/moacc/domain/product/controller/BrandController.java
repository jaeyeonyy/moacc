package com.sku.software.moacc.domain.product.controller;

import com.sku.software.moacc.domain.product.service.BrandService;
import com.sku.software.moacc.global.response.BaseResponse;
import com.sku.software.moacc.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Brand", description = "브랜드 관리 API")
@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
  private BrandService brandService;

  @PostMapping
  public ResponseEntity<BaseResponse<Void>> addBrand(
      @RequestParam String name,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ){
    brandService.addBrand(name);
    return ResponseEntity.ok(BaseResponse.success("브랜드 추가에 성공했습니다.", null));
  }
}
