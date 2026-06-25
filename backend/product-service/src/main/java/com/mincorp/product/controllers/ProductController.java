package com.mincorp.product.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mincorp.product.DAO.ProductDao;
import com.mincorp.product.beans.Product;
import com.mincorp.product.services.JWTService;
import com.mincorp.product.DTO.CachedProduct;
import com.mincorp.product.DTO.ProductDetailsResponse;
import com.mincorp.product.DTO.ProductOrder;
import com.mincorp.product.DTO.ProductRequest;
import com.mincorp.product.DTO.ProductResponse;
import com.mincorp.product.DTO.ReviewResponse;

@RequestMapping("/api/products")
@RestController
public class ProductController {
	@Autowired
	ProductDao prdao;
	@Autowired
	JWTService jwtservice;
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/getTopProducts")
	public List<Product> getProducts() {
		return prdao.getBestProducts();
	}
	
	@PostMapping("/addProduct")
	public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest request,@RequestHeader(name = "Authorization",required = false) String authHeader) { 
		if(authHeader==null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(500).body(new ProductResponse(false,"Cant process request at the moment please try again later."));
		}
		String token = authHeader.substring(7);
		int id = jwtservice.getClaimsBody(token).get("id",Integer.class);
		Product p = prdao.addProduct(new Product(request.getTitle(), request.getPrice(), 0,0,request.getDescription(), request.getImages(),
				0, request.getType(),id,request.getQuantity(),request.getTax()));
		
		if(p!=null) {
			ProductDetailsResponse pr = new ProductDetailsResponse(p.getId(),p.getName(),p.getImages(),p.getPrice(),p.getTax());
			restTemplate.postForObject("http://CACHE-SERVICE/api/cache/products/save?id="+p.getId(), pr, Void.class);
			
			return ResponseEntity.ok(new ProductResponse(true,"Successfully added the product"));
		}
		return ResponseEntity.status(500).body(new ProductResponse(false,"Cant process request at the moment please try again later."));
	}
	
	@GetMapping("/seller")
	public List<Product> getProductsForSeller(@RequestHeader(name = "Authorization",required = false) String authHeader){
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			return null;
		}
		String token = authHeader.substring(7);
		if(!jwtservice.isValid(token)) {
			return null;
		}
		int id = jwtservice.getClaimsBody(token).get("id",Integer.class);		
		return prdao.getProductsForSeller(id);
	}
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> update(@PathVariable int id,@RequestBody ProductRequest request){
		Product p = prdao.findById(id);
		p.setDescription(request.getDescription());
		p.setImages(request.getImages());
		p.setName(request.getTitle());
		p.setPrice(request.getPrice());
		p.setQuantity(request.getQuantity());
		p.setType(request.getType());
		p = prdao.addProduct(p);
		
		if(p!=null) {
			return ResponseEntity.ok(new ProductResponse(true,"Successfully added the product"));
		}
		return ResponseEntity.status(500).body(new ProductResponse(false,"Cant process request at the moment please try again later."));
	}
	@PostMapping("/getCart")
	public List<Product> getCart(@RequestBody List<Integer> productIds){
		return prdao.getProductByIds(productIds);
	}
	@GetMapping("/getPrice")
	public ProductOrder getPrice(@RequestParam int pId,@RequestParam int q) {
		Product p = prdao.findById(pId);
		int quantity = Math.min(p.getQuantity(), q);
		p.setQuantity(p.getQuantity()-quantity);
		p.setTotalSold(p.getTotalSold()+quantity);
		prdao.update(p);
		return new ProductOrder(p.getPrice(),p.getTax(),quantity,p.getSellerId());
	}
	
	@GetMapping("/getSellerProduct")
	public ProductDetailsResponse getSellerProduct(@RequestParam int pId,@RequestParam int sellerId) {
		Product p = prdao.getSellerProduct(pId,sellerId);
		return new ProductDetailsResponse(pId,p.getName(),p.getImages(),p.getPrice(),p.getTax()); 
	}
	@PostMapping("/update")
	public Boolean update(@RequestBody ReviewResponse request,@RequestParam int pId) {
		Product p = prdao.findById(pId);
		p.setTotalRating(p.getTotalRating()+1);
		p.setTotalReviews(p.getTotalReviews()+request.getStars());
		prdao.addProduct(p);
		return true;
	}
	@PutMapping("/return")
	public ResponseEntity<Boolean> reinstate(@RequestParam int pId,@RequestParam int q){
		Product p = prdao.getProduct(pId);
		p.setTotalSold(p.getTotalSold()-q);
		p.setQuantity(p.getQuantity()+q);
		prdao.addProduct(p);
		return ResponseEntity.ok(true);
	}
	@GetMapping("/search")
	public ResponseEntity<List<Product>> search(@RequestParam String query){
		return ResponseEntity.ok(prdao.getSearchedProducts(query));
	}
	@GetMapping("/all")
	public ResponseEntity<List<CachedProduct>> all() {
	    ResponseEntity<List<CachedProduct>> response = restTemplate.exchange(
	        "http://CACHE-SERVICE/api/cache/products/get",
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<List<CachedProduct>>() {}
	    );
	    List<CachedProduct> products = response.getBody();

	    if (products != null && !products.isEmpty()) {
	        return ResponseEntity.ok(products);
	    }
	    
	    products = prdao.getAllProducts().stream()
	        .map(x -> new CachedProduct(
	            x.getName(), x.getPrice(), x.getTotalRating(), 
	            x.getTotalSold(), x.getDescription(), x.getImages(), 
	            x.getTotalReviews(), x.getType(), x.getQuantity(), 
	            x.getSellerId(), x.getTax()
	        ))
	        .toList();

	    restTemplate.postForObject("http://CACHE-SERVICE/api/cache/products/save", products, String.class);
	    
	    return ResponseEntity.ok(products);
	}
}
