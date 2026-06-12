package com.mincorp.cart.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mincorp.cart.DAO.CartDao;
import com.mincorp.cart.beans.Cart;
import com.mincorp.cart.services.JWTService;

@RestController
@RequestMapping("/api/cart")
public class Controller {
	@Autowired
	CartDao cdao;
	@Autowired
	JWTService jwtservice;
	@GetMapping("/count")
	public long count(@RequestHeader(name = "Authorization",required = false) String authHeader){
		if(authHeader==null || !authHeader.startsWith("Bearer ")) {
			return 0;
		}
		
		String token = authHeader.substring(7);
		int id = jwtservice.getClaimsBody(token).get("id",Integer.class);
		
		return	cdao.getCount(id);
	}
	
	
	@PostMapping("/add/{id}")
	public Cart add(@PathVariable int id,@RequestHeader(name = "Authorization",required = false) String authHeader){
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			return null;
		}
		String token = authHeader.substring(7);
		int user_id = jwtservice.getClaimsBody(token).get("id",Integer.class);		
		Cart c = cdao.getCart(user_id);
		Map<Integer,Integer> prods = c.getProductIds();
		if(prods == null) {
			prods = new HashMap<>();
		}
		prods.put(id,prods.getOrDefault(id, 0)+1);
		c.setProductIds(prods);
		return cdao.addToCart(c);
	}
	
	@GetMapping("/details")
	public Map<Integer,Integer> getData(@RequestHeader(name = "Authorization", required = false) String authHeader){
		if(authHeader == null || !authHeader.startsWith("Bearer")) {
			return null;
		}
		String token = authHeader.substring(7);
		
		int user_id = jwtservice.getClaimsBody(token).get("id",Integer.class);
		Cart c = cdao.getCart(user_id);
		return c.getProductIds();
	}
	
	@PutMapping("/removeFromCart/{id}")
	public Cart removeFromCart(@PathVariable int id,@RequestHeader(name = "Authorization", required = false) String authHeader){
		if(authHeader == null || !authHeader.startsWith("Bearer")) {
			return null;
		}
		String token = authHeader.substring(7);
		
		int user_id = jwtservice.getClaimsBody(token).get("id",Integer.class);
		Cart c = cdao.getCart(user_id);
		Map<Integer,Integer> productIds = c.getProductIds();
		productIds.put(id, productIds.get(id)-1);
		if(productIds.get(id)==0) {
			productIds.remove(id);
		}
		c.setProductIds(productIds);
		return cdao.addToCart(c);
	}
	
	@PutMapping("emptyCart")
	public Boolean empty(@RequestHeader(name="Authorization") String authHeader) {
		int id = jwtservice.getClaimsBody(authHeader.substring(7)).get("id",Integer.class);
		return cdao.clearCart(id);
	}
	@PostMapping("createUser")
	public boolean createCart(@RequestBody Integer id){
		cdao.createCart(id);
		return true;
	}
}
