package com.mincorp.order.DTO;

public class OrderResponse {
	boolean success;
	String message;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public OrderResponse(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	public OrderResponse() {
		super();
	}
	
	
}
