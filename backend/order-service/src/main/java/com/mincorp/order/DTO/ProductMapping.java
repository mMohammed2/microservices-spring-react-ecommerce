package com.mincorp.order.DTO;

public class ProductMapping {
	int productId;
	ProductOrder p;
	int couponId;
	float discount;
	public ProductOrder getP() {
		return p;
	}
	public void setP(ProductOrder p) {
		this.p = p;
	}
	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
	}
	
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public ProductMapping(int productId,ProductOrder p, int couponId, float discount) {
		super();
		this.productId=productId;
		this.p = p;
		this.couponId = couponId;
		this.discount = discount;
	}
}
