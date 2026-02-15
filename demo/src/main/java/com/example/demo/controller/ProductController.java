package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.entity.service.ProductService;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
@RequestMapping("/product")
@RefreshScope
public class ProductController {
	
	private ProductService service;
	
	MeterRegistry meterRegistry;
	
	public ProductController(ProductService service , MeterRegistry meterRegistry) {
		this.service = service;
		this.meterRegistry = meterRegistry;
	}

	@Value("${msg:Config Server is not working. Please check...}")
	private String msg;

	@GetMapping("/{id}")
	@Timed(value = "api.product.get.duration", description = "Time taken to return product by id")
	public Product getProduct(@PathVariable Long id) {
        // Increment request counter with tags for better filtering
        meterRegistry.counter("api.getproduct.requests", 
                            "method", "GET", 
                            "endpoint", "/product/{id}").increment();
		System.out.println(this.msg);
		try {
			Product product = service.getProductById(id);
			// Counter for successful requests
			meterRegistry.counter("api.getproduct.success", 
			                    "method", "GET").increment();
			return product;
		} catch (Exception e) {
			// Counter for failed requests
			meterRegistry.counter("api.getproduct.errors", 
			                    "method", "GET", 
			                    "error", e.getClass().getSimpleName()).increment();
			throw e;
		}
	}
}
