package com.mincorp.auth.DTO;

public class SignupResponse {
	String message;
	String token;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public SignupResponse(String message, String token) {
		super();
		this.message = message;
		this.token = token;
	}
	
}
