package com.nationwide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nationwide.dto.ResponseTokenDto;
import com.nationwide.mapping.Mapping;
import com.nationwide.persistence.domain.Token;
import com.nationwide.service.TokenService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/token")
public class TokenController {
	
	@Autowired
	private Mapping mapping;
	
	@Autowired
	private TokenService service;
	
	@PostMapping("/{accountId}")
	public ResponseTokenDto createAuthToken(@PathVariable String accountId) {
		return mapping.map(service.assignToken(accountId), ResponseTokenDto.class);
	}
	
	@GetMapping("/{bearerToken}")
	public ResponseTokenDto authenticate(@PathVariable String bearerToken) {
		Token token = service.getByBearerToken(bearerToken);
		return mapping.map(service.resetBearerToken(token), ResponseTokenDto.class);
	}
	
	@DeleteMapping("/all/{accountId}")
	public String deleteAllAuthToken(@PathVariable String accountId) {
		service.removeAllAuthTokens(accountId);
		return "All Authentication Tokens have been removed";
	}
	
}
