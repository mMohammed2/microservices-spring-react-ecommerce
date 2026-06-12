package com.mincorp.review.DTO;

public class ReviewRequest {
	int productId;
	int rating;
	String review;
	public ReviewRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public ReviewRequest(int productId, int rating, String review) {
		super();
		this.productId = productId;
		this.rating = rating;
		this.review = review;
	}

	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	
}