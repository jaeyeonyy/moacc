package com.sku.software.moacc.domain.product.service;

import com.sku.software.moacc.domain.product.dto.CategoryResponse;
import com.sku.software.moacc.domain.product.repository.CategoryRepository;
import com.sku.software.moacc.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    long productCount = productRepository.countByCategoryId(category.getId());
                    return CategoryResponse.of(category, productCount);
                })
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategory(Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    long productCount = productRepository.countByCategoryId(category.getId());
                    return CategoryResponse.of(category, productCount);
                })
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }
}
