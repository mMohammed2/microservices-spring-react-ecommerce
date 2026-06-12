package com.mincorp.auth.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mincorp.auth.beans.User;
import com.mincorp.auth.repositories.UserRepository;

@Repository
public class UserDao {
	@Autowired
	UserRepository repo;
	
	public User getUser(String email,String password) {
		return repo.findByEmailAndPassword(email,password);
	}
	public User addUser(User u) {
		return repo.save(u);
	}
	public User getUserByUsername(String email) {
		return repo.findByEmail(email);
	}
	public void deleteUser(User u) {
		repo.delete(u);
	}
	public boolean changePassword(String u,String o,String n) {
		User user = getUser(u, o);
		if(user == null) {
			return false;
		}
		user.setPassword(n);
		user = addUser(user);
		return user!=null;
	}
}
