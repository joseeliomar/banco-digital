package br.com.jbank.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import br.com.jbank.JWTUtil;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private RouteValidator validator;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	public AuthenticationFilter() {
		super(Config.class);
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			if (validator.isSecured.test(exchange.getRequest())) {
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("Missing authorization header");
				}
				
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				String autenticationType = "Bearer "; // must have the space
				if (authHeader != null && authHeader.startsWith(autenticationType)) {
					authHeader = authHeader.substring(autenticationType.length());
				}
				try {
					jwtUtil.validateToken(authHeader);
				} catch (Exception e) {
					throw new RuntimeException("Access denied. Invalid token");
				}
			}
			return chain.filter(exchange);
		});
	}
	
	public static class Config {

	}

}
