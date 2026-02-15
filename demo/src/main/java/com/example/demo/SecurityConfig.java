package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.JWTAuthfilter;

@Configuration
public class SecurityConfig {

	private final JWTAuthfilter jwtAuthFilter;

	public SecurityConfig(JWTAuthfilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						auth -> auth
						.requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
						.requestMatchers(HttpMethod.GET,"/").permitAll()
						.requestMatchers(HttpMethod.POST,"/actuator/**").permitAll()
						.requestMatchers(HttpMethod.GET,"/actuator/**").permitAll()
						.requestMatchers(HttpMethod.GET,"/demo/**").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
