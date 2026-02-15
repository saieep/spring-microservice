package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.util.JWTUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password) {
		// In production, validate username/password from DB
		if ("admin".equals(username) && "password".equals(password)) {
			return JWTUtil.generateToken(username);
		}
		throw new RuntimeException("Invalid credentials");
	}
}
