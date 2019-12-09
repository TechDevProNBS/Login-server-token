package com.nationwide.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nationwide.exceptions.NotFoundException;
import com.nationwide.exceptions.UnauthorisedException;
import com.nationwide.persistence.domain.Token;
import com.nationwide.persistence.repository.TokenRepository;

@Service
public class TokenService {

	@Autowired
	private TokenRepository tokenRepository;

	public Token assignToken(String usernameId) {
		Token token = createAuthToken();
		token.setUsernameId(usernameId);
		token.setExpireDate(getDateNextTenMinutes());
		return tokenRepository.saveAndFlush(token);
	}
	
	private Date getDateNextTenMinutes() {
		Date date = new Date();
		Long currentTime = date.getTime();
		date.setTime(currentTime + 600000L);
		return date;
	}
	
	private Token createAuthToken() {
		Token token = new Token();
		token.setBearerToken(createBearerToken());
		return token;
	}
	
	private String createBearerToken() {
		return RandomStringUtils.randomAlphanumeric(50);
	}
	
	public Token getByBearerToken(String bearerToken) {
		Token token = tokenRepository.findByBearerToken(bearerToken).orElseThrow(() -> new NotFoundException("Bearer Token Not found or has expired"));	
		return removeIfOutdated(token);
	}
	
	private Token removeIfOutdated(Token token) {
		if(token.getExpireDate().before(new Date())) {
			tokenRepository.delete(token);
			throw new UnauthorisedException("Bearer Token Expired. It has been deleted");
		}
		return token;
	}
	
	public Token resetBearerToken(Token token) {
		System.out.println(token.getBearerToken());
		token.setBearerToken(createBearerToken());
		System.out.println(token.getBearerToken());
		token.setExpireDate(getDateNextTenMinutes());
		tokenRepository.flush();
		return token;
	}
	
	public void removeAllAuthTokens(String usernameId) {
		List<Token> tokens = tokenRepository.findByUsernameId(usernameId);
		tokens.stream().forEach(token -> tokenRepository.delete(token));
	}
	
}
