package com.mincorp.order.DTO;

public class SellerResponse {
	double price;
	double tax;
	double discount;
	String status;
	public SellerResponse(double price, double tax, double discount, String status) {
		super();
		this.price = price;
		this.tax = tax;
		this.discount = discount;
		this.status = status;
	}
	public SellerResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
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
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
