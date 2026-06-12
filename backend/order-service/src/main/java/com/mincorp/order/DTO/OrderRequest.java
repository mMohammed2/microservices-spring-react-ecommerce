package com.mincorp.order.DTO;

import java.util.Map;

public class OrderRequest {
	String address;
	String paymentStatus;
	Map<Integer,String> coupons;
	String transactionId;
	String paymentMethod;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Map<Integer,String> getCoupons() {
		return coupons;
	}
	public void setCoupons(Map<Integer,String> coupons) {
		this.coupons = coupons;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public OrderRequest(String address, String paymentStatus, Map<Integer,String> coupons, String paymentMethod,String transactionId) {
		super();
		this.address = address;
		this.paymentStatus = paymentStatus;
		this.coupons = coupons;
		this.paymentMethod = paymentMethod;
		this.transactionId = transactionId;
	}
	public OrderRequest() {
		super();	
	}
	
	
}
