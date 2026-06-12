package com.mincorp.pay.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mincorp.pay.controllers.DTO.CardRequest;
import com.mincorp.pay.controllers.DTO.UpiRequest;
import com.mincorp.pay.controllers.DTO.UpiResponse;


@RestController
@RequestMapping("/api/payments")
public class PayController {
	@PostMapping("/upi")
	public ResponseEntity<UpiResponse> upi(@RequestBody UpiRequest request) {
		if(request.getAmount() != 0) {
			return ResponseEntity.ok(new UpiResponse("https://quickchart.io/qr?text=pay money for buying product via upi on ShopKart units "+request.getAmount()));
		}
		return ResponseEntity.status(500).body(new UpiResponse());
	}
	@PostMapping("/card")
	public ResponseEntity<Boolean> card(@RequestBody CardRequest request){
		if(request.getAmount() != 0 && request.getCardNumber() != 0 && request.getCvv()<1000 && request.getCvv()>99) {
			return ResponseEntity.ok(true);
		}
		return ResponseEntity.status(500).body(false);
	}
}
