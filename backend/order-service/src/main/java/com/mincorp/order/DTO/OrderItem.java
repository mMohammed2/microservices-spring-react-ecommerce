package com.mincorp.order.DTO;

public class OrderItem {
	int productId;
	String title;
	String image;
	double price;
	int quantity;
	float tax;
	String couponCode;
	float couponDiscountPercent;
	public OrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderItem(int productId, String title, String image, double price, int quantity, float tax,
			String couponCode, float couponDiscountPercent) {
		super();
		this.productId = productId;
		this.title = title;
		this.image = image;
		this.price = price;
		this.quantity = quantity;
		this.tax = tax;
		this.couponCode = couponCode;
		this.couponDiscountPercent = couponDiscountPercent;
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
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public float getCouponDiscountPercent() {
		return couponDiscountPercent;
	}
	public void setCouponDiscountPercent(float couponDiscountPercent) {
		this.couponDiscountPercent = couponDiscountPercent;
	}
	
}
