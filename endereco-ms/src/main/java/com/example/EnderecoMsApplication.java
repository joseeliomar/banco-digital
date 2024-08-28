package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(
		info = @Info(
				title = "Documentação do microsserviço endereco-ms", 
				version = "0.1", 
				description = "Microsserviço responsável pelo gerenciamento dos "
						+ "endereços."))
public class EnderecoMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnderecoMsApplication.class, args);
	}

}
