package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EurekaServiceController {
	
	
	@GetMapping("/")
	public String helloWorld() {
		return "Hello world from demo-1!";
	}
	
	@GetMapping("/product/{id}")
	public String getProduct(@PathVariable Long id) {
		
		return id.toString();
	}
}
