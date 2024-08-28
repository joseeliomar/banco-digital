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
				title = "Documentação do microsserviço extrato-bancario-ms", 
				version = "0.1", 
				description = "Microsserviço responsável pela geração dos extratos "
						+ "bancários."))
public class ExtratoBancarioMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExtratoBancarioMsApplication.class, args);
	}

}
