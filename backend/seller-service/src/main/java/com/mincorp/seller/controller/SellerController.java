package com.mincorp.seller.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mincorp.seller.DAO.CouponDao;
import com.mincorp.seller.DAO.SellerDao;
import com.mincorp.seller.DTO.CouponRequest;
import com.mincorp.seller.DTO.SellerSignupForward;
import com.mincorp.seller.beans.Coupon;
import com.mincorp.seller.beans.Seller;
import com.mincorp.seller.controller.DTO.CouponValidateRequest;
import com.mincorp.seller.controller.DTO.CouponValidateResponse;
import com.mincorp.seller.services.JWTService;

@RestController
@RequestMapping("/api/seller")
public class SellerController {
	@Autowired
	SellerDao sdao;
	@Autowired
	CouponDao cdao;
	@Autowired
	JWTService jwtservice;
	@PostMapping("/registerSeller")
	public ResponseEntity<String> register(@RequestBody SellerSignupForward request){
		Seller s = new Seller(request.getId(),request.getShopname(),request.getGstnumber());
		
		s = sdao.addSeller(s);
		if(s !=null) {
			return ResponseEntity.ok("Seller registration successful!");
		}
		return ResponseEntity.status(500).body("Cant register seller!");
		
	}
	
	@GetMapping("/coupons")
	public List<Coupon> getCoupons(@RequestHeader(name = "Authorization") String authHeader){
		int id = jwtservice.getClaimsBody(authHeader.substring(7)).get("id",Integer.class);
		return cdao.getSellerCoupons(id);
	
	}
	
	@PostMapping("/addCoupon")
	public Boolean addCoupon(@RequestHeader(name = "Authorization") String authHeader,@RequestBody CouponRequest request ){
		int id = jwtservice.getClaimsBody(authHeader.substring(7)).get("id",Integer.class);
		Coupon c = new Coupon(request.getCode(), request.getDiscount(), id, request.getExpiry(), request.getAllowed() ,0,request.getAllowedProducts());
		return cdao.addCoupon(c)!=null;
	}
	@DeleteMapping("/deleteCoupon/{id}")
	public void deleteCoupon(@PathVariable int id){
		cdao.delete(id);
	}
	
	@PutMapping("/updateCoupon/{id}")
	public void update(@PathVariable int id,@RequestBody CouponRequest request) {
		cdao.update(id, request.getCode(), request.getDiscount(), request.getExpiry(), request.getAllowedProducts(),request.getAllowed());
	}
	
	@PostMapping("/validate")
	public ResponseEntity<CouponValidateResponse> validate(@RequestHeader(name = "Authorization") String auth,@RequestBody CouponValidateRequest request) {
		int id = jwtservice.getClaimsBody(auth.substring(7)).get("id",Integer.class);
		int pId = request.getProductId();
		String code = request.getCode();
		Coupon c = cdao.isValid(pId,code);
		if(c!=null) {
			c.setUsed(c.getUsed()+1);
			cdao.addCoupon(c);
			return ResponseEntity.ok(new CouponValidateResponse(true, c.getDiscount()));
		}else {
			return ResponseEntity.status(500).body(new CouponValidateResponse(false,0));
		}
		
	}
	@GetMapping("/getDiscount")
	public Map<Integer,Float> getDiscount(
	        @RequestParam String code,
	        @RequestParam int id) {
	    return cdao.getDiscount(code, id);
	}
	
	@GetMapping("/getCouponName/{id}")
	public Object[] getCouponName(@PathVariable int id) {
	    Coupon c = cdao.getCoupon(id);
	    return new Object[] {c.getCode(),c.getDiscount()};
	}
	
	
}
