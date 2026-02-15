package com.example.demo.controller.service;

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.example.demo.entity.Product;

@Service
public class ProductControllerService {
    private final CircuitBreakerFactory<?, ?> cbFactory;
    private final RestClient restClient;

    public ProductControllerService(RestClient restClient, CircuitBreakerFactory<?, ?> cbFactory) {
        this.restClient = restClient;
        this.cbFactory = cbFactory;
    }

    public Product slow(Long id) {
        return cbFactory.create("productService")
            .run(
                () -> restClient.get()
                    .uri("http://demo/product/{id}", id)
                    .retrieve()
                    .body(Product.class),
                throwable -> {
                    Product fallbackProduct = new Product();
                    fallbackProduct.setId(id);
                    fallbackProduct.setName("Product Unavailable");
                    fallbackProduct.setDescription("Fallback product");
                    return fallbackProduct;
                }
            );
    }
}