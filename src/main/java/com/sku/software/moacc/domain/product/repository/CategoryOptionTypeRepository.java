package com.sku.software.moacc.domain.product.repository;

import com.sku.software.moacc.domain.product.entity.Category;
import com.sku.software.moacc.domain.product.entity.CategoryOptionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryOptionTypeRepository extends JpaRepository<CategoryOptionType, Long> {

    // Page 대신 List를 이용해 전체 목록을 반환하는 메서드
    List<CategoryOptionType> findAllByCategoryId(Long categoryId);

    // category + code 유니크 제약을 고려한 존재 여부 체크
    boolean existsByCategoryAndCode(Category category, String code);

    // category id + code로 옵션타입 조회 (서비스에서 사용)
    Optional<CategoryOptionType> findByCategoryIdAndCode(Long categoryId, String code);

    // category id로 조회하되 active가 true인 것만 반환
    List<CategoryOptionType> findAllByCategoryIdAndActiveTrue(Long categoryId);
}
