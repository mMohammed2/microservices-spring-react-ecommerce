package com.mincorp.order.controllers;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.RestTemplate;

import com.mincorp.order.DAO.OrderDao;
import com.mincorp.order.DAO.PackageDao;
import com.mincorp.order.DAO.ReturnDao;
import com.mincorp.order.DTO.OrderItem;
import com.mincorp.order.DTO.OrderRequest;
import com.mincorp.order.DTO.OrderResponse;
import com.mincorp.order.DTO.ProductDetailsResponse;
import com.mincorp.order.DTO.ProductMapping;
import com.mincorp.order.DTO.ProductOrder;
import com.mincorp.order.DTO.SellerResponse;
import com.mincorp.order.DTO.StatusRequest;
import com.mincorp.order.DTO.UserOrders;
import com.mincorp.order.beans.Order;
import com.mincorp.order.beans.Package;
import com.mincorp.order.beans.Return;
import com.mincorp.order.services.JWTService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
@RestController
@RequestMapping("/api/orders")
public class Controller {
	@Autowired
	JWTService service;
	@Autowired
	OrderDao odao;
	@Autowired
	PackageDao pdao;
	@Autowired
	ReturnDao rdao;
	@Autowired
	private RestTemplate restTemplate;
	@PostMapping("/create")
	public ResponseEntity<OrderResponse> createOrder(
	        @RequestHeader(name = "Authorization") String authHeader,
	        @RequestBody OrderRequest request) {

	    int id = service.getClaimsBody(authHeader.substring(7)).get("id", Integer.class);
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", authHeader);
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> entity = new HttpEntity<>(headers); 
	    ResponseEntity<Map<Integer, Integer>> response =
	            restTemplate.exchange(
	                    "http://CART-SERVICE/api/cart/details",
	                    HttpMethod.GET,
	                    entity,
	                    new ParameterizedTypeReference<Map<Integer, Integer>>() {}
	            );
	    Map<Integer, Integer> cart = response.getBody();
	    if (cart == null || cart.isEmpty()) {
	        return ResponseEntity.badRequest()
	                .body(new OrderResponse(false, "Cart is empty"));
	    }	    
	    Map<Integer, List<ProductMapping>> sellerMap = new HashMap<>();
	    for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
	        ProductOrder product =
	                restTemplate.exchange(
	                        "http://PRODUCT-SERVICE/api/products/getPrice?pId=" + entry.getKey()+"&q="+entry.getValue(),
	                        HttpMethod.GET,
	                        entity,
	                        ProductOrder.class
	                ).getBody();
	        float discountRate = 0;
            int couponId = -1;
	        if (request.getCoupons().containsKey(entry.getKey())) {
	            ResponseEntity<Map<Integer, Float>> discount =
	                    restTemplate.exchange(
	                            "http://SELLER-SERVICE/api/seller/getDiscount?code="
	                                    + request.getCoupons().get(entry.getKey())
	                                    + "&id="
	                                    + entry.getKey(),
	                            HttpMethod.GET,
	                            entity,
	                            new ParameterizedTypeReference<Map<Integer, Float>>() {}
	                    );
	            for (Map.Entry<Integer, Float> entryOfCoupon : discount.getBody().entrySet()) {
	                couponId = entryOfCoupon.getKey();
	                discountRate = entryOfCoupon.getValue();
	            }
	        }
	        
	       List<ProductMapping> mapper = sellerMap.computeIfAbsent(product.getSellerId(),key->new ArrayList<>());
	       mapper.add(new ProductMapping(entry.getKey(),product,couponId,discountRate));
	    }

