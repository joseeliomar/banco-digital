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
import com.example.dto.ItemExtratoContaPessoaJuridicaInsercaoDto;
import com.example.dto.ItemExtratoContaPessoaJuridicaInseridoDto;
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
class ItemExtratoContaPessoaJuridicaControllerIntegrationTests extends ConfiguracaoAmbienteTestesParaUsoContainers {
	
	private static final String CAMINHO_BASE = "/itemExtratoContaPessoaJuridica/";
	
	private static final String CNPJ_1 = "12345678990001";

	private RequestSpecification requestSpecification;
	
	@Autowired
	private TestConfigs testConfigs;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final Long ID_SEM_ASSOCIACAO_COM_ALGUM_ITEM_EXTRATO_CONTA = 999999999999999999L; // Não há nenhum item de extrato de conta com esse ID
	
	private Long idPrimeiroItemExtratoContaPessoaJuridicaInseridoBancoDados;
	
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
	
	@DisplayName("Quando insere um item de extrato de conta de pessoa jurídica com sucesso"
			+ " deve ser retornado o header Location e o código de status 201")
	@Order(1)
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComSucesso_DeveSerRetornadoHeaderLocationMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		ItemExtratoContaPessoaJuridicaInsercaoDto itemExtratoContaPessoaJuridicaInsercaoDto = new ItemExtratoContaPessoaJuridicaInsercaoDto(
				TipoConta.CORRENTE, Operacao.DEPOSITO, "Depósito", Banco.BANCO_BRASIL, "0000000011", "1234567890",
				100.0, CNPJ_1);

		Response response = insereItemExtratoContaPessoaJuridica(itemExtratoContaPessoaJuridicaInsercaoDto);
				
		response.then().statusCode(201);
				
		String valorHeaderLocation = response.getHeader("Location");
		String conteudoBodyResposta = response.asString();

		ItemExtratoContaPessoaJuridicaInseridoDto itemExtratoContaPessoaJuridicaInserido = objectMapper.readValue(conteudoBodyResposta, ItemExtratoContaPessoaJuridicaInseridoDto.class);
		
		assertNotNull(valorHeaderLocation);
		
		String sufixoUriRecursoCriado = CAMINHO_BASE + itemExtratoContaPessoaJuridicaInserido.getId(); // não contém a porta
		assertTrue(valorHeaderLocation.endsWith(sufixoUriRecursoCriado),
				() -> "O valor presente no header Location não termina com o sufixo esperado.");
		
		assertNotNull(itemExtratoContaPessoaJuridicaInserido);
		assertNotNull(itemExtratoContaPessoaJuridicaInserido.getId());
		assertNotNull(itemExtratoContaPessoaJuridicaInserido.getTipoContaDonaExtrato());
		assertNotNull(itemExtratoContaPessoaJuridicaInserido.getOperacaoEfetuada());
		assertNotNull(itemExtratoContaPessoaJuridicaInserido.getDataHoraCadastro());
		assertNotNull(itemExtratoContaPessoaJuridicaInserido.getBancoDestino());
		assertNotNull(itemExtratoContaPessoaJuridicaInserido.getAgenciaDestino());
		assertNotNull(itemExtratoContaPessoaJuridicaInserido.getContaDestino());
		assertNotNull(itemExtratoContaPessoaJuridicaInserido.getValor());
		assertNotNull(itemExtratoContaPessoaJuridicaInserido.getCnpjCliente());
		
		assertTrue(itemExtratoContaPessoaJuridicaInserido.getId() > 0);
		assertEquals(itemExtratoContaPessoaJuridicaInsercaoDto.getTipoContaDonaExtrato(), itemExtratoContaPessoaJuridicaInserido.getTipoContaDonaExtrato());
		assertEquals(itemExtratoContaPessoaJuridicaInsercaoDto.getOperacaoEfetuada(), itemExtratoContaPessoaJuridicaInserido.getOperacaoEfetuada());
		assertEquals(itemExtratoContaPessoaJuridicaInsercaoDto.getBancoDestino(), itemExtratoContaPessoaJuridicaInserido.getBancoDestino());
		assertEquals(itemExtratoContaPessoaJuridicaInsercaoDto.getAgenciaDestino(), itemExtratoContaPessoaJuridicaInserido.getAgenciaDestino());
		assertEquals(itemExtratoContaPessoaJuridicaInsercaoDto.getContaDestino(), itemExtratoContaPessoaJuridicaInserido.getContaDestino());
		assertEquals(itemExtratoContaPessoaJuridicaInsercaoDto.getValor(), itemExtratoContaPessoaJuridicaInserido.getValor());
		assertEquals(itemExtratoContaPessoaJuridicaInsercaoDto.getCnpjCliente(), itemExtratoContaPessoaJuridicaInserido.getCnpjCliente());
		
		idPrimeiroItemExtratoContaPessoaJuridicaInseridoBancoDados = itemExtratoContaPessoaJuridicaInserido.getId();
	}

	private Response insereItemExtratoContaPessoaJuridica(ItemExtratoContaPessoaJuridicaInsercaoDto itemExtratoContaPessoaJuridicaInsercaoDto) {
		Response response = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(itemExtratoContaPessoaJuridicaInsercaoDto)
			.when()
				.post();
		return response;
	}
	
	@DisplayName("Quando deleta um item de extrato de conta de pessoa jurídica com sucesso deve ser retornado o código de status 204")
	@Order(2)
	@Test
	void testDeletaItemExtratoContaPessoaJuridica_ComSucesso_DeveSerRetornadoCodigoStatus204() {
		given()
			.spec(requestSpecification)
		.when()
			.delete("{idItemExtratoContaPessoaJuridica}", idPrimeiroItemExtratoContaPessoaJuridicaInseridoBancoDados)
		.then()
			.statusCode(204);
	}
	
	@DisplayName("Quando insere um item de extrato de conta de pessoa jurídica sem sucesso não deve ser retornado o código de status 201")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus201() throws JsonProcessingException, Exception {
		String cnpjClienteNulo = null;
		String mensagemEsperada = "O CNPJ do cliente não foi informado.";
		
		ItemExtratoContaPessoaJuridicaInsercaoDto itemExtratoContaPessoaJuridicaInsercaoDto = new ItemExtratoContaPessoaJuridicaInsercaoDto(
				TipoConta.CORRENTE, Operacao.DEPOSITO, "Depósito", Banco.BANCO_BRASIL, "0000000011", "1234567890",
				100.0, cnpjClienteNulo);
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(itemExtratoContaPessoaJuridicaInsercaoDto)
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
	
	@DisplayName("Quando remove um item de extrato de conta de pessoa jurídica sem sucesso não deve ser retornado o código de status 204")
	@Test
	void testRemoveItemExtratoContaPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus204() throws Exception {
		String mensagemEsperada = "Não foi encontrado um item de extrato de conta de pessoa jurídica com o código informado.";
		
		String conteudoBodyResposta = given()
			.spec(requestSpecification)
		.when()
			.delete("{idItemExtratoContaPessoaJuridica}", ID_SEM_ASSOCIACAO_COM_ALGUM_ITEM_EXTRATO_CONTA)
		.then()
			.assertThat().statusCode(not(equalTo(204)))
		.extract()
			.body()
				.asString();
	
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
}
