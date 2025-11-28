package com.sku.software.moacc.domain.product.service;

import com.sku.software.moacc.domain.product.dto.request.ProductCreateRequest;
import com.sku.software.moacc.domain.product.dto.request.ProductCreateWrapper;
import com.sku.software.moacc.domain.product.dto.request.ProductSkuRequest;
import com.sku.software.moacc.domain.product.dto.request.SkuOptionRequest;
import com.sku.software.moacc.domain.product.dto.request.SkuUpdateRequest;
import com.sku.software.moacc.domain.product.dto.response.ProductCreateResponse;
import com.sku.software.moacc.domain.product.dto.response.ProductDetailResponse;
import com.sku.software.moacc.domain.product.dto.response.ProductListItemResponse;
import com.sku.software.moacc.domain.product.dto.response.SkuOptionValueResponse;
import com.sku.software.moacc.domain.product.dto.response.SkuResponse;
import com.sku.software.moacc.domain.product.entity.Brand;
import com.sku.software.moacc.domain.product.entity.Category;
import com.sku.software.moacc.domain.product.entity.CategoryOptionType;
import com.sku.software.moacc.domain.product.entity.OptionValue;
import com.sku.software.moacc.domain.product.entity.Product;
import com.sku.software.moacc.domain.product.entity.ProductSku;
import com.sku.software.moacc.domain.product.entity.SkuOptionValue;
import com.sku.software.moacc.domain.product.exception.BrandErrorCode;
import com.sku.software.moacc.domain.product.exception.CategoryErrorCode;
import com.sku.software.moacc.domain.product.exception.CategoryOptionTypeErrorCode;
import com.sku.software.moacc.domain.product.exception.OptionValueErrorCode;
import com.sku.software.moacc.domain.product.repository.BrandRepository;
import com.sku.software.moacc.domain.product.repository.CategoryOptionTypeRepository;
import com.sku.software.moacc.domain.product.repository.CategoryRepository;
import com.sku.software.moacc.domain.product.repository.OptionValueRepository;
import com.sku.software.moacc.domain.product.repository.ProductRepository;
import com.sku.software.moacc.domain.product.repository.ProductSkuRepository;
import com.sku.software.moacc.domain.product.repository.SkuOptionValueRepository;
import com.sku.software.moacc.global.exception.CustomException;
import com.sku.software.moacc.global.exception.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductSkuRepository productSkuRepository;
    private final CategoryOptionTypeRepository categoryOptionTypeRepository;
    private final OptionValueRepository optionValueRepository;
    private final SkuOptionValueRepository skuOptionValueRepository;

    @Transactional
    public ProductCreateResponse createProduct(ProductCreateWrapper wrapper) {
        ProductCreateRequest req = wrapper.getProduct();
        List<ProductSkuRequest> skuRequests = wrapper.getSkus();

        // category
        Category category = categoryRepository.findByCode(req.getCategoryCode())
                .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        // brand (optional)
        Brand brand = null;
        if (req.getBrand() != null && !req.getBrand().isBlank()) {
            brand = brandRepository.findByName(req.getBrand())
                    .orElseThrow(() -> new CustomException(BrandErrorCode.BRAND_NOT_FOUND));
        }

        // create product
        Product product = Product.builder()
                .name(req.getName())
                .category(category)
                .brand(brand)
                .imageUrl(req.getImageUrl())
                .description(req.getDescription())
                .build();

        product = productRepository.save(product);

        List<Long> savedSkuIds = new ArrayList<>();

        // iterate skus
        for (ProductSkuRequest skuReq : skuRequests) {
            ProductSku sku = ProductSku.builder()
                    .product(product)
                    .price(skuReq.getPrice())
                    .stockQty(skuReq.getStockQty())
                    .safetyStockQty(skuReq.getSafetyStockQty())
                    .build();
            sku = productSkuRepository.save(sku);
            savedSkuIds.add(sku.getId());

            // options for this sku (optional)
            List<SkuOptionRequest> options = skuReq.getOptions();
            if (options != null) {
                for (SkuOptionRequest opt : options) {
                    // find option type by category id + code
                    CategoryOptionType optionType = categoryOptionTypeRepository.findByCategoryIdAndCode(category.getId(), opt.getTypeCode())
                            .orElseThrow(() -> new CustomException(CategoryOptionTypeErrorCode.OPTION_TYPE_NOT_FOUND));

                    // find option value by option type & code
                    OptionValue optionValue = optionValueRepository.findByCategoryOptionTypeAndCode(optionType, opt.getValueCode())
                            .orElseThrow(() -> new CustomException(OptionValueErrorCode.OPTION_VALUE_NOT_FOUND));

                    // create sku option value
                    SkuOptionValue sov = SkuOptionValue.builder()
                            .sku(sku)
                            .categoryOptionType(optionType)
                            .optionValue(optionValue)
                            .build();
                    skuOptionValueRepository.save(sov);
                }
            }
        }

        return new ProductCreateResponse(product.getId(), savedSkuIds);
    }

    // 페이징 없이 모든 상품을 반환, categoryCode가 null이 아니면 필터
    public List<ProductListItemResponse> listAllProducts(String categoryCode) {
        List<Product> products;
        if (categoryCode == null || categoryCode.isBlank()) {
            products = productRepository.findAll();
        } else {
            Category category = categoryRepository.findByCode(categoryCode)
                    .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_NOT_FOUND));
            products = productRepository.findByCategoryId(category.getId());
        }

        return products.stream().map(p -> new ProductListItemResponse(
                p.getId(),
                p.getName(),
                p.getCategory().getId(),
                p.getCategory().getCode(),
                p.getBrand() != null ? p.getBrand().getName() : null,
                p.getImageUrl(),
                p.getDescription()
        )).collect(Collectors.toList());
    }

    // 제품 상세 조회 (SKU + 옵션 값 포함)
    public ProductDetailResponse getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.RESOURCE_NOT_FOUND));

        List<ProductSku> skus = productSkuRepository.findAllByProductId(productId);
        List<SkuResponse> skuResponses = new ArrayList<>();

        for (ProductSku sku : skus) {
            List<SkuOptionValue> sovList = skuOptionValueRepository.findAllBySkuId(sku.getId());
            List<SkuOptionValueResponse> optionResponses = new ArrayList<>();
            for (SkuOptionValue sov : sovList) {
                CategoryOptionType cot = sov.getCategoryOptionType();
                OptionValue ov = sov.getOptionValue();
                optionResponses.add(new SkuOptionValueResponse(
                        cot.getId(),
                        cot.getCode(),
                        cot.getName(),
                        ov.getId(),
                        ov.getCode(),
                        ov.getValue()
                ));
            }
            // availableQuantity 계산 (재고 - 안전재고), null 안전성 및 음수 방지
            int stock = sku.getStockQty() != null ? sku.getStockQty() : 0;
            int safety = sku.getSafetyStockQty() != null ? sku.getSafetyStockQty() : 0;
            int available = stock - safety;
            if (available < 0) available = 0;
            skuResponses.add(new SkuResponse(sku.getId(), sku.getPrice(), sku.getStockQty(), sku.getSafetyStockQty(), available, sku.getActive(), optionResponses));
        }

        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getCategory().getId(),
                product.getCategory().getCode(),
                product.getBrand() != null ? product.getBrand().getName() : null,
                product.getImageUrl(),
                product.getDescription(),
                skuResponses
        );
    }

    // SKU 업데이트 (fields + options)
    @Transactional
    public SkuResponse updateSku(Long skuId, SkuUpdateRequest req) {
        ProductSku sku = productSkuRepository.findById(skuId)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.RESOURCE_NOT_FOUND));

        // update basic fields
        sku.update(req.getPrice(), req.getStockQty(), req.getSafetyStockQty(), req.getActive());
        productSkuRepository.save(sku);

        // 옵션 수정 불가: 요청 DTO에 options 필드가 제거되었으므로 옵션은 변경하지 않습니다.

        // build response (현재 할당된 옵션들을 그대로 반환)
        List<SkuOptionValue> sovList = skuOptionValueRepository.findAllBySkuId(skuId);
        List<SkuOptionValueResponse> optionResponses = new ArrayList<>();
        for (SkuOptionValue sov : sovList) {
            CategoryOptionType cot = sov.getCategoryOptionType();
            OptionValue ov = sov.getOptionValue();
            optionResponses.add(new SkuOptionValueResponse(
                    cot.getId(), cot.getCode(), cot.getName(), ov.getId(), ov.getCode(), ov.getValue()
            ));
        }

        // availableQuantity 계산 및 반환
        int stock = sku.getStockQty() != null ? sku.getStockQty() : 0;
        int safety = sku.getSafetyStockQty() != null ? sku.getSafetyStockQty() : 0;
        int available = stock - safety;
        if (available < 0) available = 0;

        return new SkuResponse(sku.getId(), sku.getPrice(), sku.getStockQty(), sku.getSafetyStockQty(), available, sku.getActive(), optionResponses);
    }
}
