package com.mincorp.auth.beans;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_auth")
public class User {
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(int id,String email, String password,String role) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.role= role;
	}
	
	@Id
	int id;
	@Column(unique = true)
	String email;
	String password,role;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
