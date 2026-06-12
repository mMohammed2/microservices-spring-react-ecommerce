package com.mincorp.auth.Service;

import java.security.Key;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Value("${jwt.secret}")
	private	String mySigningKey;
	public Key getKeys() {
		return Keys.hmacShaKeyFor(mySigningKey.getBytes());
	}
	@SuppressWarnings("deprecation")
	public String createToken(String username,String role,int id) {
		return 	Jwts.builder().signWith(getKeys()).setSubject(username).claim("role", role)
				.claim("id", id)
		.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
		.compact();
	}
	@SuppressWarnings("deprecation")
	public Claims getClaimsBody(String token) {
		return Jwts.parser().setSigningKey(getKeys()).build().parseClaimsJws(token).getBody();
	}
	public boolean isExpired(String token) {
		Claims claims = getClaimsBody(token);
		return claims.getExpiration().before(new Date());
	}
	public boolean isValid(String token) {
		return !isExpired(token);
	}
	
}
