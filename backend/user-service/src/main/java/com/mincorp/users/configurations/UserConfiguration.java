package com.mincorp.users.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mincorp.users.filters.JWTFilter;


@Configuration
public class UserConfiguration {
	@Autowired
	JWTFilter filter;
	@Bean
	public SecurityFilterChain secure(HttpSecurity http) throws Exception {
		http.cors(cors->{})
		.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(
				auth->
				auth.requestMatchers("/api/users/getUser","/api/users/addAddress","/api/users/getAddress").hasAnyRole("USER","SELLER")
				.requestMatchers("/api/users/addUser").permitAll()
				.anyRequest().authenticated()
		).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
