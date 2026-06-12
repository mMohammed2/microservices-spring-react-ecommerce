package com.mincorp.seller.DAO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mincorp.seller.beans.Coupon;
import com.mincorp.seller.repositories.CouponRepository;

@Repository
public class CouponDao {
	@Autowired
	CouponRepository repo;
	public List<Coupon> getSellerCoupons(int id){
		return repo.findAllBySellerId(id);
	}
	public Coupon addCoupon(Coupon c) {
		return repo.save(c);
	}
	public Coupon getCoupon(int id) {
		return repo.findById(id);
	}
	public void delete(int id) {
		repo.deleteById(id);
	}
	
	public void update(int id,String code,float discount,Date expiry,List<Integer> allowedProducts,int allowed) {
		Coupon c =repo.getById(id);
		c.setAllowed(allowed);
		c.setAllowedProducts(allowedProducts);
		c.setCode(code);
		c.setDiscount(discount);
		c.setExpiry(expiry);
		repo.save(c);
	}
	
	public Coupon isValid(int pId,String code) {
		List<Coupon> c = repo.findByCode(code);
		for(Coupon coupon: c) {
			if(coupon.getExpiry() == null || !coupon.getExpiry().before(new Date())) {
				System.out.println("The coupon has been expired");
				if(coupon.getAllowed() > coupon.getUsed() || coupon.getAllowed() == 0 && coupon.getAllowedProducts().size()==0 || coupon.getAllowedProducts().contains(pId)) {
					System.out.println("The coupon has been used or not allowed on the given product");
					return coupon;
				}
			}
		}
		return null;
	}
	public Map<Integer,Float> getDiscount(String code,int id) {
		Map<Integer,Float> map = new HashMap<>();
		Coupon c = repo.findByCode(code).stream().filter(x -> { return x.getAllowedProducts().contains(id) || x.getAllowedProducts().size()==0; }).toList().get(0);
		map.put(c.getId(), c.getDiscount());
		return map;
	}
}

