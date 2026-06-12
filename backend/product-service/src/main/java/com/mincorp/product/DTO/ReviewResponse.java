package com.mincorp.product.DTO;

public class ReviewResponse {
	String description;
	int stars;
	public ReviewResponse(String description,  int stars) {
		super();
		this.description = description;
		this.stars = stars;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStars() {
		return stars;
	}
	public void setStars(int stars) {
		this.stars = stars;
	}
	public ReviewResponse() {
		super();
	}
	
}
