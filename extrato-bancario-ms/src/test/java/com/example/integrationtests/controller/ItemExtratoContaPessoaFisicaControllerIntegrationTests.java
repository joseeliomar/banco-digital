package com.example.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.example.dto.DetalhesExcecaoDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInseridoDto;
import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.integrationtests.testcontainers.ConfiguracaoAmbienteTestesParaUsoContainers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ItemExtratoContaPessoaFisicaControllerIntegrationTests extends ConfiguracaoAmbienteTestesParaUsoContainers {
	
	private static final String CAMINHO_BASE = "/itemExtratoContaPessoaFisica/";
	
	private static final String CPF_1 = "12345678901";

	private RequestSpecification requestSpecification;
	
	@Autowired
	private TestConfigs testConfigs;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final Long ID_SEM_ASSOCIACAO_COM_ALGUM_ITEM_EXTRATO_CONTA = 999999999999999999L; // Não há nenhum item de extrato de conta com esse ID
	
	private Long idPrimeiroItemExtratoContaPessoaFisicaInseridoBancoDados;

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
	
	@DisplayName("Quando insere um item de extrato de conta de pessoa física com sucesso"
			+ " deve ser retornado o header Location e o código de status 201")
	@Order(1)
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComSucesso_DeveSerRetornadoHeaderLocationMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		ItemExtratoContaPessoaFisicaInsercaoDto itemExtratoContaPessoaFisicaInsercaoDto = new ItemExtratoContaPessoaFisicaInsercaoDto(
				TipoConta.CORRENTE, Operacao.DEPOSITO, "Depósito", Banco.BANCO_BRASIL, "0000000011", "1234567890",
				100.0, CPF_1);

		Response response = insereItemExtratoContaPessoaFisica(itemExtratoContaPessoaFisicaInsercaoDto);
				
		response.then().statusCode(201);
				
		String valorHeaderLocation = response.getHeader("Location");
		String conteudoBodyResposta = response.asString();

		ItemExtratoContaPessoaFisicaInseridoDto itemExtratoContaPessoaFisicaInserido = objectMapper.readValue(conteudoBodyResposta, ItemExtratoContaPessoaFisicaInseridoDto.class);
		
		assertNotNull(valorHeaderLocation);
		
		String sufixoUriRecursoCriado = CAMINHO_BASE + itemExtratoContaPessoaFisicaInserido.getId(); // não contém a porta
		assertTrue(valorHeaderLocation.endsWith(sufixoUriRecursoCriado),
				() -> "O valor presente no header Location não termina com o sufixo esperado.");
		
		assertNotNull(itemExtratoContaPessoaFisicaInserido);
		assertNotNull(itemExtratoContaPessoaFisicaInserido.getId());
		assertNotNull(itemExtratoContaPessoaFisicaInserido.getTipoContaDonaExtrato());
		assertNotNull(itemExtratoContaPessoaFisicaInserido.getOperacaoEfetuada());
		assertNotNull(itemExtratoContaPessoaFisicaInserido.getDataHoraCadastro());
		assertNotNull(itemExtratoContaPessoaFisicaInserido.getBancoDestino());
		assertNotNull(itemExtratoContaPessoaFisicaInserido.getAgenciaDestino());
		assertNotNull(itemExtratoContaPessoaFisicaInserido.getContaDestino());
		assertNotNull(itemExtratoContaPessoaFisicaInserido.getValor());
		assertNotNull(itemExtratoContaPessoaFisicaInserido.getCpfCliente());
		
		assertTrue(itemExtratoContaPessoaFisicaInserido.getId() > 0);
		assertEquals(itemExtratoContaPessoaFisicaInsercaoDto.getTipoContaDonaExtrato(), itemExtratoContaPessoaFisicaInserido.getTipoContaDonaExtrato());
		assertEquals(itemExtratoContaPessoaFisicaInsercaoDto.getOperacaoEfetuada(), itemExtratoContaPessoaFisicaInserido.getOperacaoEfetuada());
		assertEquals(itemExtratoContaPessoaFisicaInsercaoDto.getBancoDestino(), itemExtratoContaPessoaFisicaInserido.getBancoDestino());
		assertEquals(itemExtratoContaPessoaFisicaInsercaoDto.getAgenciaDestino(), itemExtratoContaPessoaFisicaInserido.getAgenciaDestino());
		assertEquals(itemExtratoContaPessoaFisicaInsercaoDto.getContaDestino(), itemExtratoContaPessoaFisicaInserido.getContaDestino());
		assertEquals(itemExtratoContaPessoaFisicaInsercaoDto.getValor(), itemExtratoContaPessoaFisicaInserido.getValor());
		assertEquals(itemExtratoContaPessoaFisicaInsercaoDto.getCpfCliente(), itemExtratoContaPessoaFisicaInserido.getCpfCliente());
		
		idPrimeiroItemExtratoContaPessoaFisicaInseridoBancoDados = itemExtratoContaPessoaFisicaInserido.getId();
	}

	private Response insereItemExtratoContaPessoaFisica(ItemExtratoContaPessoaFisicaInsercaoDto itemExtratoContaPessoaFisicaInsercaoDto) {
		Response response = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(itemExtratoContaPessoaFisicaInsercaoDto)
			.when()
				.post();
		return response;
	}
	
	@DisplayName("Quando deleta um item de extrato de conta de pessoa física com sucesso deve ser retornado o código de status 204")
	@Order(2)
	@Test
	void testDeletaItemExtratoContaPessoaFisica_ComSucesso_DeveSerRetornadoCodigoStatus204() {
		given()
			.spec(requestSpecification)
		.when()
			.delete("{idItemExtratoContaPessoaFisica}", idPrimeiroItemExtratoContaPessoaFisicaInseridoBancoDados)
		.then()
			.statusCode(204);
	}
	
	@DisplayName("Quando insere um item de extrato de conta de pessoa física sem sucesso não deve ser retornado o código de status 201")
	@Test
	void testInsereItemExtratoContaPessoaFisica_SemSucesso_NaoDeveSerRetornadoCodigoStatus201() throws JsonProcessingException, Exception {
		String cpfClienteNulo = null;
		String mensagemEsperada = "O CPF do cliente não foi informado.";
		
		ItemExtratoContaPessoaFisicaInsercaoDto itemExtratoContaPessoaFisicaInsercaoDto = new ItemExtratoContaPessoaFisicaInsercaoDto(
				TipoConta.CORRENTE, Operacao.DEPOSITO, "Depósito", Banco.BANCO_BRASIL, "0000000011", "1234567890",
				100.0, cpfClienteNulo);
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(itemExtratoContaPessoaFisicaInsercaoDto)
			.when()
				.post()
			.then()
				.assertThat().statusCode(not(equalTo(201)))
				.extract()
					.body()
						.asString();
		
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando remove um item de extrato de conta de pessoa física sem sucesso não deve ser retornado o código de status 204")
	@Test
	void testRemoveItemExtratoContaPessoaFisica_SemSucesso_NaoDeveSerRetornadoCodigoStatus204() throws Exception {
		String mensagemEsperada = "Não foi encontrado um item de extrato de conta de pessoa física com o código informado.";
		
		String conteudoBodyResposta = given()
			.spec(requestSpecification)
		.when()
			.delete("{idItemExtratoContaPessoaFisica}", ID_SEM_ASSOCIACAO_COM_ALGUM_ITEM_EXTRATO_CONTA)
		.then()
			.assertThat().statusCode(not(equalTo(204)))
		.extract()
			.body()
				.asString();
	
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
}
