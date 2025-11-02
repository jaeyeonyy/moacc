package com.sku.software.moacc.domain.product.service;

import com.sku.software.moacc.domain.product.entity.Brand;
import com.sku.software.moacc.domain.product.exception.BrandErrorCode;
import com.sku.software.moacc.domain.product.repository.BrandRepository;
import com.sku.software.moacc.global.exception.CustomException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {
  private BrandRepository brandRepository;

  public void addBrand(String name) {
    log.info("[서비스] 브랜드 추가 시도 name = {}", name);

    if(brandRepository.existsByName(name)) {
      log.warn("[서비스] 브랜드 추가 실패 - 이미 존재하는 브랜드 name = {}", name);
      throw new CustomException(BrandErrorCode.BRAND_ALREADY_EXISTS);
    }

    Brand brand = Brand.builder()
        .name(name)
        .build();

    brandRepository.save(brand);
  }
}
