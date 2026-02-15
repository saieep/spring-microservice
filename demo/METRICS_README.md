# Metrics Configuration - Demo Service

## Issue Resolved
The `@Timed` annotation and custom metrics were not appearing in `/actuator/metrics` endpoint.

## Root Cause
The `@Timed` annotation requires AOP (Aspect-Oriented Programming) support and the `TimedAspect` bean to intercept method calls and record timing metrics.

## Solution Applied

### 1. Added AOP Dependency
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

### 2. Created MetricsConfig Class
Location: `src/main/java/com/example/demo/config/MetricsConfig.java`
- Enables AOP with `@EnableAspectJAutoProxy`
- Configures `TimedAspect` bean for `@Timed` annotation support

### 3. Enhanced Application Properties
Added metrics-specific configurations:
```properties
management.endpoint.metrics.enabled=true
management.metrics.export.prometheus.enabled=true
```

## Available Custom Metrics

### ProductController Metrics
1. **api.getproduct.requests** (Counter)
   - Incremented on each GET `/product/{id}` request
   - Tags: `method=GET`, `endpoint=/product/{id}`

2. **api.product.get.duration** (Timer) 
   - Measures execution time of GET `/product/{id}`
   - Configured via `@Timed` annotation

3. **api.getproduct.success** (Counter)
   - Incremented on successful product retrieval
   - Tags: `method=GET`

4. **api.getproduct.errors** (Counter)
   - Incremented on exceptions during product retrieval
   - Tags: `method=GET`, `error=ExceptionClassName`

## Testing Instructions

### 1. Start the Application
```bash
cd c:\Users\saideepbandarkar\Documents\workspace-sts\demo
.\mvnw.cmd spring-boot:run
```

### 2. Generate Metrics Data
```bash
# Make some requests to generate metrics
curl http://localhost:8080/product/1
curl http://localhost:8080/product/2
curl http://localhost:8080/product/999  # This might generate an error metric
```

### 3. Check Available Metrics
```bash
# List all available metrics
curl http://localhost:8080/actuator/metrics

# Check specific metrics
curl http://localhost:8080/actuator/metrics/api.getproduct.requests
curl http://localhost:8080/actuator/metrics/api.product.get.duration
curl http://localhost:8080/actuator/metrics/api.getproduct.success
curl http://localhost:8080/actuator/metrics/api.getproduct.errors
```

### 4. Test Metrics Endpoint
```bash
# Visit the metrics info endpoint
curl http://localhost:8080/metrics-info

# Generate test metrics
curl http://localhost:8080/test-metrics
```

## Troubleshooting

### If metrics still don't appear:
1. Ensure the application is rebuilt after changes
2. Make actual requests to the `/product/{id}` endpoint first
3. Check that AOP is working by looking for `TimedAspect` in application logs
4. Verify that MeterRegistry is properly injected

### Common Issues:
- **Metrics not showing**: Ensure you've made requests to the endpoints first
- **@Timed not working**: Check that AOP dependency is included and MetricsConfig is loaded
- **Empty metrics list**: Verify actuator endpoints are exposed in application.properties

## Verification Commands
```bash
# 1. Check if TimedAspect bean is loaded
curl http://localhost:8080/actuator/beans | grep -i "timed"

# 2. Check if custom metrics are registered after making requests
curl http://localhost:8080/actuator/metrics | grep -E "(api\.getproduct|api\.product)"

# 3. Check prometheus metrics (if enabled)
curl http://localhost:8080/actuator/prometheus | grep -E "(api_getproduct|api_product)"
```