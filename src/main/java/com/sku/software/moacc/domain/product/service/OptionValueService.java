package com.sku.software.moacc.domain.product.service;

import com.sku.software.moacc.domain.product.dto.request.OptionValueRequest;
import com.sku.software.moacc.domain.product.dto.response.OptionValueResponse;
import com.sku.software.moacc.domain.product.entity.Category;
import com.sku.software.moacc.domain.product.entity.CategoryOptionType;
import com.sku.software.moacc.domain.product.entity.OptionValue;
import com.sku.software.moacc.domain.product.exception.CategoryErrorCode;
import com.sku.software.moacc.domain.product.exception.CategoryOptionTypeErrorCode;
import com.sku.software.moacc.domain.product.exception.OptionValueErrorCode;
import com.sku.software.moacc.domain.product.repository.CategoryOptionTypeRepository;
import com.sku.software.moacc.domain.product.repository.CategoryRepository;
import com.sku.software.moacc.domain.product.repository.OptionValueRepository;
import com.sku.software.moacc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionValueService {
    private final OptionValueRepository optionValueRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryOptionTypeRepository categoryOptionTypeRepository;

    @Transactional
    public OptionValueResponse create(String categoryCode, String optionCode, OptionValueRequest request) {
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        CategoryOptionType optionType = categoryOptionTypeRepository.findByCategoryIdAndCode(category.getId(), optionCode)
                .orElseThrow(() -> new CustomException(CategoryOptionTypeErrorCode.OPTION_TYPE_NOT_FOUND));

        // code 중복 체크
        if (optionValueRepository.existsByCategoryOptionTypeAndCode(optionType, request.getCode())) {
            throw new CustomException(OptionValueErrorCode.OPTION_VALUE_CODE_ALREADY_EXISTS);
        }

        // value 중복 체크
        if (optionValueRepository.existsByCategoryOptionTypeAndValue(optionType, request.getValue())) {
            throw new CustomException(OptionValueErrorCode.OPTION_VALUE_VALUE_ALREADY_EXISTS);
        }

        OptionValue entity = OptionValue.builder()
                .categoryOptionType(optionType)
                .value(request.getValue())
                .code(request.getCode())
                .build();

        OptionValue saved = optionValueRepository.save(entity);

        return OptionValueResponse.of(saved.getId(), optionType.getId(), saved.getValue(), saved.getCode(), saved.getActive());
    }

    public List<OptionValueResponse> listByOption(String categoryCode, String optionCode) {
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        CategoryOptionType optionType = categoryOptionTypeRepository.findByCategoryIdAndCode(category.getId(), optionCode)
                .orElseThrow(() -> new CustomException(CategoryOptionTypeErrorCode.OPTION_TYPE_NOT_FOUND));

        List<OptionValue> results = optionValueRepository.findAllByCategoryOptionTypeId(optionType.getId());
        return results.stream()
                .map(v -> OptionValueResponse.of(v.getId(), v.getCategoryOptionType().getId(), v.getValue(), v.getCode(), v.getActive()))
                .collect(Collectors.toList());
    }

    @Transactional
    public OptionValueResponse setActive(String categoryCode, String optionCode, String valueCode, Boolean active) {
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        CategoryOptionType optionType = categoryOptionTypeRepository.findByCategoryIdAndCode(category.getId(), optionCode)
                .orElseThrow(() -> new CustomException(CategoryOptionTypeErrorCode.OPTION_TYPE_NOT_FOUND));

        OptionValue entity = optionValueRepository.findByCategoryOptionTypeAndCode(optionType, valueCode)
                .orElseThrow(() -> new CustomException(OptionValueErrorCode.OPTION_VALUE_NOT_FOUND));

        entity.setActive(active);
        OptionValue saved = optionValueRepository.save(entity);

        return OptionValueResponse.of(saved.getId(), saved.getCategoryOptionType().getId(), saved.getValue(), saved.getCode(), saved.getActive());
    }
}
