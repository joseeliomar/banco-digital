package br.com.jbank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    public void validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWT.require(algorithm).withIssuer("auth-ms").build().verify(token);
		} catch (JWTVerificationException exception) {
			throw new RuntimeException("Error while validating token", exception);
		}
	}

}