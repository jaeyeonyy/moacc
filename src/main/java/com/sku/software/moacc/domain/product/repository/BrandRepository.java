package com.sku.software.moacc.domain.product.repository;

import com.sku.software.moacc.domain.product.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

  boolean existsByName(String name);

  Optional<Brand> findByName(String name);

  List<Brand> findAllByActiveTrue();

}
