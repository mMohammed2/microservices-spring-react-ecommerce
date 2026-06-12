package com.mincorp.order.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mincorp.order.beans.Return;
import com.mincorp.order.repositories.ReturnRepository;

@Repository
public class ReturnDao {
	@Autowired
	private ReturnRepository repo;
	public void addReturn(Return r) {
		repo.save(r);
	}
	public List<Return> getMyReturns(int buyerId){
		return repo.findByBuyerId(buyerId);
	}
}
