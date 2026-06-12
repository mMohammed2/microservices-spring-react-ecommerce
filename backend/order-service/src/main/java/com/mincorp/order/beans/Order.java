package com.mincorp.order.beans;

import java.util.Date;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@Column(name = "b_id")
	int bId;
	Date date;
	@ElementCollection
	List<Integer> packageIds;
	
	public Order() {
		super();
	}
	
	public Order(int bId, Date date, List<Integer> packageIds) {
		super();
		this.bId = bId;
		this.date = date;
		this.packageIds = packageIds;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getbId() {
		return bId;
	}
	public void setbId(int bId) {
		this.bId = bId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public List<Integer> getPackageIds() {
		return packageIds;
	}

	public void setPackageIds(List<Integer> packageIds) {
		this.packageIds = packageIds;
	}
	
	
}

