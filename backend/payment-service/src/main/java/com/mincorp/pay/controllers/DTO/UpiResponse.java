package com.mincorp.pay.controllers.DTO;

public class UpiResponse {
	String QRCode;

	public String getQRCode() {
		return QRCode;
	}

	public void setQRCode(String qRCode) {
		QRCode = qRCode;
	}

	public UpiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UpiResponse(String qRCode) {
		super();
		QRCode = qRCode;
	}
	
}
