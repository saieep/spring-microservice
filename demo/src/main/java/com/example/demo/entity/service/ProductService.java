package com.example.demo.entity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.entity.exception.ProductNotFoundException;
import com.example.demo.entity.repository.ProductRepository;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Service
public class ProductService {
	
    private final Counter productCounter;
    private final Timer productProcessingTimer;
    
    public ProductService(MeterRegistry registry) {
        this.productCounter = Counter.builder("product.search.count")
                .description("Total no of products searched")
                .tag("service", "product-service")
                .register(registry);
        
        this.productProcessingTimer = Timer.builder("product.searching.time")
                .description("Product processing duration")
                .register(registry);
    }

	@Autowired
	private ProductRepository repository;

	public Product getProductById(Long id) {
		 return productProcessingTimer.record(() -> {
	            productCounter.increment();
	            return repository.findById(id)
	    				.orElseThrow(() -> new ProductNotFoundException(id));
	        });
	}
}
