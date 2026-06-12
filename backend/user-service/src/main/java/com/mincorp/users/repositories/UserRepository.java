package com.mincorp.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mincorp.users.beans.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	public User findById(int id);
}
