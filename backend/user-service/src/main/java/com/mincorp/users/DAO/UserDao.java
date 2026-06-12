package com.mincorp.users.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mincorp.users.beans.User;
import com.mincorp.users.repositories.UserRepository;

@Repository
public class UserDao {
	@Autowired
	UserRepository repo;
	
	public User registerUser(String name, long contact,List<String> Address) {
		User u = new User(name, contact,Address);
		return repo.save(u);
	}
	public User findById(int id) {
		return repo.findById(id);
	}
	public void update(User u) {
		repo.save(u);
	}
	
}
