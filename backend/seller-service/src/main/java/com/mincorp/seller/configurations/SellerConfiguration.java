package com.mincorp.seller.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mincorp.seller.filters.JWTFilter;


@Configuration
public class SellerConfiguration {
	@Autowired
	JWTFilter filter;
	@Bean
	public SecurityFilterChain secure(HttpSecurity http) throws Exception {
		http.cors(cors->{})
		.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(auth-> 
			auth.requestMatchers("/api/seller/addProduct","/api/seller/coupons","/api/seller/addCoupon","/api/seller/updateCoupon/**","/api/seller/deleteCoupon/**").hasRole("SELLER")
			.requestMatchers("/api/seller/registerSeller").hasRole("USER")
			.requestMatchers("/api/seller/validate","/api/seller/getDiscount/**","/api/seller/getCouponName/**").hasAnyRole("USER","SELLER")
			.anyRequest().authenticated()
		).addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
