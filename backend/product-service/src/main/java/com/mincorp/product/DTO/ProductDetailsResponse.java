package com.mincorp.product.DTO;

public class ProductDetailsResponse {
	int productId;
	String title;
	String image;
	double price;
	float tax;
	public ProductDetailsResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProductDetailsResponse(int productId, String title, String image, double price, float tax) {
		super();
		this.productId = productId;
		this.title = title;
		this.image = image;
		this.price = price;
		this.tax = tax;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public float getTax() {
		return tax;
	}
	public void setTax(float tax) {
		this.tax = tax;
	}
	
	
}
