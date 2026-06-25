package com.mincorp.cache.DTO;

public class CachedProduct {
	int id;
	String name;
	float tax;
	double price;
	int totalRating;
	int totalSold;
	String description;
	int quantity;
	String images;	
	int totalReviews;
	String type;
	private int sellerId;
	
	public int getSellerId() {
		return sellerId;
	}
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
	public int getTotalRating() {
		return totalRating;
	}
	public void setTotalRating(int totalRating) {
		this.totalRating = totalRating;
	}
	public int getTotalSold() {
		return totalSold;
	}
	public void setTotalSold(int totalSold) {
		this.totalSold = totalSold;
	}
	public int getTotalReviews() {
		return totalReviews;
	}
	public void setTotalReviews(int totalReviews) {
		this.totalReviews = totalReviews;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}  
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public float getTax() {
		return tax;
	}
	public void setTax(float tax) {
		this.tax = tax;
	}
	public CachedProduct(String name, double price, int totalRating, int totalSold, String description, String images,
			int totalReviews, String type, int quantity,int sellerId,float tax) {
		super();
		this.name = name;
		this.price = price;
		this.totalRating = totalRating;
		this.totalSold = totalSold;
		this.description = description;
		this.images = images;
		this.totalReviews = totalReviews;
		this.type = type;
		this.sellerId = sellerId;
		this.quantity = quantity;
		this.tax = tax;
	}
	public CachedProduct() {
		super();
	}
	
}
