package com.mincorp.order.DAO;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mincorp.order.beans.Order;
import com.mincorp.order.repositories.OrderRepository;

import jakarta.transaction.Transactional;

@Repository
public class OrderDao {
	@Autowired
	OrderRepository repo;
	public Order addOrder(Order o) {
		return repo.save(o);
	}
	public List<Order> getOrder(int id) {
		return repo.findBybId(id);
	}
	@Transactional
	public void deleteOrder(Order o) {
		repo.delete(o);
	}
	public Date getDate(int pId) {
		return repo.findByPackageId(pId).getDate();
	}
	public void removePackage(int pId) {
		Order o = repo.findByPackageId(pId);
		List<Integer> packs = o.getPackageIds();
		boolean result = packs.remove(Integer.valueOf(pId));
		o.setPackageIds(packs);
		if(o.getPackageIds().isEmpty()) {
			repo.delete(o);
		} else 
		repo.save(o);
	}
	public Order getOrderByOrderId(int id) {
		return repo.getById(id);
	}
}

