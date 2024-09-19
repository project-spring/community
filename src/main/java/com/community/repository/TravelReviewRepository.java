package com.community.repository;

import com.community.domain.TravelReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelReviewRepository extends
        JpaRepository<TravelReview, Long> {
}
