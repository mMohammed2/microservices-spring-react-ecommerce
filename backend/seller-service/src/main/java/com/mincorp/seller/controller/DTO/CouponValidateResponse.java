package com.mincorp.seller.controller.DTO;

public class CouponValidateResponse {
	boolean valid;
	float discount;
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
	}
	public CouponValidateResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CouponValidateResponse(boolean valid, float discount) {
		super();
		this.valid = valid;
		this.discount = discount;
	}
	
}
