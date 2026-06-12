package com.mincorp.users.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mincorp.users.DAO.UserDao;
import com.mincorp.users.DTO.AddressRequest;
import com.mincorp.users.DTO.UserRequest;
import com.mincorp.users.DTO.UserResponse;
import com.mincorp.users.beans.User;
import com.mincorp.users.services.JWTService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired 
	UserDao udao;
	@Autowired
	JWTService jwtService;
	
	@GetMapping("/getUser")
	public ResponseEntity<UserResponse> home(@RequestHeader(name = "Authorization",required = false) String authHeader) {
		if(authHeader==null||!authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(401).body(new UserResponse(null,null,0,null,"User not authenticated!"));
		}
		String token = authHeader.substring(7);
		if(token.isEmpty()) {
			return ResponseEntity.status(401).body(new UserResponse(null,null,0,null,"User not authenticated!"));
		}
		if(!jwtService.isValid(token)) {
			return ResponseEntity.status(401).body(new UserResponse(null,null,0,null,"Session timed out!"));
		}
		String username = jwtService.getClaimsBody(token).getSubject();
		Object idObj = jwtService.getClaimsBody(token).get("id");

		if (idObj == null) {
		    throw new RuntimeException("Missing id in token");
		}

		int id = Integer.parseInt(idObj.toString());
		if(username!=null) {
			User user = udao.findById(id);
			return ResponseEntity.ok(new UserResponse(user.getName(),username,user.getContact(),user.getAddress(),"Session restored"));
		}
		return ResponseEntity.status(401).body(new UserResponse(null,null,0,null,"An internal error occurred!"));
	}	
	
	@PostMapping("/addUser")
	public ResponseEntity<Integer> add(@RequestBody UserRequest request){
		User u = udao.registerUser(request.getName(), request.getContact(), null);
		if(u!=null) {
			return ResponseEntity.ok(u.getId());
		}
				
		return ResponseEntity.status(500).body(null);
	}
	
	@PostMapping("/addAddress")
	public ResponseEntity<Boolean> addAddress(@RequestHeader(name = "Authorization",required = false) String authHeader,@RequestBody AddressRequest request){
		if(authHeader==null||!authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(500).body(false);
		}
		String token = authHeader.substring(7);
		if(token.isEmpty()) {
			return ResponseEntity.status(500).body(false);
		}
		if(!jwtService.isValid(token)) {
			return ResponseEntity.status(500).body(false);
		}
		String username = jwtService.getClaimsBody(token).getSubject();
		Object idObj = jwtService.getClaimsBody(token).get("id");
		int id = Integer.parseInt(idObj.toString());
		if(username!=null) {
			User user = udao.findById(id);
			user.setAddress(request.getAddress());
			udao.update(user);
			return ResponseEntity.ok(true);
		}

		return ResponseEntity.status(500).body(false);
	}
	
	@GetMapping("/getAddress")
	public List<String> getAddress(@RequestHeader(name = "Authorization") String authHeader){
		if(!authHeader.startsWith("Bearer ")) {
			return null;
		}
		int id = jwtService.getClaimsBody(authHeader.substring(7)).get("id",Integer.class);
		return udao.findById(id).getAddress();
	}
}
