package com.mincorp.auth.DTO;

public class SignupRequest {
	String email;
	String password;
	long contact;
	String name;
	public SignupRequest(String email, String password,String name,long contact) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.contact = contact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getContact() {
		return contact;
	}
	public void setContact(long contact) {
		this.contact = contact;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
