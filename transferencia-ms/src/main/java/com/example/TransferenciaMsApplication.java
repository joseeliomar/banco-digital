package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@OpenAPIDefinition(
		info = @Info(
				title = "Documentação do microsserviço transferencia-ms", 
				version = "0.1", 
				description = "Microsserviço responsável pela realização das"
						+ " transferências bancárias."))
public class TransferenciaMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransferenciaMsApplication.class, args);
	}

}
