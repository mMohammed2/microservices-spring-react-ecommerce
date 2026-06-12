package com.mincorp.order.DTO;

public class StatusRequest {
	String status;

	public StatusRequest(String status) {
		super();
		this.status = status;
	}

	public StatusRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
