package com.example.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.config.TestConfigs;
import com.example.dto.ContaDigitalPessoaFisicaInsercaoDto;
import com.example.integrationtests.testcontainers.ConfiguracaoAmbienteTestesParaUsoContainers;
import com.example.model.ContaDigitalPessoaFisica;
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
class ContaDigitalPessoaFisicaControllerIntegrationTest extends ConfiguracaoAmbienteTestesParaUsoContainers {
	
	private static final String CAMINHO_BASE = "/contaDigitalPessoaFisica/";

	private RequestSpecification requestSpecification;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeAll
	public void setup() {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		requestSpecification = new RequestSpecBuilder()
				.setBasePath(CAMINHO_BASE)
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@DisplayName("Quando insere conta digital para pessoa física com sucesso deve ser retornada "
			+ "a URI (localização do recurso criado) e o código de status 201")
	@Test
	void testInsereContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadaUriMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		String cpf = "12345678901";

		ContaDigitalPessoaFisicaInsercaoDto contaDigitalPessoaFisicaInsercaoDto = new ContaDigitalPessoaFisicaInsercaoDto(
				"1234567890", "0000000011", "12345678", "19980001234", "fulano@email.com", null, null, cpf,
				"Fulano de Tal", LocalDate.of(2001, 1, 1), "Fulana de Tal");

		String localizacaoRecursoCriado = "http://localhost:" + TestConfigs.SERVER_PORT +  CAMINHO_BASE
				+ contaDigitalPessoaFisicaInsercaoDto.getCpf();

		String conteudoBodyResposta = given().spec(requestSpecification).contentType(ContentType.JSON)
				.body(contaDigitalPessoaFisicaInsercaoDto).when().post().then().statusCode(201).assertThat()
				.header("Location", equalTo(localizacaoRecursoCriado)).extract().body().asString();

		ContaDigitalPessoaFisica contaDigitalPessoaFisicaInserida = objectMapper.readValue(conteudoBodyResposta,
				ContaDigitalPessoaFisica.class);

		assertNotNull(contaDigitalPessoaFisicaInserida);
		assertNotNull(contaDigitalPessoaFisicaInserida.getAgencia());
		assertNotNull(contaDigitalPessoaFisicaInserida.getConta());
		assertNotNull(contaDigitalPessoaFisicaInserida.getSenha());
		assertNotNull(contaDigitalPessoaFisicaInserida.getTelefone());
		assertNotNull(contaDigitalPessoaFisicaInserida.getEmail());
		assertNotNull(contaDigitalPessoaFisicaInserida.getDataHoraCadastro());
		assertNull(contaDigitalPessoaFisicaInserida.getDataHoraAlteracao(),
				() -> "Não deve haver data e hora da alteração, pois a conta digital ainda não foi alterada, "
						+ "apenas inserida no banco de dados");
		assertNotNull(contaDigitalPessoaFisicaInserida.getCpf());
		assertNotNull(contaDigitalPessoaFisicaInserida.getNomeCompleto());
		assertNotNull(contaDigitalPessoaFisicaInserida.getDataNascimento());
		assertNotNull(contaDigitalPessoaFisicaInserida.getNomeCompletoMae());
		
		assertEquals(contaDigitalPessoaFisicaInsercaoDto.getAgencia(), contaDigitalPessoaFisicaInserida.getAgencia());
		assertEquals(contaDigitalPessoaFisicaInsercaoDto.getConta(), contaDigitalPessoaFisicaInserida.getConta());
		assertEquals(contaDigitalPessoaFisicaInsercaoDto.getSenha(), contaDigitalPessoaFisicaInserida.getSenha());
		assertEquals(contaDigitalPessoaFisicaInsercaoDto.getTelefone(), contaDigitalPessoaFisicaInserida.getTelefone());
		assertEquals(contaDigitalPessoaFisicaInsercaoDto.getEmail(), contaDigitalPessoaFisicaInserida.getEmail());
		assertEquals(contaDigitalPessoaFisicaInsercaoDto.getCpf(), contaDigitalPessoaFisicaInserida.getCpf());
		assertEquals(contaDigitalPessoaFisicaInsercaoDto.getNomeCompleto(), contaDigitalPessoaFisicaInserida.getNomeCompleto());
		assertEquals(contaDigitalPessoaFisicaInsercaoDto.getDataNascimento(), contaDigitalPessoaFisicaInserida.getDataNascimento());
		assertEquals(contaDigitalPessoaFisicaInsercaoDto.getNomeCompletoMae(), contaDigitalPessoaFisicaInserida.getNomeCompletoMae());
	}
}
