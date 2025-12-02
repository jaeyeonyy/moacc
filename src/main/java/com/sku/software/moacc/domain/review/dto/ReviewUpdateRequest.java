package com.sku.software.moacc.domain.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequest {
    private BigDecimal rating;
    private String content;
}
