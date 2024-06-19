package com.example.integrationtests.controller;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.config.TestConfigs;
import com.example.dto.DadosParaDepositoContaPessoaFisicaDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DepositoContaPessoaFisicaControllerIntegrationTests {
	
	private static final String CAMINHO_BASE = "/depositoContaCorrentePessoaFisica/";
	
	private RequestSpecification requestSpecification;
	
	@Autowired
	private TestConfigs testConfigs;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeAll
	public void setupAll() {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		requestSpecification = new RequestSpecBuilder()
				.setBasePath(CAMINHO_BASE)
				.setPort(testConfigs.getServerPort())
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@DisplayName("Quando insere um depósito em conta corrente de pessoa física com sucesso"
			+ " deve ser retornado o código de status 201")
	@Order(1)
	@Test
	void testEfetuaDeposito_ComSucesso_DeveSerRetornadoHeaderLocationMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		DadosParaDepositoContaPessoaFisicaDto dadosParaDepositoDto = new DadosParaDepositoContaPessoaFisicaDto(
				"12345678901", 100.0, "0000000011", "1234567890");

		given()
			.spec(requestSpecification).contentType(ContentType.JSON)
			.body(dadosParaDepositoDto)
		.when()
			.post()
		.then()
			.statusCode(201);
		
	}

}
