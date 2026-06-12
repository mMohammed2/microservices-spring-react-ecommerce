package com.mincorp.users.DTO;

import java.util.List;

public class UserResponse {
	String name,email;
	long contact;	
	List<String> address;
	String message;
	public UserResponse(String name, String email, long contact, List<String> address, String message
			) {
		super();
		this.name = name;
		this.email = email;
		this.contact = contact;
		this.address = address;
		this.message = message;
	}	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getContact() {
		return contact;
	}
	public void setContact(long contact) {
		this.contact = contact;
	}
	public List<String> getAddress() {
		return address;
	}
	public void setAddress(List<String> address) {
		this.address = address;
	}
	
}
