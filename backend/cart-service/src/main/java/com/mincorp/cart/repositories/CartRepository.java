package com.mincorp.cart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mincorp.cart.beans.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
	public void deleteAllById(int id);
}

