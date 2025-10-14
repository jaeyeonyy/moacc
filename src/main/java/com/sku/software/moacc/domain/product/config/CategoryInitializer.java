package com.sku.software.moacc.domain.product.config;

import com.sku.software.moacc.domain.product.entity.Category;
import com.sku.software.moacc.domain.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CategoryInitializer {

    private final CategoryRepository categoryRepository;

    @Bean
    public CommandLineRunner initCategories() {
        return args -> {
            if (categoryRepository.count() == 0) {
                categoryRepository.save(new Category("PHONE_CASE", "휴대폰 케이스"));
                categoryRepository.save(new Category("PROTECTION_FILM", "보호필름"));
                categoryRepository.save(new Category("SELFIE_STICK_MOUNT", "셀카봉/거치대"));
                categoryRepository.save(new Category("BATTERY", "보조배터리"));
                categoryRepository.save(new Category("CABLE_CHARGER", "케이블/충전기"));
                categoryRepository.save(new Category("MEMORY_CARD", "메모리카드"));
                categoryRepository.save(new Category("WATERPROOF_CASE", "방수팩/방수케이스"));
                categoryRepository.save(new Category("USIM", "USIM칩"));
                categoryRepository.save(new Category("OTHER_ACCESSORIES", "기타 액세서리"));
            }
        };
    }
}
