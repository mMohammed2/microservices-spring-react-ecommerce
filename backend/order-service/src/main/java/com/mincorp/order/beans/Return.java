package com.mincorp.order.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "returns")
public class Return {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	int orderId;
	int productId;
	int buyerId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public Return(int orderId, int productId,int buyerId) {
		super();
		this.orderId = orderId;
		this.productId = productId;
		this.buyerId= buyerId;
	}
	public Return() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
