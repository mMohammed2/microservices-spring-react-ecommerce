package com.mincorp.auth.DTO;

public class UserRequest {
	long contact;
	String name;
	public UserRequest(String name,long contact) {
		this.name = name;
		this.contact = contact;
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
