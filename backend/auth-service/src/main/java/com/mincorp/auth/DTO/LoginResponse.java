package com.mincorp.auth.DTO;

public class LoginResponse {
	String token;
	boolean isSeller;
	
	
	public boolean isSeller() {
		return isSeller;
	}
	public void setSeller(boolean isSeller) {
		this.isSeller = isSeller;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LoginResponse(String token,boolean isSeller) {
		super();
		this.token = token;
		this.isSeller = isSeller;
	}
	
}