	    try {
	        ResponseEntity<Boolean> empty =
	                restTemplate.exchange(
	                        "http://CART-SERVICE/api/cart/emptyCart",
	                        HttpMethod.PUT,
	                        entity,
	                        Boolean.class
	                );

	        if (empty.getBody() == null || !empty.getBody()) {
	            return ResponseEntity.status(500)
	                    .body(new OrderResponse(false, "Failed to empty cart"));
	        }

	    } catch (Exception ex) {
	        return ResponseEntity.status(500)
	                .body(new OrderResponse(false, "Cart service unavailable"));
	    }
	    List<Integer> packageIds = new ArrayList<>();
	    for(Map.Entry<Integer, List<ProductMapping>> entry :
	    	sellerMap.entrySet()
	    	) {
	    	double total = 0;
	    	int sellerId = entry.getKey();
	    	Map<Integer,Integer> productIds = new HashMap<>();
	    	Map<Integer,Integer> couponIds = new HashMap<>();
	    	List<ProductMapping> list = entry.getValue();
	    	for(ProductMapping pm : list) {
	    		int productId = pm.getProductId();
	    		ProductOrder p = pm.getP();
	    		int cId = pm.getCouponId();
	    		float discount = pm.getDiscount();
	    		double price = p.getPrice();
	    		int quantity = p.getQuantity();
	    		double tax = p.getTax();
	    		productIds.put(productId, quantity);
	    		if(cId!=-1)
	    		couponIds.put(productId, cId);
	    		total += price*(1-discount/100.0)*(1+tax/100.0);
	    	}
	    	com.mincorp.order.beans.Package p = new com.mincorp.order.beans.Package(request.getPaymentStatus(), request.getPaymentMethod(), request.getAddress(), "PLACED",
	    			sellerId, productIds, couponIds,total);
	    	p = pdao.createPackage(p);
	    	
	    	packageIds.add(p.getId());
	    }
	    	

	    Order order = new Order(id,new Date(),packageIds);
	    if ((order = odao.addOrder(order))!=null) {
	    	return ResponseEntity.ok(new OrderResponse(true, "Success"));
	    	
	    }
	    
