package com.mincorp.review.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mincorp.review.beans.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	public Review findByProductIdAndBuyerId(int productId,int buyerId);
}
