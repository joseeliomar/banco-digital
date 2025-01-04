package br.com.jbank.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	public String secret;

	public String generateToken(String cpf) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create().withIssuer("auth-ms").withSubject(cpf)
					.withExpiresAt(generateExperationTime()).sign(algorithm);
			return token;
		} catch (JWTCreationException exception) {
			throw new RuntimeException("Error while generating token", exception);
		}
	}

//	public void validateToken(String token) {
//		try {
//			Algorithm algorithm = Algorithm.HMAC256(secret);
//			JWT.require(algorithm).withIssuer("auth-ms").build().verify(token);
//		} catch (JWTVerificationException exception) {
//			throw new RuntimeException("Error while validating token", exception);
//		}
//	}

	private Instant generateExperationTime() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}
