package com.mincorp.cart.beans;

import java.util.Map;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Cart {
	@Id
	int id;
	@ElementCollection
	Map<Integer,Integer> productIds;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Map<Integer,Integer> getProductIds() {
		return productIds;
	}
	public void setProductIds(Map<Integer,Integer> productIds) {
		this.productIds = productIds;
	}
	public Cart(int id, Map<Integer,Integer> productIds) {
		super();
		this.id = id;
		this.productIds = productIds;
	}
	public Cart() {
		super();
	}
	
}
