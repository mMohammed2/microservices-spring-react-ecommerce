package com.mincorp.product.DTO;

public class ProductResponse {
	boolean status;
	String message;

	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ProductResponse(boolean status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
}
