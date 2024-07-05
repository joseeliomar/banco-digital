package com.example.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.example.dto.DadosContaDto;
import com.example.integrationtests.testcontainers.ConfiguracaoAmbienteTestesParaUsoContainers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ContaPessoaFisicaControllerIntegrationTest extends ConfiguracaoAmbienteTestesParaUsoContainers {

	private static final String CAMINHO_BASE = "/geradorNumeroContaAfins/";

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

	@DisplayName("Quando tenta gerar os dados da conta com sucesso devem ser retornados os"
			+ " dados da conta e o código de status 200")
	@Order(1)
	@Test
	void testGeraDadosConta_ComSucesso_DevemSerRetornadosDadosContaMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		var numeroContaExperado = "00000000001";
		var digitoVerificadorContaExperado = 7;
		testaGeracaoDadosConta(numeroContaExperado, digitoVerificadorContaExperado);
	}

	@DisplayName("Quando tenta gerar os dados de outra conta com sucesso devem ser retornados os"
			+ " dados dessa conta e o código de status 200")
	@Order(2)
	@Test
	void testGeraDadosOutraConta_ComSucesso_DevemSerRetornadosDadosContaMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		var numeroContaExperado = "00000000002";
		var digitoVerificadorContaExperado = 3;
		testaGeracaoDadosConta(numeroContaExperado, digitoVerificadorContaExperado);
	}

	/**
	 * Testa a geração dos dados da conta.
	 * 
	 * @param numeroContaExperado
	 * @param digitoVerificadorContaExperado
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	private void testaGeracaoDadosConta(String numeroContaExperado, int digitoVerificadorContaExperado)
			throws JsonProcessingException, JsonMappingException {
		DadosContaDto dadosConta = geraDadosConta();
		verificaDadosContaRetornados(numeroContaExperado, digitoVerificadorContaExperado, dadosConta);
	}
	
	/**
	 * Verifica os dados da conta retornados.
	 * 
	 * @param numeroContaExperado
	 * @param digitoVerificadorContaExperado
	 * @param dadosConta
	 */
	private void verificaDadosContaRetornados(String numeroContaExperado, int digitoVerificadorContaExperado,
			DadosContaDto dadosConta) {
		assertNotNull(dadosConta);
		assertNotNull(dadosConta.numeroConta());
		assertNotNull(dadosConta.digitoVerificadorConta());

		assertEquals(numeroContaExperado, dadosConta.numeroConta());
		assertEquals(digitoVerificadorContaExperado, dadosConta.digitoVerificadorConta());
	}

	/**
	 * Busca os dados da conta.
	 * 
	 * @return Os dados da conta
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	private DadosContaDto geraDadosConta() throws JsonProcessingException, JsonMappingException {
		String conteudoBodyResposta = given()
					.spec(requestSpecification)
				.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();

		DadosContaDto dadosContaDto = objectMapper.readValue(conteudoBodyResposta, DadosContaDto.class);
		return dadosContaDto;
	}
}
