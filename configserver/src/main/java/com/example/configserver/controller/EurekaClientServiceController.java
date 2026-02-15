package com.example.configserver.controller;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class EurekaClientServiceController {

	private final DiscoveryClient discoveryClient;
	private final RestClient restClient;

	public EurekaClientServiceController(DiscoveryClient discoveryClient, RestClient.Builder restClientBuilder) {
		this.discoveryClient = discoveryClient;
		restClient = restClientBuilder.build();
	}
	
	@GetMapping("/configserver/testeureka")
	public String eurekaClient() {
		ServiceInstance serviceInstance = discoveryClient.getInstances("demo").get(0);
		String serviceAResponse = restClient.get()
				.uri(serviceInstance.getUri() + "/")
				.retrieve()
				.body(String.class);
		return serviceAResponse;
	}
}
