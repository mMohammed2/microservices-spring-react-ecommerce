package com.mincorp.seller.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Seller{
	 private String shopName;
	 private String gstNumber;
	 @Id
	 private int id;
	 
	 public String getShopName() {
		return shopName;
	}
	 public void setShopName(String shopName) {
		 this.shopName = shopName;
	 }
	 public String getGstNumber() {
		 return gstNumber;
	 }
	 public void setGstNumber(String gstNumber) {
		 this.gstNumber = gstNumber;
	 }
	 public Seller(int id,String shopname,String gstnumber) {
			this.shopName = shopname;
			this.gstNumber = gstnumber;
			this.id= id;
	 }
	 public Seller() {
			super();
	 }
	 public int getId() {
		 return id;
	 }
	 public void setId(int id) {
		 this.id = id;
	 }
	 
	 
}
