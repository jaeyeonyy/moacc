package com.sku.software.moacc.domain.product.service;

import com.sku.software.moacc.domain.product.entity.Brand;
import com.sku.software.moacc.domain.product.exception.BrandErrorCode;
import com.sku.software.moacc.domain.product.repository.BrandRepository;
import com.sku.software.moacc.domain.product.dto.response.BrandResponse;
import com.sku.software.moacc.global.exception.CustomException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {
  private final BrandRepository brandRepository;

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

  public List<BrandResponse> listAllBrands() {
    return brandRepository.findAll().stream()
        .map(b -> BrandResponse.of(b.getId(), b.getName(), b.getActive()))
        .collect(Collectors.toList());
  }

  public List<BrandResponse> listActiveBrands() {
    return brandRepository.findAllByActiveTrue().stream()
        .map(b -> BrandResponse.of(b.getId(), b.getName(), b.getActive()))
        .collect(Collectors.toList());
  }

  public BrandResponse setActive(Long id, Boolean active) {
    Brand brand = brandRepository.findById(id)
        .orElseThrow(() -> new com.sku.software.moacc.global.exception.CustomException(BrandErrorCode.BRAND_NOT_FOUND));

    brand.setActive(active);
    brandRepository.save(brand);

    return BrandResponse.of(brand.getId(), brand.getName(), brand.getActive());
  }

  public BrandResponse createBrand(String name) {
    log.info("[서비스] 브랜드 생성 시도 name = {}", name);

    if (brandRepository.existsByName(name)) {
      log.warn("[서비스] 브랜드 생성 실패 - 이미 존재하는 브랜드 name = {}", name);
      throw new CustomException(BrandErrorCode.BRAND_ALREADY_EXISTS);
    }

    Brand brand = Brand.builder()
        .name(name)
        .build();

    Brand saved = brandRepository.save(brand);

    return BrandResponse.of(saved.getId(), saved.getName(), saved.getActive());
  }
}
