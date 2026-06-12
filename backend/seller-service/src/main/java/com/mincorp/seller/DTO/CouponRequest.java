package com.mincorp.seller.DTO;

import java.util.Date;
import java.util.List;
public class CouponRequest {
	String code;
	float discount;
	int allowed;
	Date expiry;
	List<Integer> allowedProducts;
	
	public List<Integer> getAllowedProducts() {
		return allowedProducts;
	}
	public void setAllowedProducts(List<Integer> allowedProducts) {
		this.allowedProducts = allowedProducts;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
	}
	public int getAllowed() {
		return allowed;
	}
	public void setAllowed(int allowed) {
		this.allowed = allowed;
	}
	public Date getExpiry() {
		return expiry;
	}
	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	public CouponRequest(String code, float discount, int allowed, Date expiry,List<Integer> allowedProducts) {
		super();
		this.code = code;
		this.discount = discount;
		this.allowed = allowed;
		this.allowedProducts = allowedProducts;
		this.expiry = expiry;
	}
	public CouponRequest() {
		super();
	}
	
}
