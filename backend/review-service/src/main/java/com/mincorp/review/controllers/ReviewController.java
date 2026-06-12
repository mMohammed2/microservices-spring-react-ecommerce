package com.mincorp.review.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mincorp.review.DAO.ReviewDao;
import com.mincorp.review.DTO.ReviewRequest;
import com.mincorp.review.DTO.ReviewResponse;
import com.mincorp.review.beans.Review;
import com.mincorp.review.services.JWTService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
	@Autowired
	ReviewDao rdao;
	@Autowired
	RestTemplate restTemplate;
	@Autowired 
	JWTService service;
	@PostMapping("/add")
	public void addReview(@RequestBody ReviewRequest request,@RequestHeader(name = "Authorization") String authHeader) {
		int id = service.getClaimsBody(authHeader.substring(7)).get("id",Integer.class);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authHeader);
		headers.setContentType(MediaType.APPLICATION_JSON);
		ReviewResponse body =
		        new ReviewResponse(request.getReview(), request.getRating());

		HttpEntity<ReviewResponse> entity = new HttpEntity<>(body, headers);

		ResponseEntity<Boolean> update =
		        restTemplate.exchange(
		                "http://PRODUCT-SERVICE/api/products/update?pId=" + request.getProductId(),
		                HttpMethod.POST,
		                entity,
		                Boolean.class
		        );
	    if(update.getBody()) {
	    	System.out.print(request.getProductId());
	    	rdao.addReview(new Review(request.getProductId(),id,request.getRating(),request.getReview()));
	    } else {
	    	System.out.println("Could not add review");
	    }
	}
	@GetMapping("/order/{id}")
	public ResponseEntity<Review> currentReview(@PathVariable int id,@RequestHeader(name="Authorization") String authHeader){
		int bId = service.getClaimsBody(authHeader.substring(7)).get("id",Integer.class);
		return ResponseEntity.ok(rdao.getReview(id,bId));
	}
	@GetMapping("/get/{id}")
	public ResponseEntity<List<Review>> review(@PathVariable int id){
		return ResponseEntity.ok(rdao.getReviewById(id));
	}
	
}
