package com.mincorp.review.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mincorp.review.beans.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	public Review findByProductIdAndBuyerId(int productId,int buyerId);
	public List<Review> findByProductId(int productId);
}
