# Spring Cloud Gateway

Spring Cloud Gateway is a smart "traffic controller" or "front door" for microservices that sits between clients and your backend services, routing requests to the right destination.

## Simple Explanation

Think of Spring Cloud Gateway as a receptionist at a large office building. When visitors (client requests) arrive, the receptionist checks who they want to meet, directs them to the correct floor and office (microservice), and can even check their ID or appointment before letting them through. Instead of visitors needing to know exactly where each person sits, they just go to the reception desk.

## Core Components

**Routes**  
Routes define where requests should go, consisting of an ID, destination URI, predicates, and filters. Each route is like a rule that says "if this request looks like X, send it to service Y".

**Predicates**  
Predicates are conditions that check if incoming requests match a specific route. They can match based on path, headers, query parameters, or cookies - essentially deciding which requests go where.

**Filters**  
Filters modify requests before sending them to microservices or responses before returning them to clients. They handle cross-cutting concerns like authentication, logging, rate limiting, or adding/removing headers.

## How It Works

When a client request arrives, it goes to the Gateway Handler Mapping which uses predicates to find a matching route. The request then passes through the Gateway Web Handler which applies pre-filters, forwards the request to the destination microservice, receives the response, and applies post-filters before returning to the client.

## SAP Hybris Context Example

In SAP Commerce (Hybris), which is increasingly adopting microservices architecture, Spring Cloud Gateway can serve as the unified entry point for various services:

**Scenario**: An e-commerce site with separated Hybris services

```java
@Configuration
public class HybrisGatewayConfig {

    @Bean
    public RouteLocator hybrisRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            // Route product catalog requests
            .route("product-service", r -> r
                .path("/products/**")
                .filters(f -> f
                    .addRequestHeader("X-Source", "Gateway")
                    .circuitBreaker(c -> c.setName("productCB")))
                .uri("http://hybris-product-service:9001"))

            // Route order management requests
            .route("order-service", r -> r
                .path("/orders/**")
                .filters(f -> f
                    .addRequestHeader("X-User-Context", "B2C"))
                .uri("http://hybris-order-service:9002"))

            // Route customer profile requests
            .route("customer-service", r -> r
                .path("/customers/**")
                .filters(f -> f.rewritePath("/customers/(?<segment>.*)", "/profiles/${segment}"))
                .uri("http://hybris-customer-service:9003"))

            // Route payment processing
            .route("payment-service", r -> r
                .path("/payments/**")
                .and()
                .header("Authorization")
                .filters(f -> f.retry(3))
                .uri("http://hybris-payment-service:9004"))

            .build();
    }
}
```

**Benefits in Hybris Architecture**:

- **Single Entry Point**: Instead of frontend applications calling multiple Hybris OCC (Omnichannel Commerce) endpoints directly, they call one gateway URL
- **Security Centralization**: OAuth token validation, rate limiting, and API key checks happen once at the gateway rather than in each Hybris extension
- **Protocol Translation**: Gateway can accept REST calls from mobile apps and translate them to Hybris-specific formats or even legacy SOAP services
- **Request Aggregation**: When a product page needs data from catalog service, inventory service, and pricing service, the gateway can combine these into one response
- **Integration with Hybris Extensions**: Gateway can route to custom Hybris extensions, third-party services (payment gateways, shipping providers), and legacy ERP systems

**Practical Use Case**:
```
Client Request: GET /products/123456
         ↓
Spring Cloud Gateway
         ↓ (checks predicates)
         ↓ (applies authentication filter)
         ↓ (adds correlation ID)
         ↓
Hybris Product Service (port 9001)
         ↓ (aggregates with)
Hybris Inventory Service (port 9005)
         ↓
Gateway combines responses
         ↓
Returns to client: {product details + stock level}
```

This pattern is especially useful when modernizing Hybris deployments by gradually extracting functionality into microservices while maintaining a consistent API for consumers.
