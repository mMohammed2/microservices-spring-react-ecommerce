package com.mincorp.review.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
    private int productId;
	int buyerId;
	int stars;
	String description;
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}
	public int getStars() {
		return stars;
	}
	public void setStars(int stars) {
		this.stars = stars;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Review(int productId, int buyerId, int stars, String description) {
		super();
		this.productId = productId;
		this.buyerId = buyerId;
		this.stars = stars;
		this.description = description;
	}
	public Review() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
