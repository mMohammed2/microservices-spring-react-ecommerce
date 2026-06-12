package com.mincorp.order.filters;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mincorp.order.services.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter{
	@Autowired
	JWTService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String auth = request.getHeader("Authorization");
		if(auth == null || auth.isEmpty() || !auth.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = auth.substring(7);
		if(!jwtService.isValid(token)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String username = jwtService.getClaimsBody(token).getSubject();
		String role = jwtService.getClaimsBody(token).get("role",String.class);
		if(username!=null) {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,null,List.of(new SimpleGrantedAuthority("ROLE_"+role)));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}
	
	
}
