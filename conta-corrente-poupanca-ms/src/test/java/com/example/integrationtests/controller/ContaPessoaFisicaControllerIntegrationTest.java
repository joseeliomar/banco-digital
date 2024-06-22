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
import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaAlteradaDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.ContaPessoaFisicaInsercaoDto;
import com.example.dto.ContaPessoaFisicaInseridaDto;
import com.example.dto.DetalhesExcecaoDto;
import com.example.enumeration.TipoConta;
import com.example.integrationtests.dto.DadosContaPessoaFisica;
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
class ContaPessoaFisicaControllerIntegrationTest extends ConfiguracaoAmbienteTestesParaUsoContainers {
	
	private static final String CAMINHO_BASE = "/contaPessoaFisica/";
	
	private static final String CPF_1 = "12345678901";
	
	private static final String CPF_SEM_ASSOCIACAO_COM_ALGUM_CONTA = "99999999999"; // Não há nenhum conta com esse CPF
	
	private static final Long ID_SEM_ASSOCIACAO_COM_ALGUM_CONTA = 999999999999999999L; // Não há nenhum conta com esse ID

	private RequestSpecification requestSpecification;
	
	@Autowired
	private TestConfigs testConfigs;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private DadosContaPessoaFisica dadosPrimeiraContaCorrentePessoaFisicaInseridaBancoDados;
	
	private DadosContaPessoaFisica dadosPrimeiraContaPoupancaPessoaFisicaInseridaBancoDados;
	
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
	void testInsereContaCorrentePessoaFisica_ComSucesso_DeveSerRetornadoHeaderLocationMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		TipoConta tipoConta = TipoConta.CORRENTE;
		ContaPessoaFisicaInsercaoDto contaPessoaFisicaInsercaoDto = new ContaPessoaFisicaInsercaoDto(tipoConta, 0, CPF_1);

		ContaPessoaFisicaInseridaDto contaPessoaFisicaInserida = testaInsercaoBemSucedidaContaPessoaFisica(
				contaPessoaFisicaInsercaoDto);
		
