package com.mincorp.order.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.mincorp.order.filters.JWTFilter;

@Configuration
public class OrdersConfiguration {
	@Autowired
	JWTFilter filter;
	@Bean
	@LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	@Bean
	public SecurityFilterChain secure(HttpSecurity http) throws Exception {
		http.cors(cors->{})
		.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(auth
				-> 
			auth.requestMatchers("/api/orders/create","/api/return/**").hasAnyRole("USER","SELLER")
			.requestMatchers("/api/order/package/**").hasRole("SELLER")
			.anyRequest().authenticated()	
				)
		.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	
}
