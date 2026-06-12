package com.mincorp.order.beans;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Package {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String paymentStatus;
	String paymentMethod;
	String address;
	String status;
	int sellerId;
	double price;
	Date delivered;
	public Date getDelivered() {
		return delivered;
	}
	public void setDelivered(Date delivered) {
		this.delivered = delivered;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	@ElementCollection
	Map<Integer,Integer> productIds;
	@ElementCollection
	Map<Integer,Integer> couponIds;	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Map<Integer, Integer> getProductIds() {
		return productIds;
	}
	public void setProductIds(Map<Integer, Integer> productIds) {
		this.productIds = productIds;
	}
	public Map<Integer, Integer> getCouponIds() {
		return couponIds;
	}
	public void setCouponIds(Map<Integer, Integer> couponIds) {
		this.couponIds = couponIds;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getSellerId() {
		return sellerId;
	}
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
	public Package(String paymentStatus, String paymentMethod, String address, String status,
			int sellerId, Map<Integer, Integer> productIds, Map<Integer, Integer> couponIds,double price) {
		super();
		this.paymentStatus = paymentStatus;
		this.paymentMethod = paymentMethod;
		this.address = address;
		this.status = status;
		this.sellerId = sellerId;
		this.productIds = productIds;
		this.couponIds = couponIds;
		this.price = price;
	}
	public Package() {
		super();
	}
	
}
