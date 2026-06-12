package com.mincorp.api;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.mincorp.api.filters.JWTFilter;


@org.springframework.context.annotation.Configuration
public class ApiConfiguration {
	@Autowired
	JWTFilter jwtFilter;
	@Bean
	SecurityWebFilterChain secure(ServerHttpSecurity http) {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
		.csrf(csrf->csrf.disable())
		.authorizeExchange(auth->
			auth.pathMatchers("/api/auth/**","/api/products/getTopProducts","/api/reviews/get/**").permitAll().
			pathMatchers("/api/users/**","api/cart/**","/api/orders/**","/api/seller/validate","/api/reviews/add/**","/api/reviews/update/**").hasAnyRole("USER","SELLER").
			pathMatchers("/api/seller/**","/api/products/addProduct","/api/products/seller").hasRole("SELLER").
			pathMatchers("/api/payments/**").permitAll()
			
			.anyExchange().authenticated()
		).addFilterAt(jwtFilter,SecurityWebFiltersOrder.AUTHENTICATION);
		;
		return http.build();
	}
	 @Bean
	    public RouteLocator routes(RouteLocatorBuilder builder) {
	        return builder.routes()
	                .route("auth-service", r -> r
	                        .path("/api/auth/**")
	                        .uri("lb://AUTH-SERVICE"))
	                .route("product-service", r -> r
	                        .path("/api/products/**")
	                        .uri("lb://PRODUCT-SERVICE"))
	                .route("user-service", r -> r
	                        .path("/api/users/**")
	                        .uri("lb://USER-SERVICE"))
	                .route("seller-service",r-> r
	                		.path("/api/seller/**")
	                		.uri("lb://SELLER-SERVICE"))
	                .route("cart-service",r-> r
	                		.path("/api/cart/**")
	                		.uri("lb://CART-SERVICE"))
	                .route("order-service",r-> r
	                		.path("/api/orders/**")
	                		.uri("lb://ORDER-SERVICE"))
	                .route("payment-service",r-> r
	                		.path("/api/payments/**")
	                		.uri("lb://PAYMENT-SERVICE"))
	                .route("review-service",r-> r
	                		.path("/api/reviews/**")
	                		.uri("lb://REVIEW-SERVICE"))
	                .build();
	    }
	 
	 @Bean
	    public CorsConfigurationSource corsConfigurationSource() {

	        CorsConfiguration config = new CorsConfiguration();

	        config.setAllowedOrigins(List.of("http://localhost:3000"));
	        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	        config.setAllowedHeaders(List.of("*"));
	        config.setAllowCredentials(true);

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);

	        return source;
	    }	
}
