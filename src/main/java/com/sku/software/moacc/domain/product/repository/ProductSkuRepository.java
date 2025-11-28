package com.sku.software.moacc.domain.product.repository;

import com.sku.software.moacc.domain.product.entity.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSkuRepository extends JpaRepository<ProductSku, Long> {
    List<ProductSku> findAllByProductId(Long productId);
}
