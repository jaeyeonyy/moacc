package com.sku.software.moacc.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewResponse {
    private List<ReviewResponse> reviews;
    private boolean hasPurchased;
}

