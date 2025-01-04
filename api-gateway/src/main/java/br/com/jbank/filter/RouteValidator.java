package br.com.jbank.filter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

	public static final List<String> openApiEndpoints = Arrays.asList("/auth/token", "/eureka");
	
	public Predicate<ServerHttpRequest> isSecured =
			request -> openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
}
