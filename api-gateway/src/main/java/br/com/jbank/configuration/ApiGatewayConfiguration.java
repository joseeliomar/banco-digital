package br.com.jbank.configuration;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.jbank.filter.AuthenticationFilter;

@Configuration
public class ApiGatewayConfiguration {
	
	@Autowired
	private AuthenticationFilter authenticationFilter;

    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		Function<GatewayFilterSpec, UriSpec> filtroAutenticacao = f -> f
				.filter(authenticationFilter.apply(new AuthenticationFilter.Config()));
 
		return builder.routes()
				.route(x -> x.path("/contaDigitalPessoaFisica/**").filters(filtroAutenticacao).uri("lb://conta-digital-cliente-ms"))
				.route(x -> x.path("/contaDigitalPessoaJuridica/**").filters(filtroAutenticacao).uri("lb://conta-digital-cliente-ms"))
				.route(x -> x.path("/contaPessoaFisica/**").filters(filtroAutenticacao).uri("lb://conta-corrente-poupanca-ms"))
				.route(x -> x.path("/contaPessoaJuridica/**").filters(filtroAutenticacao).uri("lb://conta-corrente-poupanca-ms"))
				.route(x -> x.path("/depositoContaCorrentePessoaFisica/**").filters(filtroAutenticacao).uri("lb://deposito-ms"))
				.route(x -> x.path("/depositoContaCorrentePessoaJuridica/**").filters(filtroAutenticacao).uri("lb://deposito-ms"))
				.route(x -> x.path("/endereco/**").filters(filtroAutenticacao).uri("lb://endereco-ms"))
				.route(x -> x.path("/itemExtratoContaPessoaFisica/**").filters(filtroAutenticacao).uri("lb://extrato-bancario-ms"))
				.route(x -> x.path("/extratoContaPessoaFisica/**").filters(filtroAutenticacao).uri("lb://extrato-bancario-ms"))
				.route(x -> x.path("/itemExtratoContaPessoaJuridica/**").filters(filtroAutenticacao).uri("lb://extrato-bancario-ms"))
				.route(x -> x.path("/geradorNumeroContaAfins/**").filters(filtroAutenticacao).uri("lb://gerador-numero-conta-afins-ms"))
				.route(x -> x.path("/saqueContaCorrentePessoaFisica/**").filters(filtroAutenticacao).uri("lb://saque-ms"))
				.route(x -> x.path("/saqueContaCorrentePessoaJuridica/**").filters(filtroAutenticacao).uri("lb://saque-ms"))
				.route(x -> x.path("/transferenciaDinheiroPessoaFisica/**").filters(filtroAutenticacao).uri("lb://transferencia-ms"))
				.route(x -> x.path("/auth/**").uri("lb://auth-ms"))
				.build();
    }
}
