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
import com.example.dto.ContaPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaPessoaJuridicaAlteradaDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.ContaPessoaJuridicaInsercaoDto;
import com.example.dto.ContaPessoaJuridicaInseridaDto;
import com.example.dto.DetalhesExcecaoDto;
import com.example.enumeration.TipoConta;
import com.example.integrationtests.dto.DadosContaPessoaJuridica;
import com.example.integrationtests.testcontainers.ConfiguracaoAmbienteTestesParaUsoContainers;
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
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ContaPessoaJuridicaControllerIntegrationTest extends ConfiguracaoAmbienteTestesParaUsoContainers {
	
	private static final String CAMINHO_BASE = "/contaPessoaJuridica/";
	
	private static final String CNPJ_1 = "12345678990001";
	
	private static final String CNPJ_SEM_ASSOCIACAO_COM_ALGUM_CONTA = "99999999999999"; // Não há nenhum conta com esse CNPJ
	
	private static final Long ID_SEM_ASSOCIACAO_COM_ALGUM_CONTA = 999999999999999999L; // Não há nenhum conta com esse ID

	private RequestSpecification requestSpecification;
	
	@Autowired
	private TestConfigs testConfigs;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private DadosContaPessoaJuridica dadosPrimeiraContaCorrentePessoaJuridicaInseridaBancoDados;
	
	private DadosContaPessoaJuridica dadosPrimeiraContaPoupancaPessoaJuridicaInseridaBancoDados;
	
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
	
	@DisplayName("Quando insere conta corrente com sucesso deve ser retornado o header Location e o código de status 201")
	@Order(1)
	@Test
	void testInsereContaCorrentePessoaJuridica_ComSucesso_DeveSerRetornadoHeaderLocationMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		TipoConta tipoConta = TipoConta.CORRENTE;
		ContaPessoaJuridicaInsercaoDto contaPessoaJuridicaInsercaoDto = new ContaPessoaJuridicaInsercaoDto(tipoConta, 0, CNPJ_1);

		ContaPessoaJuridicaInseridaDto contaPessoaJuridicaInserida = testaInsercaoBemSucedidaContaPessoaJuridica(
				contaPessoaJuridicaInsercaoDto);
		
		dadosPrimeiraContaCorrentePessoaJuridicaInseridaBancoDados = new DadosContaPessoaJuridica(contaPessoaJuridicaInserida.getId(),
				contaPessoaJuridicaInserida.getCnpj(), contaPessoaJuridicaInserida.getTipoConta());
	}
	
	@DisplayName("Quando insere conta poupança com sucesso deve ser retornado o header Location e o código de status 201")
	@Order(2)
	@Test
	void testInsereContaPoupancaPessoaJuridica_ComSucesso_DeveSerRetornadoHeaderLocationMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		TipoConta tipoConta = TipoConta.POUPANCA;
		ContaPessoaJuridicaInsercaoDto contaPessoaJuridicaInsercaoDto = new ContaPessoaJuridicaInsercaoDto(tipoConta, 0, CNPJ_1);

		ContaPessoaJuridicaInseridaDto contaPessoaJuridicaInserida = testaInsercaoBemSucedidaContaPessoaJuridica(
				contaPessoaJuridicaInsercaoDto);
		
		dadosPrimeiraContaPoupancaPessoaJuridicaInseridaBancoDados = new DadosContaPessoaJuridica(contaPessoaJuridicaInserida.getId(),
				contaPessoaJuridicaInserida.getCnpj(), contaPessoaJuridicaInserida.getTipoConta());
	}

	private ContaPessoaJuridicaInseridaDto testaInsercaoBemSucedidaContaPessoaJuridica(
			ContaPessoaJuridicaInsercaoDto contaPessoaJuridicaInsercaoDto)
			throws JsonProcessingException, JsonMappingException {
		Response response = insereContaPessoaJuridica(contaPessoaJuridicaInsercaoDto);
				
		response.then().statusCode(201);
				
		String valorHeaderLocation = response.getHeader("Location");
		String conteudoBodyResposta = response.asString();

		ContaPessoaJuridicaInseridaDto contaPessoaJuridicaInserida = objectMapper.readValue(conteudoBodyResposta,
				ContaPessoaJuridicaInseridaDto.class);
		
		testaHeaderLocation(valorHeaderLocation, contaPessoaJuridicaInserida);
		assertNotNull(contaPessoaJuridicaInserida);
		assertNotNull(contaPessoaJuridicaInserida.getId());
		assertNotNull(contaPessoaJuridicaInserida.getTipoConta());
		assertNotNull(contaPessoaJuridicaInserida.getSaldo());
		assertNotNull(contaPessoaJuridicaInserida.getCnpj());
		
		assertTrue(contaPessoaJuridicaInserida.getId() > 0);
		assertEquals(contaPessoaJuridicaInsercaoDto.getTipoConta(), contaPessoaJuridicaInserida.getTipoConta());
		assertEquals(contaPessoaJuridicaInsercaoDto.getSaldo(), contaPessoaJuridicaInserida.getSaldo());
		assertEquals(contaPessoaJuridicaInsercaoDto.getCnpj(), contaPessoaJuridicaInserida.getCnpj());
		return contaPessoaJuridicaInserida;
	}

	private void testaHeaderLocation(String valorHeaderLocation,
			ContaPessoaJuridicaInseridaDto contaPessoaJuridicaInserida) {
		assertNotNull(valorHeaderLocation);
		
		String sufixoUriRecursoCriado = CAMINHO_BASE + contaPessoaJuridicaInserida.getCnpj() + "/"
				+ contaPessoaJuridicaInserida.getTipoConta().name(); // não contém a porta
		assertTrue(valorHeaderLocation.endsWith(sufixoUriRecursoCriado),
				() -> "O valor presente no header Location não termina com o sufixo esperado.");
	}

	private Response insereContaPessoaJuridica(ContaPessoaJuridicaInsercaoDto contaPessoaJuridicaInsercaoDto) {
		Response response = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(contaPessoaJuridicaInsercaoDto)
			.when()
				.post();
		return response;
	}
	
	@DisplayName("Quando busca conta corrente com sucesso deve ser retornado um objeto com os dados da conta"
			+ " e o código de status 200")
	@Order(3)
	@Test
	void testBuscaContaCorrentePessoaJuridica_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		String cnpjContaInserida = dadosPrimeiraContaCorrentePessoaJuridicaInseridaBancoDados.cnpj();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaCorrentePessoaJuridicaInseridaBancoDados.tipoConta();
		TipoConta tipoContaEsperado = TipoConta.CORRENTE;
		testaBuscaBemSucedidaContaPessoaJuridica(cnpjContaInserida, tipoContaContaInserida, tipoContaEsperado);
	}

	@DisplayName("Quando busca conta poupança com sucesso deve ser retornado um objeto com os dados da conta"
			+ " e o código de status 200")
	@Order(4)
	@Test
	void testBuscaContaPoupancaPessoaJuridica_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		String cnpjContaInserida = dadosPrimeiraContaPoupancaPessoaJuridicaInseridaBancoDados.cnpj();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaPoupancaPessoaJuridicaInseridaBancoDados.tipoConta();
		TipoConta tipoContaEsperado = TipoConta.POUPANCA;
		testaBuscaBemSucedidaContaPessoaJuridica(cnpjContaInserida, tipoContaContaInserida, tipoContaEsperado);
	}
	
	private void testaBuscaBemSucedidaContaPessoaJuridica(String cnpjContaInserida, TipoConta tipoContaContaInserida,
			TipoConta tipoContaEsperado) throws JsonProcessingException, JsonMappingException {
		ContaPessoaJuridicaBuscaDto1 contaPessoaJuridicaBuscadaBancoDados = buscaContaPessoaJuridicaPeloCnpjTipoConta(cnpjContaInserida,
				tipoContaContaInserida.name());

		assertNotNull(contaPessoaJuridicaBuscadaBancoDados);
		assertNotNull(contaPessoaJuridicaBuscadaBancoDados.getId());
		assertNotNull(contaPessoaJuridicaBuscadaBancoDados.getTipoConta());
		assertNotNull(contaPessoaJuridicaBuscadaBancoDados.getSaldo());
		assertNotNull(contaPessoaJuridicaBuscadaBancoDados.getCnpj());
		
		assertEquals(cnpjContaInserida, contaPessoaJuridicaBuscadaBancoDados.getCnpj());
		assertEquals(tipoContaEsperado, tipoContaContaInserida);
		assertEquals(tipoContaEsperado, contaPessoaJuridicaBuscadaBancoDados.getTipoConta());
	}

	@DisplayName("Quando altera conta corrente com saldo negativo com sucesso devem ser retornados "
			+ " os dados da conta atuais e o código de status 200")
	@Order(5)
	@Test
	void testAlteraContaCorrentePessoaJuridica_ComSaldoNegativoComSucesso_DevemSerRetornadosDadosContaAtuaisMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		double novoSaldo = -100;
		String cnpjContaInserida = dadosPrimeiraContaCorrentePessoaJuridicaInseridaBancoDados.cnpj();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaCorrentePessoaJuridicaInseridaBancoDados.tipoConta();
		testaAlteracaoBemSucedidaContaPessoaJuridica(novoSaldo, cnpjContaInserida, tipoContaContaInserida);
	}
	
	@DisplayName("Quando altera conta corrente com saldo positivo com sucesso devem ser retornados "
			+ " os dados da conta atuais e o código de status 200")
	@Order(6)
	@Test
	void testAlteraContaCorrentePessoaJuridica_ComSaldoPositivoComSucesso_DevemSerRetornadosDadosContaAtuaisMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		double novoSaldo = 100;
		String cnpjContaInserida = dadosPrimeiraContaCorrentePessoaJuridicaInseridaBancoDados.cnpj();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaCorrentePessoaJuridicaInseridaBancoDados.tipoConta();
		testaAlteracaoBemSucedidaContaPessoaJuridica(novoSaldo, cnpjContaInserida, tipoContaContaInserida);
	}
	
	@DisplayName("Quando altera conta poupança com saldo negativo com sucesso devem ser retornados "
			+ " os dados da conta atuais e o código de status 200")
	@Order(7)
	@Test
	void testAlteraContaPoupancaPessoaJuridica_ComSaldoNegativoComSucesso_DevemSerRetornadosDadosContaAtuaisMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		double novoSaldo = -100;
		String cnpjContaInserida = dadosPrimeiraContaPoupancaPessoaJuridicaInseridaBancoDados.cnpj();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaPoupancaPessoaJuridicaInseridaBancoDados.tipoConta();
		testaAlteracaoBemSucedidaContaPessoaJuridica(novoSaldo, cnpjContaInserida, tipoContaContaInserida);
	}
	
	@DisplayName("Quando altera conta poupança com saldo positivo com sucesso devem ser retornados "
			+ " os dados da conta atuais e o código de status 200")
	@Order(8)
	@Test
	void testAlteraContaPoupancaPessoaJuridica_ComSaldoPositivoComSucesso_DevemSerRetornadosDadosContaAtuaisMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		double novoSaldo = 100;
		String cnpjContaInserida = dadosPrimeiraContaPoupancaPessoaJuridicaInseridaBancoDados.cnpj();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaPoupancaPessoaJuridicaInseridaBancoDados.tipoConta();
		testaAlteracaoBemSucedidaContaPessoaJuridica(novoSaldo, cnpjContaInserida, tipoContaContaInserida);
	}

	private void testaAlteracaoBemSucedidaContaPessoaJuridica(double novoSaldo, String cnpjContaInserida,
			TipoConta tipoContaContaInserida) throws JsonProcessingException, JsonMappingException {
		ContaPessoaJuridicaBuscaDto1 contaPessoaJuridicaBuscadaBancoDados = buscaContaPessoaJuridicaPeloCnpjTipoConta(
				cnpjContaInserida, tipoContaContaInserida.name());

		ContaPessoaJuridicaAlteracaoDto novosDadosParaAlteracao = new ContaPessoaJuridicaAlteracaoDto(
				contaPessoaJuridicaBuscadaBancoDados.getId(),
				novoSaldo);

		Response response = alteraContaPessoaJuridica(novosDadosParaAlteracao);
		
		response.then().statusCode(200);
		
		String conteudoBodyResposta = response.then().extract().body().asString();

		ContaPessoaJuridicaAlteradaDto contaPessoaJuridicaAlterada = objectMapper.readValue(conteudoBodyResposta,
				ContaPessoaJuridicaAlteradaDto.class);

		assertNotNull(contaPessoaJuridicaAlterada);
		assertNotNull(contaPessoaJuridicaAlterada.getId());
		assertNotNull(contaPessoaJuridicaAlterada.getTipoConta());
		assertNotNull(contaPessoaJuridicaAlterada.getSaldo());
		assertNotNull(contaPessoaJuridicaAlterada.getCnpj());
		
		assertEquals(novosDadosParaAlteracao.getSaldo(), contaPessoaJuridicaAlterada.getSaldo());
		
		// verifica se os demais dados se mantiveram os mesmos
		assertEquals(contaPessoaJuridicaBuscadaBancoDados.getId(), contaPessoaJuridicaAlterada.getId());
		assertEquals(contaPessoaJuridicaBuscadaBancoDados.getTipoConta(), contaPessoaJuridicaAlterada.getTipoConta());
		assertEquals(contaPessoaJuridicaBuscadaBancoDados.getCnpj(), contaPessoaJuridicaAlterada.getCnpj());
	}

	private Response alteraContaPessoaJuridica(ContaPessoaJuridicaAlteracaoDto novosDadosParaAlteracao) {
		Response response = given()
				.spec(requestSpecification)
				.contentType(ContentType.JSON)
				.body(novosDadosParaAlteracao)
			.when()
				.put();
		return response;
	}
	
	@DisplayName("Quando tenta inserir uma conta corrente com o CNPJ de uma conta corrente"
			+ " já cadastrada deve ser lançada uma exceção.")
	@Test
	@Order(9)
	void testInsereContaCorrentePessoaJuridica_ComCnpjContaCorrenteJaCadastrada_DeveSerLancadaExcecao() throws JsonMappingException, JsonProcessingException {
		String cnpjContaInserida = dadosPrimeiraContaCorrentePessoaJuridicaInseridaBancoDados.cnpj();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaCorrentePessoaJuridicaInseridaBancoDados.tipoConta();
		
		testaInsercaoMalSucedidaContaPessoaJuridica(cnpjContaInserida, tipoContaContaInserida);
	}
	
	@DisplayName("Quando tenta inserir uma conta poupança com o CNPJ de uma conta poupança"
			+ " já cadastrada deve ser lançada uma exceção.")
	@Test
	@Order(10)
	void testInsereContaPoupancaPessoaJuridica_ComCnpjContaCorrenteJaCadastrada_DeveSerLancadaExcecao() throws JsonMappingException, JsonProcessingException {
		String cnpjContaInserida = dadosPrimeiraContaPoupancaPessoaJuridicaInseridaBancoDados.cnpj();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaPoupancaPessoaJuridicaInseridaBancoDados.tipoConta();
		
		testaInsercaoMalSucedidaContaPessoaJuridica(cnpjContaInserida, tipoContaContaInserida);
	}

	private void testaInsercaoMalSucedidaContaPessoaJuridica(String cnpjContaInserida, TipoConta tipoContaContaInserida)
			throws JsonProcessingException, JsonMappingException {
		String mensagemEsperada = "Já existe uma conta " + tipoContaContaInserida.getNome().toLowerCase()
				+ " cadastrada com o CNPJ " + cnpjContaInserida + ".";
		
		ContaPessoaJuridicaInsercaoDto novaContaPessoaJuridicaParaInsercao = new ContaPessoaJuridicaInsercaoDto(
				tipoContaContaInserida, 0, cnpjContaInserida);
		
		Response response = insereContaPessoaJuridica(novaContaPessoaJuridicaParaInsercao);
		
		String conteudoBodyResposta = response.then().extract().body().asString();
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando deleta uma conta corrente com sucesso deve ser retornado o código de status 204")
	@Order(11)
	@Test
	void testDeletaContaCorrentePessoaJuridica_ComSucesso_DeveSerRetornadoCodigoStatus204() {
		Long idContaCorrentePessoaJuridica = dadosPrimeiraContaCorrentePessoaJuridicaInseridaBancoDados.idConta();
		testaRemocaoBemSucedidaContaPessoaJuridica(idContaCorrentePessoaJuridica);
	}
	
	@DisplayName("Quando deleta uma conta poupanca com sucesso deve ser retornado o código de status 204")
	@Order(12)
	@Test
	void testDeletaContaPoupancaPessoaJuridica_ComSucesso_DeveSerRetornadoCodigoStatus204() {
		Long idContaPoupancaPessoaJuridica = dadosPrimeiraContaPoupancaPessoaJuridicaInseridaBancoDados.idConta();
		testaRemocaoBemSucedidaContaPessoaJuridica(idContaPoupancaPessoaJuridica);
	}

	private void testaRemocaoBemSucedidaContaPessoaJuridica(Long idConta) {
		Response response = removeContaPessoaJuridica(idConta);
		response.then().statusCode(204);
	}

	private Response removeContaPessoaJuridica(Long idContaPessoaJuridica) {
		Response response = given()
			.spec(requestSpecification)
		.when()
			.delete("{idContaPessoaJuridica}", idContaPessoaJuridica);
		return response;
	}
	
	@DisplayName("Quando não insere uma conta com sucesso não deve ser retornado o código de status 201")
	@Test
	void testInsereContaPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus201() throws JsonProcessingException, Exception {
		TipoConta tipoContaNulo = null;
		String mensagemEsperada = "O tipo de conta não foi informado.";
		
		ContaPessoaJuridicaInsercaoDto contaPessoaJuridicaInsercaoDto = new ContaPessoaJuridicaInsercaoDto(tipoContaNulo, 0, CNPJ_1);
		
		Response response = insereContaPessoaJuridica(contaPessoaJuridicaInsercaoDto);
		ValidatableResponse then = response.then();
		
		then.assertThat().statusCode(not(equalTo(201)));
		
		String conteudoBodyResposta = then.extract().body().asString();
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando não busca uma conta corrente com sucesso "
			+ "deve ser retornado o código de status 404 e o body da resposta vazio")
	@Test
	void testBuscaContaCorrentePessoaJuridica_SemSucesso_deveSerRetornadoCodigoStatus404SemConteudoBody()
			throws Exception {
		String cnpj = CNPJ_SEM_ASSOCIACAO_COM_ALGUM_CONTA;
		String tipoConta = TipoConta.CORRENTE.name();
		
		testaBuscaMalSucedidaContaPessoaJuridica(cnpj, tipoConta);
	}
	
	@DisplayName("Quando não busca uma conta poupança com sucesso "
			+ "deve ser retornado o código de status 404 e o body da resposta vazio")
	@Test
	void testBuscaContaPoupancaPessoaJuridica_SemSucesso_deveSerRetornadoCodigoStatus404SemConteudoBody()
			throws Exception {
		String cnpj = CNPJ_SEM_ASSOCIACAO_COM_ALGUM_CONTA;
		String tipoConta = TipoConta.POUPANCA.name();
		
		testaBuscaMalSucedidaContaPessoaJuridica(cnpj, tipoConta);
	}

	private void testaBuscaMalSucedidaContaPessoaJuridica(String cnpj, String tipoConta) {
		String conteudoBodyResposta = given()
				.spec(requestSpecification)
			.when()
				.get("{cnpj}/{tipoConta}", cnpj, tipoConta)
			.then()
				.statusCode(404)
			.extract()
				.body()
					.asString();

		assertEquals("", conteudoBodyResposta, () -> "O body da resposta possuí conteúdo, porém deveria estar vazio.");
	}
	
	@DisplayName("Quando não altera uma conta com sucesso não deve ser retornado o código de status 200")
	@Test
	void testAlteraContaPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus200() throws JsonProcessingException, Exception {
		String mensagemEsperada = "Não foi encontrada uma conta com o código informado.";
		
		ContaPessoaJuridicaAlteracaoDto contaPessoaJuridicaAlteracaoDto = new ContaPessoaJuridicaAlteracaoDto(ID_SEM_ASSOCIACAO_COM_ALGUM_CONTA, 200);
		
		Response response = alteraContaPessoaJuridica(contaPessoaJuridicaAlteracaoDto);
		ValidatableResponse then = response.then();
		 
		then.assertThat().statusCode(not(equalTo(200)));
		
		String conteudoBodyResposta = then.extract().body().asString();
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando não remove uma conta com sucesso não deve ser retornado o código de status 204")
	@Test
	void testRemoveContaPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus204() throws Exception {
		String mensagemEsperada = "Não foi encontrada uma conta com o código informado.";
		
		Response response = removeContaPessoaJuridica(ID_SEM_ASSOCIACAO_COM_ALGUM_CONTA);
		ValidatableResponse then = response.then();
		
		then.assertThat().statusCode(not(equalTo(204)));
		
		String conteudoBodyResposta = then.extract().body().asString();
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	private ContaPessoaJuridicaBuscaDto1 buscaContaPessoaJuridicaPeloCnpjTipoConta(String cnpj, String tipoConta)
			throws JsonProcessingException, JsonMappingException {
		String conteudoBodyResposta = given()
					.spec(requestSpecification)
				.when()
					.get("{cnpj}/{tipoConta}", cnpj, tipoConta)
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		ContaPessoaJuridicaBuscaDto1 contaPessoaJuridicaBuscadaBancoDados = objectMapper
				.readValue(conteudoBodyResposta, ContaPessoaJuridicaBuscaDto1.class);
		return contaPessoaJuridicaBuscadaBancoDados;
	}
}
