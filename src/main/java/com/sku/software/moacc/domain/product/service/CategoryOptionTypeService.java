package com.sku.software.moacc.domain.product.service;

import com.sku.software.moacc.domain.product.dto.request.CategoryOptionTypeRequest;
import com.sku.software.moacc.domain.product.dto.response.CategoryOptionTypeResponse;
import com.sku.software.moacc.domain.product.entity.Category;
import com.sku.software.moacc.domain.product.entity.CategoryOptionType;
import com.sku.software.moacc.domain.product.exception.CategoryErrorCode;
import com.sku.software.moacc.domain.product.exception.CategoryOptionTypeErrorCode;
import com.sku.software.moacc.domain.product.repository.CategoryOptionTypeRepository;
import com.sku.software.moacc.domain.product.repository.CategoryRepository;
import com.sku.software.moacc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryOptionTypeService {
    private final CategoryOptionTypeRepository categoryOptionTypeRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryOptionTypeResponse create(String categoryCode, CategoryOptionTypeRequest request) {
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        // 중복 체크: category + code
        if (categoryOptionTypeRepository.existsByCategoryAndCode(category, request.getCode())) {
            throw new CustomException(CategoryOptionTypeErrorCode.OPTION_TYPE_ALREADY_EXISTS);
        }

        CategoryOptionType entity = CategoryOptionType.builder()
                .category(category)
                .code(request.getCode())
                .name(request.getName())
                .build();

        CategoryOptionType saved = categoryOptionTypeRepository.save(entity);

        return CategoryOptionTypeResponse.of(saved.getId(), category.getCode(), category.getName(), saved.getCode(), saved.getName(), saved.getActive());
    }

    public List<CategoryOptionTypeResponse> listByCategory(String categoryCode) {
        // find category first to ensure category exists and to return category id in response
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        List<CategoryOptionType> results = categoryOptionTypeRepository.findAllByCategoryId(category.getId());
        return results.stream()
                .map(p -> CategoryOptionTypeResponse.of(p.getId(), p.getCategory().getCode(), p.getCategory().getName(), p.getCode(), p.getName(), p.getActive()))
                .collect(Collectors.toList());
    }

    // 카테고리와 무관하게 전체 옵션 타입 목록을 반환하는 메서드
    public List<CategoryOptionTypeResponse> listAll() {
        List<CategoryOptionType> results = categoryOptionTypeRepository.findAll();
        return results.stream()
                .map(p -> CategoryOptionTypeResponse.of(p.getId(), p.getCategory().getCode(), p.getCategory().getName(), p.getCode(), p.getName(), p.getActive()))
                .collect(Collectors.toList());
    }

    // 활성(active) 옵션 타입만 반환하는 메서드
    public List<CategoryOptionTypeResponse> listActiveByCategory(String categoryCode) {
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        List<CategoryOptionType> results = categoryOptionTypeRepository.findAllByCategoryIdAndActiveTrue(category.getId());
        return results.stream()
                .map(p -> CategoryOptionTypeResponse.of(p.getId(), p.getCategory().getCode(), p.getCategory().getName(), p.getCode(), p.getName(), p.getActive()))
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryOptionTypeResponse setActive(String categoryCode, String optionCode, Boolean active) {
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        CategoryOptionType entity = categoryOptionTypeRepository.findByCategoryIdAndCode(category.getId(), optionCode)
                .orElseThrow(() -> new CustomException(CategoryOptionTypeErrorCode.OPTION_TYPE_NOT_FOUND));

        // 활성 상태 변경
        entity.update(entity.getCode(), entity.getName(), active);
        CategoryOptionType saved = categoryOptionTypeRepository.save(entity);
        return CategoryOptionTypeResponse.of(saved.getId(), saved.getCategory().getCode(), saved.getCategory().getName(), saved.getCode(), saved.getName(), saved.getActive());
    }

    @Transactional
    public CategoryOptionTypeResponse update(Long id, CategoryOptionTypeRequest request) {
        CategoryOptionType entity = categoryOptionTypeRepository.findById(id)
                .orElseThrow(() -> new CustomException(CategoryOptionTypeErrorCode.OPTION_TYPE_NOT_FOUND));

        // code를 변경하려는 경우 중복 체크
        String newCode = request.getCode();
        if (newCode != null && !newCode.equals(entity.getCode())) {
            Category category = entity.getCategory();
            if (categoryOptionTypeRepository.existsByCategoryAndCode(category, newCode)) {
                throw new CustomException(CategoryOptionTypeErrorCode.OPTION_TYPE_ALREADY_EXISTS);
            }
        }

        // active 필드가 요청에서 제거되어 있으므로 업데이트 시 active는 변경하지 않습니다.
        entity.update(request.getCode(), request.getName(), null);
        CategoryOptionType saved = categoryOptionTypeRepository.save(entity);
        return CategoryOptionTypeResponse.of(saved.getId(), saved.getCategory().getCode(), saved.getCategory().getName(), saved.getCode(), saved.getName(), saved.getActive());
    }

    @Transactional
    public void delete(Long id) {
        categoryOptionTypeRepository.deleteById(id);
    }
}
