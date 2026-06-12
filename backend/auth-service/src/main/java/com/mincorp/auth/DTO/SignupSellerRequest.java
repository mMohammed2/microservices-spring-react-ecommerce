package com.mincorp.auth.DTO;

public class SignupSellerRequest {
	String shopname,gstnumber;
	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getGstnumber() {
		return gstnumber;
	}

	public void setGstnumber(String gstnumber) {
		this.gstnumber = gstnumber;
	}
	public SignupSellerRequest(String shopname, String gstnumber) {
		super();
		this.shopname = shopname;
		this.gstnumber = gstnumber;
	}
}

