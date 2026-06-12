package com.mincorp.order.DTO;

public class ProductOrder {
	double price,tax;
	int quantity,sellerId;
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getSellerId() {
		return sellerId;
	}
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
	public ProductOrder(double price, double tax, int quantity, int sellerId) {
		super();
		this.price = price;
		this.tax = tax;
		this.quantity = quantity;
		this.sellerId = sellerId;
	}
	public ProductOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
