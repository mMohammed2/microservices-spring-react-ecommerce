package com.mincorp.pay.controllers.DTO;

public class UpiRequest {
	float amount;

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public UpiRequest(float amount) {
		super();
		this.amount = amount;
	}

	public UpiRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
