package com.mincorp.auth.repositories;

import com.mincorp.auth.beans.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByEmailAndPassword(String email,String password);
	public User findByEmail(String email);
}
