package com.sku.software.moacc.domain.product.repository;

import com.sku.software.moacc.domain.product.entity.CategoryOptionType;
import com.sku.software.moacc.domain.product.entity.OptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionValueRepository extends JpaRepository<OptionValue, Long> {
    boolean existsByCategoryOptionTypeAndValue(CategoryOptionType categoryOptionType, String value);

    boolean existsByCategoryOptionTypeAndCode(CategoryOptionType categoryOptionType, String code);

    List<OptionValue> findAllByCategoryOptionTypeId(Long categoryOptionTypeId);

    Optional<OptionValue> findByCategoryOptionTypeAndCode(CategoryOptionType categoryOptionType, String code);
}
