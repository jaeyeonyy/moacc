package com.sku.software.moacc.domain.product.repository;

import com.sku.software.moacc.domain.product.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {

  boolean existsByName(String name);

}
