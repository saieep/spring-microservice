# Spring Boot Microservices API Collection

## Overview
This Postman collection contains all REST APIs from your Spring Boot microservices ecosystem.

## Services Included

### 1. Config Server (Port 8888)
- **Purpose**: Centralized configuration management
- **Endpoints**:
  - `GET /configserver/testeureka` - Test Eureka discovery
  - `GET /demo/default` - Get demo service config (default profile)
  - `GET /demo/production` - Get demo service config (production profile)

### 2. Eureka Server (Port 8761)
- **Purpose**: Service discovery and registration
- **Endpoints**:
  - `GET /` - Eureka dashboard
  - `GET /eureka/apps` - List all registered services

### 3. Gateway Server (Port 8082)
- **Purpose**: API Gateway with JWT authentication
- **Endpoints**:
  - `GET /` - Route to demo service root
  - `GET /demo/testeureka` - Route to demo eureka test
  - `GET /product/{id}` - Route to product service (requires JWT token)

### 4. Demo Service (Port 8080)
- **Purpose**: Main application with products and authentication
- **Endpoints**:
  - **Authentication**:
    - `POST /auth/login` - Login and get JWT token
  - **General**:
    - `GET /` - Welcome message
    - `GET /demo/testeureka` - Test Eureka discovery
  - **Products**:
    - `GET /product/{id}` - Get product by ID
  - **Spring Data REST API**:
    - `GET /product` - Get all products
    - `POST /product` - Create new product
    - `PUT /product/{id}` - Update product
    - `DELETE /product/{id}` - Delete product
    - `GET /product/search/findByName?name=...` - Search products by name
  - **Actuator**:
    - `GET /actuator/health` - Health check
    - `GET /actuator/info` - Application info
    - `POST /actuator/refresh` - Refresh configuration
    - `GET /actuator/metrics` - Application metrics

### 5. Demo-1 Service (Port 8081)
- **Purpose**: Secondary demo service
- **Endpoints**:
  - `GET /` - Welcome message

## How to Import into Postman

1. Open Postman
2. Click "Import" button
3. Select "File" tab
4. Choose the `Spring_Boot_Microservices_API_Collection.json` file
5. Click "Import"

## Authentication Flow

1. **Login**: Use the `POST /auth/login` endpoint with:
   - Username: `admin`
   - Password: `password`
2. **Copy JWT Token**: Copy the returned token from the response
3. **Set Variable**: In Postman, set the collection variable `jwt_token` with the copied token
4. **Access Protected Routes**: Now you can access protected endpoints through the gateway

## Environment Variables

The collection includes these variables:
- `jwt_token` - JWT token from login
- `config_server_url` - http://localhost:8888
- `eureka_server_url` - http://localhost:8761
- `gateway_server_url` - http://localhost:8082
- `demo_service_url` - http://localhost:8080
- `demo1_service_url` - http://localhost:8081

## Service Startup Order

For complete functionality, start services in this order:
1. Config Server (Port 8888)
2. Eureka Server (Port 8761)
3. Demo Service (Port 8080)
4. Demo-1 Service (Port 8081)
5. Gateway Server (Port 8082)

## Testing Tips

1. **Start with Health Checks**: Test `/actuator/health` endpoints first
2. **Authentication**: Always login first to get JWT token for protected endpoints
3. **Gateway vs Direct**: Test both direct service calls and gateway-routed calls
4. **Service Discovery**: Use `/demo/testeureka` endpoints to verify Eureka functionality

## Sample Product Data

For testing Spring Data REST endpoints, use this sample JSON:

```json
{
  "name": "Sample Product",
  "price": 99.99,
  "description": "A sample product for testing"
}
```