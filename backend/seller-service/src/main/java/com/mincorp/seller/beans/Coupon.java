package com.mincorp.seller.beans;

import java.util.Date;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String code;
	float discount;
	int sellerId;
	Date expiry;
	@ElementCollection
	List<Integer> allowedProducts;
	int allowed;
	int used;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getSeller_id() {
		return sellerId;
	}
	public void setSeller_id(int seller_id) {
		this.sellerId = seller_id;
	}
	public Date getExpiry() {
		return expiry;
	}
	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	public int getAllowed() {
		return allowed;
	}
	public void setAllowed(int allowed) {
		this.allowed = allowed;
	}
	public Coupon(String code, float discount, int sellerId, Date expiry, int allowed, int used,List<Integer> allowedProducts) {
		super();
		this.code = code;
		this.discount = discount;
		this.sellerId = sellerId;
		this.expiry = expiry;
		this.allowed = allowed;
		this.used = used;
		this.allowedProducts = allowedProducts;
	}
	public int getUsed() {
		return used;
	}
	public void setUsed(int used) {
		this.used = used;
	}
	
	public int getSellerId() {
		return sellerId;
	}
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
	public List<Integer> getAllowedProducts() {
		return allowedProducts;
	}
	public void setAllowedProducts(List<Integer> allowedProducts) {
		this.allowedProducts = allowedProducts;
	}
	public Coupon() {
		super();
	}

	
	
}
