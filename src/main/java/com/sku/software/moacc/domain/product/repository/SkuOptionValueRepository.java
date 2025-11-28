package com.sku.software.moacc.domain.product.repository;

import com.sku.software.moacc.domain.product.entity.SkuOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkuOptionValueRepository extends JpaRepository<SkuOptionValue, Long> {
    List<SkuOptionValue> findAllBySkuId(Long skuId);

    // sku의 기존 옵션들을 모두 삭제할 때 사용
    void deleteAllBySkuId(Long skuId);
}
