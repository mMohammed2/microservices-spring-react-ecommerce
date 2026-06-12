package com.mincorp.seller.controller.DTO;

public class CouponValidateRequest {
	int productId;
	String code;
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public CouponValidateRequest(int productId, String code) {
		super();
		this.productId = productId;
		this.code = code;
	}
	public CouponValidateRequest() {
	}
	
}
