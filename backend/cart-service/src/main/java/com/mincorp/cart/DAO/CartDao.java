package com.mincorp.cart.DAO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mincorp.cart.beans.Cart;
import com.mincorp.cart.repositories.CartRepository;
@Repository
public class CartDao {
	@Autowired
	CartRepository repo;
	public long getCount(int id) {
		return repo.getById(id).getProductIds().size();
	}
	
	public Cart addToCart(Cart c) {
		return repo.save(c);
	}
	public Cart getCart(int id) {
		return repo.getById(id);
	}
	
	public Boolean clearCart(int id) {
		Cart c = repo.getById(id);
		c.getProductIds().clear();
		return addToCart(c)!=null;
	}
	public void createCart(int id) {
		repo.save(new Cart(id,null));
	}	
}