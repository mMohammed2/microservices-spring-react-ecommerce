package com.mincorp.seller.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mincorp.seller.beans.Seller;
import com.mincorp.seller.repositories.SellerRepository;
@Repository
public class SellerDao {
	@Autowired
	SellerRepository repo;
	
	public Seller addSeller(Seller seller) {
		return repo.save(seller);
	}
	
	public Seller getSellerById(int id) {
		return repo.findById(id).get();
	}
}
