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
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.DadosParaDepositoContaPessoaJuridicaDto;
import com.example.dto.DetalhesExcecaoDto;
import com.example.enumeration.TipoConta;
import com.example.integrationtests.dto.ContaDigitalPessoaJuridicaInsercaoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
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
class DepositoContaPessoaJuridicaControllerIntegrationTests {
	
	private static final String CAMINHO_BASE = "/depositoContaCorrentePessoaJuridica/";
	
	private static final double VALOR_PRIMEIRO_DEPOSITO = 435.50;
	
	private static final double VALOR_SEGUNDO_DEPOSITO = 500.99;
	
	private RequestSpecification requestSpecification;
	
	private RequestSpecification requestSpecificationContaDigitalPessoaJuridica;
	
	private RequestSpecification requestSpecificationContaPessoaJuridica;
	
	@Autowired
	private MetodosEmComumIntegrationTest metodosEmComumIntegrationTest;
	
	@Autowired
	private TestConfigs testConfigs;
	
	@Autowired
	private ObjectMapper objectMapper;

	private DadosContaClientePessoaJuridica dadosContaCliente1;
	
	@BeforeAll
	public void setupAll() {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		requestSpecification = new RequestSpecBuilder()
				.setBasePath(CAMINHO_BASE)
				.setPort(testConfigs.getServerPort())
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		requestSpecificationContaDigitalPessoaJuridica = new RequestSpecBuilder()
				.setBasePath("/contaDigitalPessoaJuridica/")
				.setPort(metodosEmComumIntegrationTest.obterUmaPortaMicrosservico("conta-digital-cliente-ms"))
				.build();
		
		requestSpecificationContaPessoaJuridica = new RequestSpecBuilder()
				.setBasePath("/contaPessoaJuridica/")
				.setPort(metodosEmComumIntegrationTest.obterUmaPortaMicrosservico("conta-corrente-poupanca-ms"))
				.build();
		
		dadosContaCliente1 = new DadosContaClientePessoaJuridica("0000000013",
				"12345678", "19980001234", "fulano@email.com", "12345678990002", "Fábrica Tal");
		
		var contaDigitalPessoaJuridicaInsercao = new ContaDigitalPessoaJuridicaInsercaoDto(
				dadosContaCliente1.agencia(), 
				dadosContaCliente1.senha(), 
				dadosContaCliente1.telefone(), 
				dadosContaCliente1.email(), 
				dadosContaCliente1.cnpjCliente(),
				dadosContaCliente1.razaoSocial());
		
		insereContaDigitalPessoaJuridica(contaDigitalPessoaJuridicaInsercao);
	}
	
	@DisplayName("Quando efetua o primeiro depósito em conta corrente de pessoa jurídica com sucesso"
			+ " deve ser retornado o código de status 200")
	@Order(1)
	@Test
	void testEfetuaPrimeiroDeposito_ComSucesso_CodigoStatus200()
			throws JsonProcessingException, Exception {
		efetuaDepositoComSucesso(dadosContaCliente1.cnpjCliente(), VALOR_PRIMEIRO_DEPOSITO);
	}

	@DisplayName("Confere se o saldo da conta corrente está correto após o primeiro "
			+ "depósito na conta corrente do cliente")
	@Order(2)
	@Test
	void testConfereSeSaldoContaCorrenteEstaCorretoAposPrimeiroDepositoContaCorrenteCliente()
			throws JsonProcessingException, Exception {
		double valorEsperado = VALOR_PRIMEIRO_DEPOSITO;
		confereSeSaldoContaCorrenteEstaCorreto(valorEsperado);
	}

	@DisplayName("Confere se o saldo da conta poupança está correto após o primeiro "
			+ "depósito na conta corrente do cliente")
	@Order(3)
	@Test
	void testConfereSeSaldoContaPoupancaEstaCorretoAposPrimeiroDepositoContaCorrenteCliente()
			throws JsonProcessingException, Exception {
		confereSeSaldoContaPoupancaEstaCorreto();
	}
	
	@DisplayName("Quando efetua o segundo depósito em conta corrente de pessoa jurídica com sucesso"
			+ " deve ser retornado o código de status 200")
	@Order(4)
	@Test
	void testEfetuaSegundoDeposito_ComSucesso_CodigoStatus200()
			throws JsonProcessingException, Exception {
		efetuaDepositoComSucesso(dadosContaCliente1.cnpjCliente(), VALOR_SEGUNDO_DEPOSITO);
	}

	@DisplayName("Confere se o saldo da conta corrente está correto após o segundo "
			+ "depósito na conta corrente do cliente")
	@Order(5)
	@Test
	void testConfereSeSaldoContaCorrenteEstaCorretoAposSegundoDepositoContaCorrenteCliente()
			throws JsonProcessingException, Exception {
		double saldoEsperado = VALOR_PRIMEIRO_DEPOSITO + VALOR_SEGUNDO_DEPOSITO;
		confereSeSaldoContaCorrenteEstaCorreto(saldoEsperado);
	}
	
