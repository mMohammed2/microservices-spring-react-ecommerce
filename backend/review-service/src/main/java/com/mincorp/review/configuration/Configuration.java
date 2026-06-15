package com.mincorp.review.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.mincorp.review.filters.JWTFilter;

@org.springframework.context.annotation.Configuration
public class Configuration {
	@Autowired
	JWTFilter filter;
	@Bean
	@LoadBalanced
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
	@Bean
	public SecurityFilterChain secure(HttpSecurity http) throws Exception {
		http.cors(cors->{})
		.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(auth->
		auth.requestMatchers("/api/revies/get/**").permitAll()
		.requestMatchers("/api/reviews/add","/api/reviews/get/**").hasAnyRole("USER","SELLER")
		.anyRequest().authenticated()
		).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
