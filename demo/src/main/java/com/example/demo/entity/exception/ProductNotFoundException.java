package com.example.demo.entity.exception;

@SuppressWarnings("serial")
public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException(Long id) {
	    super("Could not find product " + id);
	  }
	}