		dadosPrimeiraContaCorrentePessoaFisicaInseridaBancoDados = new DadosContaPessoaFisica(contaPessoaFisicaInserida.getId(),
				contaPessoaFisicaInserida.getCpf(), contaPessoaFisicaInserida.getTipoConta());
	}
	
	@DisplayName("Quando insere conta poupança com sucesso deve ser retornado o header Location e o código de status 201")
	@Order(2)
	@Test
	void testInsereContaPoupancaPessoaFisica_ComSucesso_DeveSerRetornadoHeaderLocationMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		TipoConta tipoConta = TipoConta.POUPANCA;
		ContaPessoaFisicaInsercaoDto contaPessoaFisicaInsercaoDto = new ContaPessoaFisicaInsercaoDto(tipoConta, 0, CPF_1);

		ContaPessoaFisicaInseridaDto contaPessoaFisicaInserida = testaInsercaoBemSucedidaContaPessoaFisica(
				contaPessoaFisicaInsercaoDto);
		
		dadosPrimeiraContaPoupancaPessoaFisicaInseridaBancoDados = new DadosContaPessoaFisica(contaPessoaFisicaInserida.getId(),
				contaPessoaFisicaInserida.getCpf(), contaPessoaFisicaInserida.getTipoConta());
	}

	private ContaPessoaFisicaInseridaDto testaInsercaoBemSucedidaContaPessoaFisica(
			ContaPessoaFisicaInsercaoDto contaPessoaFisicaInsercaoDto)
			throws JsonProcessingException, JsonMappingException {
		Response response = insereContaPessoaFisica(contaPessoaFisicaInsercaoDto);
				
		response.then().statusCode(201);
				
		String valorHeaderLocation = response.getHeader("Location");
		String conteudoBodyResposta = response.asString();

		ContaPessoaFisicaInseridaDto contaPessoaFisicaInserida = objectMapper.readValue(conteudoBodyResposta,
				ContaPessoaFisicaInseridaDto.class);
		
		testaHeaderLocation(valorHeaderLocation, contaPessoaFisicaInserida);
		assertNotNull(contaPessoaFisicaInserida);
		assertNotNull(contaPessoaFisicaInserida.getId());
		assertNotNull(contaPessoaFisicaInserida.getTipoConta());
		assertNotNull(contaPessoaFisicaInserida.getSaldo());
		assertNotNull(contaPessoaFisicaInserida.getCpf());
		
		assertTrue(contaPessoaFisicaInserida.getId() > 0);
		assertEquals(contaPessoaFisicaInsercaoDto.getTipoConta(), contaPessoaFisicaInserida.getTipoConta());
		assertEquals(contaPessoaFisicaInsercaoDto.getSaldo(), contaPessoaFisicaInserida.getSaldo());
		assertEquals(contaPessoaFisicaInsercaoDto.getCpf(), contaPessoaFisicaInserida.getCpf());
		return contaPessoaFisicaInserida;
	}

	private void testaHeaderLocation(String valorHeaderLocation,
			ContaPessoaFisicaInseridaDto contaPessoaFisicaInserida) {
		assertNotNull(valorHeaderLocation);
		
		String sufixoUriRecursoCriado = CAMINHO_BASE + contaPessoaFisicaInserida.getCpf() + "/"
				+ contaPessoaFisicaInserida.getTipoConta().name(); // não contém a porta
		assertTrue(valorHeaderLocation.endsWith(sufixoUriRecursoCriado),
				() -> "O valor presente no header Location não termina com o sufixo esperado.");
	}

	private Response insereContaPessoaFisica(ContaPessoaFisicaInsercaoDto contaPessoaFisicaInsercaoDto) {
		Response response = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(contaPessoaFisicaInsercaoDto)
			.when()
				.post();
		return response;
	}
	
	@DisplayName("Quando busca conta corrente com sucesso deve ser retornado um objeto com os dados da conta"
			+ " e o código de status 200")
	@Order(3)
	@Test
	void testBuscaContaCorrentePessoaFisica_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		String cpfContaInserida = dadosPrimeiraContaCorrentePessoaFisicaInseridaBancoDados.cpf();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaCorrentePessoaFisicaInseridaBancoDados.tipoConta();
		TipoConta tipoContaEsperado = TipoConta.CORRENTE;
		testaBuscaBemSucedidaContaPessoaFisica(cpfContaInserida, tipoContaContaInserida, tipoContaEsperado);
	}

	@DisplayName("Quando busca conta poupança com sucesso deve ser retornado um objeto com os dados da conta"
			+ " e o código de status 200")
	@Order(4)
	@Test
	void testBuscaContaPoupancaPessoaFisica_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		String cpfContaInserida = dadosPrimeiraContaPoupancaPessoaFisicaInseridaBancoDados.cpf();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaPoupancaPessoaFisicaInseridaBancoDados.tipoConta();
		TipoConta tipoContaEsperado = TipoConta.POUPANCA;
		testaBuscaBemSucedidaContaPessoaFisica(cpfContaInserida, tipoContaContaInserida, tipoContaEsperado);
	}
	
	private void testaBuscaBemSucedidaContaPessoaFisica(String cpfContaInserida, TipoConta tipoContaContaInserida,
			TipoConta tipoContaEsperado) throws JsonProcessingException, JsonMappingException {
		ContaPessoaFisicaBuscaDto1 contaPessoaFisicaBuscadaBancoDados = buscaContaPessoaFisicaPeloCpfTipoConta(cpfContaInserida,
				tipoContaContaInserida.name());

		assertNotNull(contaPessoaFisicaBuscadaBancoDados);
		assertNotNull(contaPessoaFisicaBuscadaBancoDados.getId());
		assertNotNull(contaPessoaFisicaBuscadaBancoDados.getTipoConta());
		assertNotNull(contaPessoaFisicaBuscadaBancoDados.getSaldo());
		assertNotNull(contaPessoaFisicaBuscadaBancoDados.getCpf());
		
		assertEquals(cpfContaInserida, contaPessoaFisicaBuscadaBancoDados.getCpf());
		assertEquals(tipoContaEsperado, tipoContaContaInserida);
		assertEquals(tipoContaEsperado, contaPessoaFisicaBuscadaBancoDados.getTipoConta());
	}

	@DisplayName("Quando altera conta corrente com saldo negativo com sucesso devem ser retornados "
			+ " os dados da conta atuais e o código de status 200")
	@Order(5)
	@Test
	void testAlteraContaCorrentePessoaFisica_ComSaldoNegativoComSucesso_DevemSerRetornadosDadosContaAtuaisMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		double novoSaldo = -100;
		String cpfContaInserida = dadosPrimeiraContaCorrentePessoaFisicaInseridaBancoDados.cpf();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaCorrentePessoaFisicaInseridaBancoDados.tipoConta();
		testaAlteracaoBemSucedidaContaPessoaFisica(novoSaldo, cpfContaInserida, tipoContaContaInserida);
	}
	
	@DisplayName("Quando altera conta corrente com saldo positivo com sucesso devem ser retornados "
			+ " os dados da conta atuais e o código de status 200")
	@Order(6)
	@Test
	void testAlteraContaCorrentePessoaFisica_ComSaldoPositivoComSucesso_DevemSerRetornadosDadosContaAtuaisMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		double novoSaldo = 100;
		String cpfContaInserida = dadosPrimeiraContaCorrentePessoaFisicaInseridaBancoDados.cpf();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaCorrentePessoaFisicaInseridaBancoDados.tipoConta();
		testaAlteracaoBemSucedidaContaPessoaFisica(novoSaldo, cpfContaInserida, tipoContaContaInserida);
	}
	
	@DisplayName("Quando altera conta poupança com saldo negativo com sucesso devem ser retornados "
			+ " os dados da conta atuais e o código de status 200")
	@Order(7)
	@Test
	void testAlteraContaPoupancaPessoaFisica_ComSaldoNegativoComSucesso_DevemSerRetornadosDadosContaAtuaisMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		double novoSaldo = -100;
		String cpfContaInserida = dadosPrimeiraContaPoupancaPessoaFisicaInseridaBancoDados.cpf();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaPoupancaPessoaFisicaInseridaBancoDados.tipoConta();
		testaAlteracaoBemSucedidaContaPessoaFisica(novoSaldo, cpfContaInserida, tipoContaContaInserida);
	}
	
	@DisplayName("Quando altera conta poupança com saldo positivo com sucesso devem ser retornados "
			+ " os dados da conta atuais e o código de status 200")
	@Order(8)
	@Test
	void testAlteraContaPoupancaPessoaFisica_ComSaldoPositivoComSucesso_DevemSerRetornadosDadosContaAtuaisMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		double novoSaldo = 100;
		String cpfContaInserida = dadosPrimeiraContaPoupancaPessoaFisicaInseridaBancoDados.cpf();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaPoupancaPessoaFisicaInseridaBancoDados.tipoConta();
		testaAlteracaoBemSucedidaContaPessoaFisica(novoSaldo, cpfContaInserida, tipoContaContaInserida);
	}

	private void testaAlteracaoBemSucedidaContaPessoaFisica(double novoSaldo, String cpfContaInserida,
			TipoConta tipoContaContaInserida) throws JsonProcessingException, JsonMappingException {
		ContaPessoaFisicaBuscaDto1 contaPessoaFisicaBuscadaBancoDados = buscaContaPessoaFisicaPeloCpfTipoConta(
				cpfContaInserida, tipoContaContaInserida.name());

		ContaPessoaFisicaAlteracaoDto novosDadosParaAlteracao = new ContaPessoaFisicaAlteracaoDto(
				contaPessoaFisicaBuscadaBancoDados.getId(),
				novoSaldo);

		Response response = alteraContaPessoaFisica(novosDadosParaAlteracao);
		
		response.then().statusCode(200);
		
		String conteudoBodyResposta = response.then().extract().body().asString();

		ContaPessoaFisicaAlteradaDto contaPessoaFisicaAlterada = objectMapper.readValue(conteudoBodyResposta,
				ContaPessoaFisicaAlteradaDto.class);

		assertNotNull(contaPessoaFisicaAlterada);
		assertNotNull(contaPessoaFisicaAlterada.getId());
		assertNotNull(contaPessoaFisicaAlterada.getTipoConta());
		assertNotNull(contaPessoaFisicaAlterada.getSaldo());
		assertNotNull(contaPessoaFisicaAlterada.getCpf());
		
		assertEquals(novosDadosParaAlteracao.getSaldo(), contaPessoaFisicaAlterada.getSaldo());
		
		// verifica se os demais dados se mantiveram os mesmos
		assertEquals(contaPessoaFisicaBuscadaBancoDados.getId(), contaPessoaFisicaAlterada.getId());
		assertEquals(contaPessoaFisicaBuscadaBancoDados.getTipoConta(), contaPessoaFisicaAlterada.getTipoConta());
		assertEquals(contaPessoaFisicaBuscadaBancoDados.getCpf(), contaPessoaFisicaAlterada.getCpf());
	}

	private Response alteraContaPessoaFisica(ContaPessoaFisicaAlteracaoDto novosDadosParaAlteracao) {
		Response response = given()
				.spec(requestSpecification)
				.contentType(ContentType.JSON)
				.body(novosDadosParaAlteracao)
			.when()
				.put();
		return response;
	}
	
	@DisplayName("Quando tenta inserir uma conta corrente com o CPF de uma conta corrente"
			+ " já cadastrada deve ser lançada uma exceção.")
	@Test
	@Order(9)
	void testInsereContaCorrentePessoaFisica_ComCpfContaCorrenteJaCadastrada_DeveSerLancadaExcecao() throws JsonMappingException, JsonProcessingException {
		String cpfContaInserida = dadosPrimeiraContaCorrentePessoaFisicaInseridaBancoDados.cpf();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaCorrentePessoaFisicaInseridaBancoDados.tipoConta();
		
		testaInsercaoMalSucedidaContaPessoaFisica(cpfContaInserida, tipoContaContaInserida);
	}
	
	@DisplayName("Quando tenta inserir uma conta poupança com o CPF de uma conta poupança"
			+ " já cadastrada deve ser lançada uma exceção.")
	@Test
	@Order(10)
	void testInsereContaPoupancaPessoaFisica_ComCpfContaCorrenteJaCadastrada_DeveSerLancadaExcecao() throws JsonMappingException, JsonProcessingException {
		String cpfContaInserida = dadosPrimeiraContaPoupancaPessoaFisicaInseridaBancoDados.cpf();
		TipoConta tipoContaContaInserida = dadosPrimeiraContaPoupancaPessoaFisicaInseridaBancoDados.tipoConta();
		
		testaInsercaoMalSucedidaContaPessoaFisica(cpfContaInserida, tipoContaContaInserida);
	}

	private void testaInsercaoMalSucedidaContaPessoaFisica(String cpfContaInserida, TipoConta tipoContaContaInserida)
			throws JsonProcessingException, JsonMappingException {
		String mensagemEsperada = "Já existe uma conta " + tipoContaContaInserida.getNome().toLowerCase()
				+ " cadastrada com o CPF " + cpfContaInserida + ".";
		
		ContaPessoaFisicaInsercaoDto novaContaPessoaFisicaParaInsercao = new ContaPessoaFisicaInsercaoDto(
				tipoContaContaInserida, 0, cpfContaInserida);
		
		Response response = insereContaPessoaFisica(novaContaPessoaFisicaParaInsercao);
		
		String conteudoBodyResposta = response.then().extract().body().asString();
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando deleta uma conta corrente com sucesso deve ser retornado o código de status 204")
	@Order(11)
	@Test
	void testDeletaContaCorrentePessoaFisica_ComSucesso_DeveSerRetornadoCodigoStatus204() {
		Long idContaCorrentePessoaFisica = dadosPrimeiraContaCorrentePessoaFisicaInseridaBancoDados.idConta();
		testaRemocaoBemSucedidaContaPessoaFisica(idContaCorrentePessoaFisica);
	}
	
	@DisplayName("Quando deleta uma conta poupanca com sucesso deve ser retornado o código de status 204")
	@Order(12)
	@Test
	void testDeletaContaPoupancaPessoaFisica_ComSucesso_DeveSerRetornadoCodigoStatus204() {
		Long idContaPoupancaPessoaFisica = dadosPrimeiraContaPoupancaPessoaFisicaInseridaBancoDados.idConta();
		testaRemocaoBemSucedidaContaPessoaFisica(idContaPoupancaPessoaFisica);
	}

	private void testaRemocaoBemSucedidaContaPessoaFisica(Long idConta) {
		Response response = removeContaPessoaFisica(idConta);
		response.then().statusCode(204);
	}

	private Response removeContaPessoaFisica(Long idContaPessoaFisica) {
		Response response = given()
			.spec(requestSpecification)
		.when()
			.delete("{idContaPessoaFisica}", idContaPessoaFisica);
		return response;
	}
	
	@DisplayName("Quando não insere uma conta com sucesso não deve ser retornado o código de status 201")
	@Test
	void testInsereContaPessoaFisica_SemSucesso_NaoDeveSerRetornadoCodigoStatus201() throws JsonProcessingException, Exception {
		TipoConta tipoContaNulo = null;
		String mensagemEsperada = "O tipo de conta não foi informado.";
		
		ContaPessoaFisicaInsercaoDto contaPessoaFisicaInsercaoDto = new ContaPessoaFisicaInsercaoDto(tipoContaNulo, 0, CPF_1);
		
		Response response = insereContaPessoaFisica(contaPessoaFisicaInsercaoDto);
		ValidatableResponse then = response.then();
		
		then.assertThat().statusCode(not(equalTo(201)));
		
		String conteudoBodyResposta = then.extract().body().asString();
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando não busca uma conta corrente com sucesso "
			+ "deve ser retornado o código de status 404 e o body da resposta vazio")
	@Test
	void testBuscaContaCorrentePessoaFisica_SemSucesso_deveSerRetornadoCodigoStatus404SemConteudoBody()
			throws Exception {
		String cpf = CPF_SEM_ASSOCIACAO_COM_ALGUM_CONTA;
		String tipoConta = TipoConta.CORRENTE.name();
		
		testaBuscaMalSucedidaContaPessoaFisica(cpf, tipoConta);
	}
	
	@DisplayName("Quando não busca uma conta poupança com sucesso "
			+ "deve ser retornado o código de status 404 e o body da resposta vazio")
	@Test
	void testBuscaContaPoupancaPessoaFisica_SemSucesso_deveSerRetornadoCodigoStatus404SemConteudoBody()
			throws Exception {
		String cpf = CPF_SEM_ASSOCIACAO_COM_ALGUM_CONTA;
		String tipoConta = TipoConta.POUPANCA.name();
		
		testaBuscaMalSucedidaContaPessoaFisica(cpf, tipoConta);
	}

	private void testaBuscaMalSucedidaContaPessoaFisica(String cpf, String tipoConta) {
		String conteudoBodyResposta = given()
				.spec(requestSpecification)
			.when()
				.get("{cpf}/{tipoConta}", cpf, tipoConta)
			.then()
				.statusCode(404)
			.extract()
				.body()
					.asString();

		assertEquals("", conteudoBodyResposta, () -> "O body da resposta possuí conteúdo, porém deveria estar vazio.");
	}
	
	@DisplayName("Quando não altera uma conta com sucesso não deve ser retornado o código de status 200")
	@Test
	void testAlteraContaPessoaFisica_SemSucesso_NaoDeveSerRetornadoCodigoStatus200() throws JsonProcessingException, Exception {
		String mensagemEsperada = "Não foi encontrada uma conta com o código informado.";
		
		ContaPessoaFisicaAlteracaoDto contaPessoaFisicaAlteracaoDto = new ContaPessoaFisicaAlteracaoDto(ID_SEM_ASSOCIACAO_COM_ALGUM_CONTA, 200);
		
		Response response = alteraContaPessoaFisica(contaPessoaFisicaAlteracaoDto);
		ValidatableResponse then = response.then();
		 
		then.assertThat().statusCode(not(equalTo(200)));
		
		String conteudoBodyResposta = then.extract().body().asString();
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando não remove uma conta com sucesso não deve ser retornado o código de status 204")
	@Test
	void testRemoveContaPessoaFisica_SemSucesso_NaoDeveSerRetornadoCodigoStatus204() throws Exception {
		String mensagemEsperada = "Não foi encontrada uma conta com o código informado.";
		
		Response response = removeContaPessoaFisica(ID_SEM_ASSOCIACAO_COM_ALGUM_CONTA);
		ValidatableResponse then = response.then();
		
		then.assertThat().statusCode(not(equalTo(204)));
		
		String conteudoBodyResposta = then.extract().body().asString();
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	/**
	 * Busca uma conta de pessoa física pelo CPF e o tipo de conta.
	 * 
	 * @param cpf
	 * @param tipoConta
	 * @return a conta de pessoa física encontrada.
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	private ContaPessoaFisicaBuscaDto1 buscaContaPessoaFisicaPeloCpfTipoConta(String cpf, String tipoConta)
			throws JsonProcessingException, JsonMappingException {
		String conteudoBodyResposta = given()
					.spec(requestSpecification)
				.when()
					.get("{cpf}/{tipoConta}", cpf, tipoConta)
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		ContaPessoaFisicaBuscaDto1 contaPessoaFisicaBuscadaBancoDados = objectMapper
				.readValue(conteudoBodyResposta, ContaPessoaFisicaBuscaDto1.class);
		return contaPessoaFisicaBuscadaBancoDados;
	}
}
