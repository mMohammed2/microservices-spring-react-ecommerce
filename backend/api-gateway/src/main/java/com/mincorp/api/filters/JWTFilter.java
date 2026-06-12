package com.mincorp.api.filters;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.mincorp.api.services.JWTService;

import reactor.core.publisher.Mono;
@Component
public class JWTFilter implements WebFilter {

    @Autowired
    private JWTService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String auth = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
        	exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return chain.filter(exchange);
        }

        String token = auth.substring(7);

        try {
            String username = jwtService.extractUsername(token);
            String role= jwtService.extractRole(token);
            if (jwtService.isValid(token)) {

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_"+role)) 
                        );

                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }

        } catch (Exception e) {
            return chain.filter(exchange);
        }

        return chain.filter(exchange);
    }
}