	    return ResponseEntity.status(500).body(new OrderResponse(false, "Order creation failed"));
	}
	@GetMapping("/user")
	public ResponseEntity<List<UserOrders>> user(@RequestHeader(name = "Authorization") String authHeader){
		List<UserOrders> userOrders = new ArrayList<>();
		try {
			int id = service.getClaimsBody(authHeader.substring(7)).get("id", Integer.class);
			List<Order> orders = odao.getOrder(id);
			UserOrders user=null;	
			HttpHeaders headers = new HttpHeaders();
		    headers.set("Authorization", authHeader);
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    HttpEntity<String> entity = new HttpEntity<>(headers);
			for(Order o: orders) {
				List<Integer> packageIds = o.getPackageIds();
			    double subtotal = 0;
			    double tax = 0;
			    double total = 0;
				List<OrderItem> list = new ArrayList<>();
				com.mincorp.order.beans.Package p=null;
				for(int packageId:packageIds) {
					p = pdao.getPackage(packageId);
					for(Map.Entry<Integer,Integer> entry:p.getProductIds().entrySet()) {
						ResponseEntity<ProductDetailsResponse> products =
				                restTemplate.exchange(
				                        "http://PRODUCT-SERVICE/api/products/getProduct/"+entry.getKey(),
				                        HttpMethod.GET,
				                        entity,
				                        ProductDetailsResponse.class
				                );
						int couponID = p.getCouponIds().getOrDefault(entry.getKey(),-1);
						ProductDetailsResponse response = products.getBody();
						subtotal+=response.getPrice();
						Object[] coupon= new Object[] {"",0};
						if(couponID!=-1) {
						coupon =
				                restTemplate.exchange(
				                        "http://SELLER-SERVICE/api/seller/getCouponName/"+couponID,
				                        HttpMethod.GET,
				                        entity,
				                        Object[].class
				                ).getBody();
							tax+=response.getPrice()*(1-Float.parseFloat(String.valueOf(coupon[1]))/100.0)*(response.getTax()/100.0);
						}else {
							tax+=response.getPrice()*(response.getTax()/100.0);
						}
						OrderItem item = new OrderItem(entry.getKey(), response.getTitle(), response.getImage(), response.getPrice(), entry.getValue(), response.getTax(), (String)coupon[0], Float.parseFloat(String.valueOf(coupon[1])));
						list.add(item);	
					}
					total+=p.getPrice();
					
				}
				user = new UserOrders(o.getId(),p.getStatus(), o.getDate(), p.getPaymentMethod(), p.getPaymentStatus(), p.getAddress(), list, subtotal, tax, (subtotal + tax)-total , total,p.getDelivered());
				userOrders.add(user);
			}
			return ResponseEntity.ok(userOrders);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to fetch the orders");
			return ResponseEntity.status(500).body(userOrders);
		}
	}
	
	@GetMapping("/seller")
	public ResponseEntity<List<UserOrders>> seller(@RequestHeader(name = "Authorization") String authHeader){
		List<UserOrders> userOrders = new ArrayList<>();
		try {
			int id = service.getClaimsBody(authHeader.substring(7)).get("id", Integer.class);
			List<com.mincorp.order.beans.Package> packages = pdao.getPackageBySellerId(id);
			UserOrders user=null;
			HttpHeaders headers = new HttpHeaders();
		    headers.set("Authorization", authHeader);
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    HttpEntity<String> entity = new HttpEntity<>(headers);
			for(Package p: packages) {
				List<OrderItem> list = new ArrayList<>();
			    double subtotal = 0;
			    double tax = 0;
				for(Map.Entry<Integer,Integer> entry:p.getProductIds().entrySet()) {
					ResponseEntity<ProductDetailsResponse> products =
			                restTemplate.exchange(
			                        "http://PRODUCT-SERVICE/api/products/getSellerProduct?pId="+entry.getKey()+"&sellerId="+id,
			                        HttpMethod.GET,
			                        entity,
			                        ProductDetailsResponse.class
			                );
					int couponID = p.getCouponIds().getOrDefault(entry.getKey(),-1);
					ProductDetailsResponse response = products.getBody();
					subtotal+=response.getPrice();
					Object[] coupon= new Object[] {"",0};
					if(couponID!=-1) {
					coupon =
			                restTemplate.exchange(
			                        "http://SELLER-SERVICE/api/seller/getCouponName/"+couponID,
			                        HttpMethod.GET,
			                        entity,
			                        Object[].class
			                ).getBody();
						tax+=response.getPrice()*(1-Float.parseFloat(String.valueOf(coupon[1]))/100.0)*(response.getTax()/100.0);
					}else {
						tax+=response.getPrice()*(response.getTax()/100.0);
					}
					OrderItem item = new OrderItem(entry.getKey(), response.getTitle(), response.getImage(), response.getPrice(), entry.getValue(), response.getTax(), (String)coupon[0], Float.parseFloat(String.valueOf(coupon[1])));
					
					list.add(item);	
				}
				Date date = odao.getDate(p.getId());
				user = new UserOrders(p.getId(),p.getStatus(), date, p.getPaymentMethod(), p.getPaymentStatus(), p.getAddress(), list, subtotal, tax, (subtotal + tax)-p.getPrice(), p.getPrice(),p.getDelivered());
				userOrders.add(user);
			}
			return ResponseEntity.ok(userOrders);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to fetch the orders");
			return ResponseEntity.status(500).body(userOrders);
		}
		

	}
	
	@PutMapping("/package/{id}/status")
	public void updateStatus(@PathVariable int id, @RequestBody StatusRequest status) {
		if(!status.getStatus().equals("DELIVERED")) {
			pdao.updateStatus(id,status.getStatus(),null);
		}else {
			pdao.updateStatus(id,status.getStatus(),new Date());
		}
	}
	@PutMapping("/package/{id}/payment-status")
	public void updatePackageStatus(@PathVariable int id, @RequestBody StatusRequest status) {
		System.out.print("Came here");
		System.out.print(status.getStatus());
		pdao.updatePaymentStatus(id,status.getStatus());
	}
	@PostMapping("/{id}/cancel")
	public void cancelOrder(@PathVariable int id,@RequestHeader(name="Authoriration") String authHeader) {
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", authHeader);
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> entity = new HttpEntity<>(headers);
		Order o = odao.getOrderByOrderId(id);
		Package p;
		for(int packageId :o.getPackageIds()) {
			p = pdao.getPackage(packageId);
			for(Map.Entry<Integer,Integer> entry: p.getProductIds().entrySet()) {
				boolean removed = restTemplate.exchange(
		                "http://PRODUCT-SERVICE/api/products/return?pId="+entry.getKey()+"&q="+entry.getValue(),
		                HttpMethod.PUT,
		                entity,
		                Boolean.class
		        ).getBody();
			}
			pdao.removePackage(packageId);
		}
		odao.deleteOrder(o);
	}
	@PostMapping("/package/{id}/cancel")
	public void cancelPackage(@PathVariable int id,@RequestHeader(name="Authorization") String authHeader) {
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", authHeader);
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> entity = new HttpEntity<>(headers);
		Package p = pdao.getPackage(id);
		for(Map.Entry<Integer,Integer> entry: p.getProductIds().entrySet()) {
			boolean removed = restTemplate.exchange(
	                "http://PRODUCT-SERVICE/api/products/return?pId="+entry.getKey()+"&q="+entry.getValue(),
	                HttpMethod.PUT,
	                entity,
	                Boolean.class
	        ).getBody();
		}
		pdao.removePackage(id);
		odao.removePackage(id);
	}
	@PostMapping("/return/{orderId}/{productId}")
	public void returnOrder(@PathVariable int orderId,@PathVariable int productId,@RequestHeader(name= "Authorization") String authHeader) {
		int id = service.getClaimsBody(authHeader.substring(7)).get("id",Integer.class);
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", authHeader);
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> entity = new HttpEntity<>(headers);
	    Order o = odao.getOrderByOrderId(orderId);
	    List<Integer> packageIds = o.getPackageIds();
	    int quantity = 0;
	    for(int packageId:packageIds) {
	    	Package p = pdao.getPackage(packageId);
	    	quantity = p.getProductIds().get(productId);
	    }
	    boolean removed = restTemplate.exchange(
                "http://PRODUCT-SERVICE/api/products/return?pId="+productId+"&q="+quantity,
                HttpMethod.PUT,
                entity,
                Boolean.class
        ).getBody();
		rdao.addReturn(new Return(orderId,productId,id));		
	}
	@GetMapping("/return/my")
	public List<Return> myReturn(@RequestHeader(name="Authorization") String authHeader) {
		int id = service.getClaimsBody(authHeader.substring(7)).get("id",Integer.class);
		return rdao.getMyReturns(id);
	}	
	
	@GetMapping("/analyze")
	public ResponseEntity<List<SellerResponse>> analyze(@RequestHeader(name="Authorization") String authHeader){
		int id = service.getClaimsBody(authHeader.substring(7)).get("id",Integer.class);
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", authHeader);
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> entity = new HttpEntity<>(headers);
		List<Package> packages = pdao.getPackageBySellerId(id);
		List<SellerResponse> response = new ArrayList<>();
		for(Package p:packages) {
			double subtotal = 0;
		    double tax = 0;
			for(Map.Entry<Integer,Integer> entry:p.getProductIds().entrySet()) {
				ResponseEntity<ProductDetailsResponse> products =
		                restTemplate.exchange(
		                        "http://PRODUCT-SERVICE/api/products/getSellerProduct?pId="+entry.getKey()+"&sellerId="+id,
		                        HttpMethod.GET,
		                        entity,
		                        ProductDetailsResponse.class
		                );
				int couponID = p.getCouponIds().getOrDefault(entry.getKey(),-1);
				ProductDetailsResponse responseProducts = products.getBody();
				subtotal+=responseProducts.getPrice();
				Object[] coupon= new Object[] {"",0};
				if(couponID!=-1) {
				coupon =
		                restTemplate.exchange(
		                        "http://SELLER-SERVICE/api/seller/getCouponName/"+couponID,
		                        HttpMethod.GET,
		                        entity,
		                        Object[].class
		                ).getBody();
					tax+=responseProducts.getPrice()*(1-Float.parseFloat(String.valueOf(coupon[1]))/100.0)*(responseProducts.getTax()/100.0);
				}else {
					tax+=responseProducts.getPrice()*(responseProducts.getTax()/100.0);
				}
			}
			SellerResponse r = new SellerResponse(subtotal,p.getPrice(),tax, p.getStatus());
			response.add(r);
		}
		return ResponseEntity.ok(response);
	}
}