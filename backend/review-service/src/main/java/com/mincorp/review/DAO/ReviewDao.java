package com.mincorp.review.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mincorp.review.beans.Review;
import com.mincorp.review.repositories.ReviewRepository;

@Repository
public class ReviewDao {
	@Autowired
	ReviewRepository repo;

	public void addReview(Review review) {
		repo.save(review);	
	}
	public Review getReview(int id,int bId) {
		return repo.findByProductIdAndBuyerId(id, bId);
	}
	
	public List<Review> getReviewById(int id) {
		return repo.findByProductId(id);
	}
}
