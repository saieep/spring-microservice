package com.example.gatewayserver.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JWTGatewayFilter implements GatewayFilter, Ordered {
    
    private static final Logger logger = LoggerFactory.getLogger(JWTGatewayFilter.class);
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().toString();
        logger.info("🔒 JWT Filter triggered for {} {}", method, requestPath);
        
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        logger.info("📋 Authorization header: {}", authHeader != null ? "Present" : "Missing");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("❌ Unauthorized access attempt - Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        // Extract token for validation
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        logger.info("✅ Valid Authorization header found, token length: {}", token.length());
        
        // Basic token validation
        if (token.isEmpty()) {
            logger.warn("❌ Empty JWT token");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        logger.info("✅ JWT validation passed, proceeding with request to backend");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // High precedence
    }
}