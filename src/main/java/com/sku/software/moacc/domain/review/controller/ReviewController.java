package com.sku.software.moacc.domain.review.controller;

import com.sku.software.moacc.domain.review.dto.ProductReviewResponse;
import com.sku.software.moacc.domain.review.dto.ReviewCreateRequest;
import com.sku.software.moacc.domain.review.dto.ReviewResponse;
import com.sku.software.moacc.domain.review.dto.ReviewUpdateRequest;
import com.sku.software.moacc.domain.review.service.ReviewService;
import com.sku.software.moacc.global.response.BaseResponse;
import com.sku.software.moacc.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<BaseResponse<ReviewResponse>> createReview(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReviewCreateRequest request) {
        ReviewResponse review = reviewService.createReview(userDetails.getUser().getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success("리뷰 작성 성공", review));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<BaseResponse<ProductReviewResponse>> getReviewsByProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ProductReviewResponse reviews = reviewService.getReviewsByProduct(productId, userDetails);
        return ResponseEntity.ok(BaseResponse.success("상품별 리뷰 조회 성공", reviews));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<BaseResponse<ReviewResponse>> updateReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReviewUpdateRequest request) {
        ReviewResponse review = reviewService.updateReview(reviewId, userDetails.getUser().getId(), request);
        return ResponseEntity.ok(BaseResponse.success("리뷰 수정 성공", review));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<BaseResponse<Void>> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUser().getId());
        return ResponseEntity.ok(BaseResponse.success("리뷰 삭제 성공", null));
    }
}