	@DisplayName("Confere se o saldo da conta poupança está correto após o segundo "
			+ "depósito na conta corrente do cliente")
	@Order(6)
	@Test
	void testConfereSeSaldoContaPoupancaEstaCorretoAposSegundoDepositoContaCorrenteCliente()
			throws JsonProcessingException, Exception {
		confereSeSaldoContaPoupancaEstaCorreto();
	}
	
	@DisplayName("Quando tenta depositar um valor negativo em conta corrente de pessoa jurídica "
			+ " deve ser lançada uma exceção")
	@Test
	void testEfetuaDeposito_ValorNegativo_DeveSerLançadaUmaExcecao()
			throws JsonProcessingException, Exception {
		double valorNegativo = -100.0;
		String mensagemEsperada = "Não é possível depositar um valor negativo.";
		
		Response response = efetuaDeposito(dadosContaCliente1.cnpjCliente(), valorNegativo);
		
		String bodyResposta = response.then().extract().body().asString();
		
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(bodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	/**
	 * Insere conta digital para pessoa jurídica 
	 * 
	 * @param contaDigitalPessoaJuridicaInsercaoDto
	 */
	private void insereContaDigitalPessoaJuridica(ContaDigitalPessoaJuridicaInsercaoDto contaDigitalPessoaJuridicaInsercaoDto) {
		given()
			.spec(requestSpecificationContaDigitalPessoaJuridica)
			.contentType(ContentType.JSON)
			.body(contaDigitalPessoaJuridicaInsercaoDto)
		.when()
			.post()
		.then()
			.statusCode(201);
	}
	
	/**
	 * Efetua depósito com sucesso.
	 * 
	 * @param cnpjCliente
	 * @param valorDeposito
	 */
	private void efetuaDepositoComSucesso(String cnpjCliente, double valorDeposito) {
		Response response = efetuaDeposito(cnpjCliente, valorDeposito);
		response.then().statusCode(200);
	}
	
	/**
	 * Efetua depósito.
	 * 
	 * @param cnpjCliente
	 * @param valorDeposito
	 */
	private Response efetuaDeposito(String cnpjCliente, double valorDeposito) {
		DadosParaDepositoContaPessoaJuridicaDto dadosParaDepositoDto = new DadosParaDepositoContaPessoaJuridicaDto(
				cnpjCliente, valorDeposito);

		Response response = given()
			.spec(requestSpecification)
			.contentType(ContentType.JSON)
			.body(dadosParaDepositoDto)
		.when()
			.post();
		
		return response;
	}
	
	/**
	 * Busca uma conta de pessoa jurídica pelo CNPJ e o tipo de conta.
	 * 
	 * @param cnpj
	 * @param tipoConta
	 * @return a conta de pessoa jurídica encontrada.
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	private ContaPessoaJuridicaBuscaDto1 buscaContaPessoaJuridicaPeloCnpjTipoConta(String cnpj, TipoConta tipoConta)
			throws JsonProcessingException, JsonMappingException {
		String conteudoBodyResposta = given()
					.spec(requestSpecificationContaPessoaJuridica)
				.when()
					.get("{cnpj}/{tipoConta}", cnpj, tipoConta.name())
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		ContaPessoaJuridicaBuscaDto1 contaPessoaJuridicaBuscadaBancoDados = objectMapper
				.readValue(conteudoBodyResposta, ContaPessoaJuridicaBuscaDto1.class);
		return contaPessoaJuridicaBuscadaBancoDados;
	}
	
	/**
	 * Confere se o saldo conta corrente está corrento.
	 * 
	 * @param saldoEsperado
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	private void confereSeSaldoContaCorrenteEstaCorreto(double saldoEsperado) throws JsonProcessingException, JsonMappingException {
		ContaPessoaJuridicaBuscaDto1 contaCorrenteBuscada = buscaContaPessoaJuridicaPeloCnpjTipoConta(
				dadosContaCliente1.cnpjCliente(), TipoConta.CORRENTE);

		assertNotNull(contaCorrenteBuscada);

		double saldoAtualContaCorrente = contaCorrenteBuscada.getSaldo();
		
		assertEquals(saldoEsperado, saldoAtualContaCorrente);
	}
	
	/**
	 * Confere se o saldo da conta poupança está correto.
	 * 
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	private void confereSeSaldoContaPoupancaEstaCorreto() throws JsonProcessingException, JsonMappingException {
		double saldoEsperado = 0.0;
		
		ContaPessoaJuridicaBuscaDto1 contaPoupancaBuscada = buscaContaPessoaJuridicaPeloCnpjTipoConta(
				dadosContaCliente1.cnpjCliente(), TipoConta.POUPANCA);

		assertNotNull(contaPoupancaBuscada);

		double saldoAtualContaPoupanca = contaPoupancaBuscada.getSaldo();
		
		assertEquals(saldoEsperado, saldoAtualContaPoupanca);
	}
}
