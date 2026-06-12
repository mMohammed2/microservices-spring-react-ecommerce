package com.mincorp.users.DTO;
import java.util.List;
public class AddressRequest {
	List<String> address;

	public List<String> getAddress() {
		return address;
	}

	public void setAddress(List<String> address) {
		this.address = address;
	}

	public AddressRequest(List<String> address) {
		super();
		this.address = address;
	}

	public AddressRequest() {
		super();
	}
	
}
