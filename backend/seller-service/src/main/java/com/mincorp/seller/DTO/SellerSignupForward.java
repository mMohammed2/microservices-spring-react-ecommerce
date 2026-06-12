package com.mincorp.seller.DTO;

public class SellerSignupForward{
	String shopname,gstnumber;
	int id;
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SellerSignupForward(int id,String shopname, String gstnumber) {
		super();
		this.id=id;
		this.shopname = shopname;
		this.gstnumber = gstnumber;
	}
}

