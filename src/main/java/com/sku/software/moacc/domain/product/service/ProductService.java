package com.sku.software.moacc.domain.product.service;

import com.sku.software.moacc.domain.product.dto.ProductRequest;
import com.sku.software.moacc.domain.product.dto.ProductResponse;
import com.sku.software.moacc.domain.product.entity.Product;
import com.sku.software.moacc.domain.product.entity.Category;
import com.sku.software.moacc.domain.product.repository.ProductRepository;
import com.sku.software.moacc.domain.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // Create
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Product product = Product.builder()
                .category(category)
                .name(request.getName())
                .description(request.getDescription())
                .thumbnailUrl(request.getThumbnailUrl())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();

        Product savedProduct = productRepository.save(product);
        return toResponse(savedProduct);
    }

    // Read One
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return toResponse(product);
    }

    // Read All (with pagination)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toResponse);
    }

    /**
     * 카테고리별 상품 목록 조회
     */
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        // 카테고리 존재 여부 확인
        categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        return productRepository.findByCategoryId(categoryId, pageable)
                .map(this::toResponse);
    }

    /**
     * 카테고리별 상품 수량 조회
     */
    public long getProductCountByCategory(Long categoryId) {
        // 카테고리 존재 여부 확인
        categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        return productRepository.countByCategoryId(categoryId);
    }

    // Update
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        product = Product.builder()
                .id(product.getId())
                .category(category)
                .name(request.getName())
                .description(request.getDescription())
                .thumbnailUrl(request.getThumbnailUrl())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();

        return toResponse(productRepository.save(product));
    }

    // Delete
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .category(toCategoryInfo(product.getCategory()))
                .name(product.getName())
                .description(product.getDescription())
                .thumbnailUrl(product.getThumbnailUrl())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    private ProductResponse.CategoryInfo toCategoryInfo(Category category) {
        return ProductResponse.CategoryInfo.builder()
                .id(category.getId())
                .name(category.getName())
                .displayName(category.getDisplayName())
                .build();
    }
}
