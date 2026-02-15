package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RefreshScope
public class HelloWorldController {

	@Value("${msg:Config Server is not working. Please check...}")
	private String msg;

	private final DiscoveryClient discoveryClient;
	private final RestClient restClient;

	public HelloWorldController(DiscoveryClient discoveryClient, RestClient.Builder restClientBuilder) {
		this.discoveryClient = discoveryClient;
		restClient = restClientBuilder.build();
	}

	@CrossOrigin(origins = "http://localhost:9000")
	@GetMapping("/")
	public String index() {
		System.out.println(this.msg);
		return "Greetings from Spring Boot app demo!!";
	}

	
	@GetMapping("/demo/testeureka")
	public String eurekaClient() {
		ServiceInstance serviceInstance = discoveryClient.getInstances("demo").get(0);
		String serviceAResponse = restClient.get().uri(serviceInstance.getUri() + "/").retrieve()
				.body(String.class);
		return serviceAResponse;
	}
	 
}
