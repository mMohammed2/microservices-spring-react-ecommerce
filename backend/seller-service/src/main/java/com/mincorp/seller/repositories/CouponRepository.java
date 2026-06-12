package com.mincorp.seller.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mincorp.seller.beans.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer>{
	public List<Coupon> findAllBySellerId(int id);
	public List<Coupon> findByCode(String code);
	public Coupon findById(int id);
	
}
