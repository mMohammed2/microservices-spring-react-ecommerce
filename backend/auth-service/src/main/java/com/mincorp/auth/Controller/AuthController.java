package com.mincorp.auth.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mincorp.auth.DAO.UserDao;
import com.mincorp.auth.DTO.LoginRequest;
import com.mincorp.auth.DTO.LoginResponse;
import com.mincorp.auth.DTO.PasswordRequest;
import com.mincorp.auth.DTO.SellerSignupForward;
import com.mincorp.auth.DTO.SignupRequest;
import com.mincorp.auth.DTO.SignupResponse;
import com.mincorp.auth.DTO.SignupSellerRequest;
import com.mincorp.auth.DTO.UserRequest;
import com.mincorp.auth.Service.JwtService;
import com.mincorp.auth.beans.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	JwtService jwtservice;
	@Autowired
	UserDao udao;
	@Autowired
	private RestTemplate restTemplate;
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		User user = udao.getUser(request.getEmail(), request.getPassword());
		if(user!=null) {
			boolean role = user.getRole().equals("SELLER")? true : false;
			return ResponseEntity.ok(new LoginResponse(jwtservice.createToken(user.getEmail(), user.getRole(),user.getId()), role));
		}
		return ResponseEntity.status(404).body(new LoginResponse(null,false));
	}
    @PostMapping("/signup")
	public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
	    	if (request.getEmail() == null || request.getPassword() == null) {
	    	    return ResponseEntity.badRequest().body(
	    	        new SignupResponse("Invalid input", null)
	    	    );
	    	}
    		
    		UserRequest ureq = new UserRequest(request.getName(),request.getContact());
    		ResponseEntity<Integer> response =
    		        restTemplate.postForEntity(
    		                "http://USER-SERVICE/api/users/addUser",
    		                ureq,
    		                Integer.class
    		        );
    		Integer id = response.getBody();
    		ResponseEntity<Boolean> cart=
    		        restTemplate.postForEntity(
    		                "http://CART-SERVICE/api/cart/createUser",
    		                id,
    		                Boolean.class
    		        );
    		if(!cart.getBody()) {
    			System.out.println("Failed to create a cart");
    		}
    		if(id!=null) {
    			User user = new User(id,request.getEmail(),request.getPassword(),"USER");
        		udao.addUser(user);
    			return ResponseEntity.ok(
    					new SignupResponse("Registration successfull",jwtservice.createToken(user.getEmail(), "USER",user.getId()))
    					);
    		}
    		
			return ResponseEntity.status(4050).body(
					new SignupResponse("User registration fails",null)
					);
		
	}
	@PostMapping("/signupseller")
	public ResponseEntity<SignupResponse> signupseller(@RequestBody SignupSellerRequest request,@RequestHeader(value = "Authorization", required = false) String authHeader) {
		if(authHeader == null) {
			return ResponseEntity.status(401).body(new SignupResponse("Cant authorize your request, please try again later",null));
		}
		String gstnumber,shopname;
		gstnumber = request.getGstnumber();
		shopname = request.getShopname();
		String token = authHeader.substring(7);
		String username = jwtservice.getClaimsBody(token).getSubject();
		User u = udao.getUserByUsername(username);
		if(u!=null) {
			SellerSignupForward s = new SellerSignupForward(u.getId(),shopname,gstnumber);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", authHeader);

			HttpEntity<SellerSignupForward> entity =
			        new HttpEntity<>(s, headers);

			ResponseEntity<String> response =
			        restTemplate.postForEntity(
			                "http://SELLER-SERVICE/api/seller/registerSeller",
			                entity,
			                String.class
			        );
			
			return ResponseEntity.ok(new SignupResponse(response.getBody(),jwtservice.createToken(username, "SELLER",u.getId())));
		}
		return ResponseEntity.status(401).body(
					new SignupResponse("User registration failes",null)
			);
				
	}
	@PostMapping("/changePassword")
	public ResponseEntity<Boolean> changePassword(@RequestHeader(name = "Authorization",required = false) String authHeader,@RequestBody PasswordRequest request) {
		if(authHeader==null||!authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(401).body(false);
		}
		String token = authHeader.substring(7);
		if(token.isEmpty()) {
			return ResponseEntity.status(401).body(false);
		}
		if(!jwtservice.isValid(token)) {
			return ResponseEntity.status(401).body(false);
		}
		String username = jwtservice.getClaimsBody(token).getSubject();
		if(udao.changePassword(username,request.getOldPassword(),request.getNewPassword())) {
			return ResponseEntity.ok(
					true
			);
		}

			return ResponseEntity.status(401).body(
					false
			);
		
	}
}