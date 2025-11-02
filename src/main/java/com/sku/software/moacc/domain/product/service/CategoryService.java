package com.sku.software.moacc.domain.product.service;

import com.sku.software.moacc.domain.product.repository.CategoryRepository;
import com.sku.software.moacc.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


}
