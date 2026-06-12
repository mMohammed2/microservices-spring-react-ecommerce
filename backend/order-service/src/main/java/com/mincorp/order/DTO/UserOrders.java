package com.mincorp.order.DTO;

import java.util.Date;
import java.util.List;

public class UserOrders {
	int id;
	String status;
	Date date;
	String paymentMethod;
	String paymentStatus;
	String address;
	List<OrderItem> item;
	double subtotal;
	double tax;
	double discount;
	double total;
	Date delivered;
	public UserOrders() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserOrders(int id, String status, Date date, String paymentMethod, String paymentStatus, String address,
			List<OrderItem> item, double subtotal, double tax, double discount, double total,Date delivered) {
		super();
		this.id = id;
		this.status = status;
		this.date = date;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.address = address;
		this.item = item;
		this.subtotal = subtotal;
		this.tax = tax;
		this.discount = discount;
		this.total = total;
		this.delivered = delivered;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<OrderItem> getItem() {
		return item;
	}
	public void setItem(List<OrderItem> item) {
		this.item = item;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public Date getDelivered() {
		return delivered;
	}
	public void setDelivered(Date delivered) {
		this.delivered = delivered;
	}
	
	
}
