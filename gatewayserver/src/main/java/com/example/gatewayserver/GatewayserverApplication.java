package com.example.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import com.example.gatewayserver.filter.JWTGatewayFilter;

@SpringBootApplication
public class GatewayserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayserverApplication.class, args);
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder, JWTGatewayFilter jwtGatewayFilter) {
        return builder.routes()
            .route("demo_root", r -> r.path("/").uri("lb://demo"))
            .route("demo_testeureka", r -> r.path("/demo/testeureka").uri("lb://demo"))
            .route("product_route", r -> r.path("/product/**").filters(f -> f.filter(jwtGatewayFilter)).uri("lb://demo"))
            .build();
    }
}