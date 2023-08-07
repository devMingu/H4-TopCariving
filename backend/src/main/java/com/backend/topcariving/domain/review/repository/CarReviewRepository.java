package com.backend.topcariving.domain.review.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.topcariving.domain.review.entity.CarReview;

@Repository
public interface CarReviewRepository extends CrudRepository<CarReview, Long> {
}