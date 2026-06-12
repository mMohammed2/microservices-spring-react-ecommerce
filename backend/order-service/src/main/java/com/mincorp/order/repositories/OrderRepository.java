package com.mincorp.order.repositories;

import com.mincorp.order.beans.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer>{
	public List<Order> findBybId(int id);
	@Query("SELECT o FROM Order o JOIN o.packageIds p WHERE p = :packageId")
	Order findByPackageId(@Param("packageId") int packageId);	
}
