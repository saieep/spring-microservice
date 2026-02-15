# Spring Cloud Circuit Breaker in Simple Words (with SAP Hybris Context)

Spring Cloud Circuit Breaker is like an electrical circuit breaker in your home - it automatically "trips" and stops requests to a failing service to prevent cascading failures and give the service time to recover.

## Simple Explanation

Imagine you're trying to call a friend who never picks up the phone. After calling 10 times and getting no answer, you stop trying for a while instead of wasting time calling repeatedly. Spring Cloud Circuit Breaker does exactly this for microservices. When a service keeps failing, the circuit breaker "opens" and immediately returns a fallback response instead of waiting for timeouts, protecting your application from hanging and cascading failures.

## Three States

**Closed State (Normal Operation)**  
Everything works fine - requests pass through to the service normally. The circuit breaker monitors failures and counts them.

**Open State (Failure Detected)**  
When failures exceed a threshold (e.g., 5 failures out of 10 requests), the circuit breaker "trips" and opens. All subsequent requests fail immediately without even trying to call the service, returning a fallback response instead.

**Half-Open State (Testing Recovery)**  
After a configured timeout period (e.g., 10 seconds), the circuit breaker allows a limited number of test requests through. If they succeed, it returns to Closed state. If they fail, it goes back to Open state.

## How It Works

Spring Cloud Circuit Breaker watches method calls to detect failures. When failures build up to a threshold, it opens the circuit and redirects calls to a fallback method. The circuit breaker automatically monitors health and transitions between states based on success/failure rates.

## Basic Implementation

```java
@Service
public class ProductService {
    
    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public String getProductDetails(String productCode) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("product-service");
        
        // Wrap the risky call in circuit breaker with fallback
        return circuitBreaker.run(
            () -> restTemplate.getForObject("http://product-api/products/" + productCode, String.class),
            throwable -> getFallbackProduct(productCode)  // Fallback method
        );
    }
    
    private String getFallbackProduct(String productCode) {
        return "Product temporarily unavailable: " + productCode;
    }
}
