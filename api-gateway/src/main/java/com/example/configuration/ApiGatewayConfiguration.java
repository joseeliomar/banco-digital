package com.example.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(x -> x.path("/contaDigitalPessoaFisica/**").uri("lb://conta-digital-cliente-ms"))
				.route(x -> x.path("/contaDigitalPessoaJuridica/**").uri("lb://conta-digital-cliente-ms"))
				.build();
    }
}
