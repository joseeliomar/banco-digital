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
				.route(x -> x.path("/contaPessoaFisica/**").uri("lb://conta-corrente-poupanca-ms"))
				.route(x -> x.path("/contaPessoaJuridica/**").uri("lb://conta-corrente-poupanca-ms"))
				.route(x -> x.path("/depositoContaCorrentePessoaFisica/**").uri("lb://deposito-ms"))
				.route(x -> x.path("/depositoContaCorrentePessoaJuridica/**").uri("lb://deposito-ms"))
				.route(x -> x.path("/endereco/**").uri("lb://endereco-ms"))
				.route(x -> x.path("/itemExtratoContaPessoaFisica/**").uri("lb://extrato-bancario-ms"))
				.route(x -> x.path("/itemExtratoContaPessoaJuridica/**").uri("lb://extrato-bancario-ms"))
				.route(x -> x.path("/geradorNumeroContaAfins/**").uri("lb://gerador-numero-conta-afins-ms"))
				.route(x -> x.path("/saqueContaCorrentePessoaFisica/**").uri("lb://saque-ms"))
				.route(x -> x.path("/saqueContaCorrentePessoaJuridica/**").uri("lb://saque-ms"))
				.build();
    }
}
