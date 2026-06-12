package com.mincorp.order.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mincorp.order.beans.Return;

public interface ReturnRepository extends JpaRepository<Return, Integer>{
	List<Return> findByBuyerId(int buyerId);
}
