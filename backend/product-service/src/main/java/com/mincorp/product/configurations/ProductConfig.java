package com.mincorp.product.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.mincorp.product.filters.JWTFilter;


@Configuration
@ComponentScan(basePackages = "com.mincorp.product.controllers")
public class ProductConfig {
	@Autowired
	JWTFilter filter;
	@Bean
	SecurityFilterChain secure(HttpSecurity http) throws Exception {
		http.cors(cors -> {})
		.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(auth-> 
		auth.requestMatchers("/api/products/getTopProducts","/api/products/search","/api/products/all").permitAll()
		.requestMatchers("/api/products/getCart","/api/products/getPrice","/api/products/getProduct/**","/api/products/update","/api/products/return/**").hasAnyRole("USER","SELLER")
		.requestMatchers("/api/products/seller","/api/products/addProduct","/api/products/getSellerProduct/**").hasRole("SELLER")
		.anyRequest().authenticated())
		.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
