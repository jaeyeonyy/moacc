package com.sku.software.moacc.domain.review.repository;

import com.sku.software.moacc.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}

