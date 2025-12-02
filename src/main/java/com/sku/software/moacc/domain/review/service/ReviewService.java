package com.sku.software.moacc.domain.review.service;

import com.sku.software.moacc.domain.order.service.OrderService;
import com.sku.software.moacc.domain.product.entity.Product;
import com.sku.software.moacc.domain.product.repository.ProductRepository;
import com.sku.software.moacc.domain.review.dto.*;
import com.sku.software.moacc.domain.review.entity.Review;
import com.sku.software.moacc.domain.review.repository.ReviewRepository;
import com.sku.software.moacc.domain.user.entity.User;
import com.sku.software.moacc.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    @Transactional
    public ReviewResponse createReview(Long userId, ReviewCreateRequest request) {
        if (!orderService.hasUserPurchasedProduct(userId, request.getProductId())) {
            throw new IllegalStateException("이 상품을 구매한 사용자만 리뷰를 작성할 수 있습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .content(request.getContent())
                .build();

        Review savedReview = reviewRepository.save(review);
        return ReviewResponse.fromEntity(savedReview);
    }

    public ProductReviewResponse getReviewsByProduct(Long productId, UserDetails userDetails) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        boolean hasPurchased = false;
        if (userDetails != null) {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
            log.info("사용자 ID: {}, 상품 ID: {} 에 대한 구매 여부 확인 시작", user.getId(), productId);
            hasPurchased = orderService.hasUserPurchasedProduct(user.getId(), productId);
            log.info("구매 여부 확인 결과: {}", hasPurchased);
        }

        List<ReviewResponse> reviews = product.getReviews().stream()
                .filter(review -> !review.isDeleted())
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());

        return ProductReviewResponse.builder()
                .reviews(reviews)
                .hasPurchased(hasPurchased)
                .build();
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, Long userId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalStateException("자신이 작성한 리뷰만 수정할 수 있습니다.");
        }

        review.update(request.getRating(), request.getContent());
        return ReviewResponse.fromEntity(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalStateException("자신이 작성한 리뷰만 삭제할 수 있습니다.");
        }
        review.delete();
    }
}
