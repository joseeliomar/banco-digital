package com.example.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import com.example.dto.ContaDigitalPessoaFisicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaFisicaAlteradaDto;
import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaFisicaInsercaoDto;
import com.example.dto.ContaDigitalPessoaFisicaInseridaDto;
import com.example.dto.DetalhesExcecaoDto;
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
import io.restassured.specification.RequestSpecification;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ContaDigitalPessoaFisicaControllerIntegrationTest extends ConfiguracaoAmbienteTestesParaUsoContainers {
	
	private static final String CAMINHO_BASE = "/contaDigitalPessoaFisica/";

	private RequestSpecification requestSpecification;
	
	@Autowired
	private TestConfigs testConfigs;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final String CPF_1 = "12345678901";
	private static final String CPF_2 = "12345678902";
	private static final String CPF_SEM_CONTA_DIGITAL_ASSOCIADO_COM_ELE = "99999999999"; // Não há nenhuma conta digital com esse CPF;

	private ContaDigitalPessoaFisicaInsercaoDto contaDigitalPessoaFisicaInsercaoDto1;

	private ContaDigitalPessoaFisicaAlteracaoDto contaDigitalPessoaFisicaAlteracaoDto1;
	
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
	
	@BeforeEach
	public void setup() {
		contaDigitalPessoaFisicaInsercaoDto1 = new ContaDigitalPessoaFisicaInsercaoDto(
				"0000000013", "1234567890", "654115897", "19980009999", "email@email.com", CPF_2,
				"Fulano de Tal", LocalDate.of(1995, 1, 1), "Fulana de Tal");
		
		contaDigitalPessoaFisicaAlteracaoDto1 = new ContaDigitalPessoaFisicaAlteracaoDto("0000000012", "1234567999",
				"654115897", "19980009999", "email@email.com", 2L, CPF_2, "Fulano de Tal", LocalDate.of(1995, 1, 1),
				"Fulana de Tal");
	}
	
	@DisplayName("Quando insere conta digital para pessoa física com sucesso deve ser retornado "
			+ "o header Location e o código de status 201")
	@Order(1)
	@Test
	void testInsereContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadoHeaderLocationMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		ContaDigitalPessoaFisicaInsercaoDto contaDigitalPessoaFisicaInsercaoDto = new ContaDigitalPessoaFisicaInsercaoDto(
				"0000000011", "1234567890", "12345678", "19980001234", "fulano@email.com", CPF_1,
				"Fulano de Tal", LocalDate.of(2001, 1, 1), "Fulana de Tal");

		String sufixoUriRecursoCriado = CAMINHO_BASE + contaDigitalPessoaFisicaInsercaoDto.getCpf(); // não contém a porta

		Response response = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(contaDigitalPessoaFisicaInsercaoDto)
			.when()
				.post();
		
		response.then().statusCode(201);
		
		String valorHeaderLocation = response.getHeader("Location");
		String conteudoBodyResposta = response.asString();

		ContaDigitalPessoaFisicaInseridaDto contaDigitalPessoaFisicaInserida = objectMapper
				.readValue(conteudoBodyResposta, ContaDigitalPessoaFisicaInseridaDto.class);

		assertNotNull(valorHeaderLocation);
		assertTrue(valorHeaderLocation.endsWith(sufixoUriRecursoCriado),
				() -> "O valor presente no header Location não termina com o sufixo esperado.");
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
	
	@DisplayName("Quando busca conta digital para pessoa física com sucesso deve ser retornado "
			+ "um objeto com os dados da conta digital e o código de status 200")
	@Order(2)
	@Test
	void testBuscaContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisicaBuscadaBancoDados = buscaContaDigitalPessoaFisicaComSucessoPeloCpf(
				CPF_1);

		assertNotNull(contaDigitalPessoaFisicaBuscadaBancoDados);
		assertNotNull(contaDigitalPessoaFisicaBuscadaBancoDados.getAgencia());
		assertNotNull(contaDigitalPessoaFisicaBuscadaBancoDados.getConta());
		assertNotNull(contaDigitalPessoaFisicaBuscadaBancoDados.getTelefone());
		assertNotNull(contaDigitalPessoaFisicaBuscadaBancoDados.getEmail());
		assertNull(contaDigitalPessoaFisicaBuscadaBancoDados.getIdEndereco(), () -> "Não deve haver um código de endereço,"
				+ " pois na inserção da conta não foi associado a essa conta um endereço");
		assertNotNull(contaDigitalPessoaFisicaBuscadaBancoDados.getDataHoraCadastro());
		assertNull(contaDigitalPessoaFisicaBuscadaBancoDados.getDataHoraAlteracao(),
				() -> "Não deve haver data e hora da alteração, pois a conta digital ainda não foi alterada, "
				+ "apenas inserida no banco de dados");
		assertNotNull(contaDigitalPessoaFisicaBuscadaBancoDados.getCpf());
		assertNotNull(contaDigitalPessoaFisicaBuscadaBancoDados.getNomeCompleto());
		assertNotNull(contaDigitalPessoaFisicaBuscadaBancoDados.getDataNascimento());
		assertNotNull(contaDigitalPessoaFisicaBuscadaBancoDados.getNomeCompletoMae());
	}

	@DisplayName("Quando altera conta digital para pessoa física com sucesso deve ser retornado "
			+ "um objeto com os dados atualizados e o código de status 200")
	@Order(3)
	@Test
	void testAlteraContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisicaBuscadaBancoDados = buscaContaDigitalPessoaFisicaComSucessoPeloCpf(
				CPF_1);

		String novaSenha = "123456@55", novoTelefone = "19980005555", novoEmail = "ciclano@email.com",
				novoNomeCompleto = "Ciclano de tal", novoNomeCompletoMae = "Ciclana de tal";
		Long idEndereco = 1L;
		LocalDate novaDataNascimento = LocalDate.of(2002, 1, 1);
		ContaDigitalPessoaFisicaAlteracaoDto novosDadosParaAlteracao = new ContaDigitalPessoaFisicaAlteracaoDto(
				contaDigitalPessoaFisicaBuscadaBancoDados.getAgencia(), 
				contaDigitalPessoaFisicaBuscadaBancoDados.getConta(), 
				novaSenha, 
				novoTelefone, 
				novoEmail, 
				idEndereco, 
				contaDigitalPessoaFisicaBuscadaBancoDados.getCpf(),
				novoNomeCompleto, 
				novaDataNascimento, 
				novoNomeCompletoMae);

		String conteudoBodyResposta = given()
					.spec(requestSpecification)
					.contentType(ContentType.JSON)
					.body(novosDadosParaAlteracao)
				.when()
					.put()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();

		ContaDigitalPessoaFisicaAlteradaDto contaDigitalPessoaFisicaAlterada = objectMapper
				.readValue(conteudoBodyResposta, ContaDigitalPessoaFisicaAlteradaDto.class);

		assertNotNull(contaDigitalPessoaFisicaAlterada);
		assertNotNull(contaDigitalPessoaFisicaAlterada.getAgencia());
		assertNotNull(contaDigitalPessoaFisicaAlterada.getConta());
		assertNotNull(contaDigitalPessoaFisicaAlterada.getSenha());
		assertNotNull(contaDigitalPessoaFisicaAlterada.getTelefone());
		assertNotNull(contaDigitalPessoaFisicaAlterada.getEmail());
		assertNotNull(contaDigitalPessoaFisicaAlterada.getIdEndereco());
		assertNotNull(contaDigitalPessoaFisicaAlterada.getDataHoraCadastro());
		assertNotNull(contaDigitalPessoaFisicaAlterada.getDataHoraAlteracao());
		assertNotNull(contaDigitalPessoaFisicaAlterada.getCpf());
		assertNotNull(contaDigitalPessoaFisicaAlterada.getNomeCompleto());
		assertNotNull(contaDigitalPessoaFisicaAlterada.getDataNascimento());
		assertNotNull(contaDigitalPessoaFisicaAlterada.getNomeCompletoMae());
		
		assertEquals(novosDadosParaAlteracao.getAgencia(), contaDigitalPessoaFisicaAlterada.getAgencia());
		assertEquals(novosDadosParaAlteracao.getConta(), contaDigitalPessoaFisicaAlterada.getConta());
		assertEquals(novosDadosParaAlteracao.getSenha(), contaDigitalPessoaFisicaAlterada.getSenha());
		assertEquals(novosDadosParaAlteracao.getTelefone(), contaDigitalPessoaFisicaAlterada.getTelefone());
		assertEquals(novosDadosParaAlteracao.getEmail(), contaDigitalPessoaFisicaAlterada.getEmail());
		assertEquals(novosDadosParaAlteracao.getIdEndereco(), contaDigitalPessoaFisicaAlterada.getIdEndereco());
		assertEquals(contaDigitalPessoaFisicaBuscadaBancoDados.getDataHoraCadastro(), contaDigitalPessoaFisicaAlterada.getDataHoraCadastro());
		assertEquals(novosDadosParaAlteracao.getCpf(), contaDigitalPessoaFisicaAlterada.getCpf());
		assertEquals(novosDadosParaAlteracao.getNomeCompleto(), contaDigitalPessoaFisicaAlterada.getNomeCompleto());
		assertEquals(novosDadosParaAlteracao.getDataNascimento(), contaDigitalPessoaFisicaAlterada.getDataNascimento());
		assertEquals(novosDadosParaAlteracao.getNomeCompletoMae(), contaDigitalPessoaFisicaAlterada.getNomeCompletoMae());
	}
	
	@DisplayName("Quando tenta inserir conta digital com o CPF de uma conta digital já cadastrada deve ser lançada uma exceção.")
	@Test
	@Order(4)
	void testInsereContaDigital_ComCpfContaDigitalJaCadastrada_DeveSerLancadaExcecao() throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalCadastrada = buscaContaDigitalPessoaFisicaComSucessoPeloCpf(CPF_1);
		String cpfContaDigitalCadastrada = contaDigitalCadastrada.getCpf();
		contaDigitalPessoaFisicaInsercaoDto1.setCpf(cpfContaDigitalCadastrada);
		String mensagemEsperada = "Já existe uma conta digital cadastrada com o CPF " + cpfContaDigitalCadastrada + ".";
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(contaDigitalPessoaFisicaInsercaoDto1)
			.when()
				.post()
			.then()
				.extract()
					.body()
						.asString();
		
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CPF de uma conta digital já cadastrada,"
			+ " essa conta digital deve ser atualizada com os novos dados e não deve ser lançada uma exceção.")
	@Test
	@Order(5)
	void testAlteraContaDigital_ComCpfContaDigitalJaCadastrada_ContaDigitalDeveSerAtualizadaNaoDeveSerLancadaExcecao()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalCadastrada = buscaContaDigitalPessoaFisicaComSucessoPeloCpf(CPF_1);
		String cpfContaDigitalCadastrada = contaDigitalCadastrada.getCpf();
		contaDigitalPessoaFisicaAlteracaoDto1.setCpf(cpfContaDigitalCadastrada);
		
		String conteudoBodyResposta = given()
			.spec(requestSpecification).contentType(ContentType.JSON)
			.body(contaDigitalPessoaFisicaAlteracaoDto1)
		.when()
			.put()
		.then()
			.statusCode(200)
			.extract()
				.body()
					.asString();
		
		ContaDigitalPessoaFisicaAlteradaDto contaDigitalAlterada = objectMapper.readValue(conteudoBodyResposta,
				ContaDigitalPessoaFisicaAlteradaDto.class);
		
		assertEquals(contaDigitalPessoaFisicaAlteracaoDto1.getAgencia(), contaDigitalAlterada.getAgencia());
		assertEquals(contaDigitalPessoaFisicaAlteracaoDto1.getConta(), contaDigitalAlterada.getConta());
		assertEquals(contaDigitalPessoaFisicaAlteracaoDto1.getSenha(), contaDigitalAlterada.getSenha());
		assertEquals(contaDigitalPessoaFisicaAlteracaoDto1.getTelefone(), contaDigitalAlterada.getTelefone());
		assertEquals(contaDigitalPessoaFisicaAlteracaoDto1.getEmail(), contaDigitalAlterada.getEmail());
		assertEquals(contaDigitalPessoaFisicaAlteracaoDto1.getIdEndereco(), contaDigitalAlterada.getIdEndereco());
		assertEquals(contaDigitalCadastrada.getDataHoraCadastro(), contaDigitalAlterada.getDataHoraCadastro(),
				() -> "A data e a hora devem permanecer as mesmas que antes da alteração");
		assertEquals(contaDigitalPessoaFisicaAlteracaoDto1.getCpf(), contaDigitalAlterada.getCpf());
		assertEquals(contaDigitalPessoaFisicaAlteracaoDto1.getNomeCompleto(), contaDigitalAlterada.getNomeCompleto());
		assertEquals(contaDigitalPessoaFisicaAlteracaoDto1.getDataNascimento(), contaDigitalAlterada.getDataNascimento());
		assertEquals(contaDigitalPessoaFisicaAlteracaoDto1.getNomeCompletoMae(), contaDigitalAlterada.getNomeCompletoMae());
	}
	
	@DisplayName("Quando tenta inserir conta digital com a agência e a conta de uma conta digital já cadastrada "
			+ "deve ser lançada uma exceção.")
	@Order(6)
	@Test
	void testInsereContaDigital_ComAgenciaContaUtilizadasContaDigitalJaCadastrada_DeveSerLancadaExcecao()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalCadastrada = buscaContaDigitalPessoaFisicaComSucessoPeloCpf(CPF_1);
		String agencia = contaDigitalCadastrada.getAgencia();
		String conta = contaDigitalCadastrada.getConta();
		
		contaDigitalPessoaFisicaInsercaoDto1.setAgencia(agencia);
		contaDigitalPessoaFisicaInsercaoDto1.setConta(conta);
		
		String mensagemEsperada = "Já existe uma conta digital cadastrada a agência " + agencia + " e a conta " + conta
				+ ".";
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(contaDigitalPessoaFisicaInsercaoDto1)
			.when()
				.post()
			.then()
				.extract()
					.body()
						.asString();

		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando tenta alterar conta digital com a agência e a conta de uma outra conta digital já cadastrada "
			+ "deve ser lançada uma exceção.")
	@Order(7)
	@Test
	void testAlteraContaDigital_ComAgenciaContaUtilizadasOutraContaDigitalJaCadastrada_DeveSerLancadaExcecao()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalCadastrada = buscaContaDigitalPessoaFisicaComSucessoPeloCpf(CPF_1);
		String agencia = contaDigitalCadastrada.getAgencia();
		String conta = contaDigitalCadastrada.getConta();
		
		contaDigitalPessoaFisicaAlteracaoDto1.setAgencia(agencia);
		contaDigitalPessoaFisicaAlteracaoDto1.setConta(conta);
		
		String mensagemEsperada = "Já existe uma outra conta digital cadastrada a agência " + agencia + " e a conta " + conta
				+ ".";
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(contaDigitalPessoaFisicaAlteracaoDto1)
			.when()
				.put()
			.then()
				.extract()
					.body()
						.asString();

		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando deleta conta digital para pessoa física com sucesso deve ser retornado o código de status 204")
	@Order(8)
	@Test
	void testDeletaContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadoCodigoStatus204() {
		given()
			.spec(requestSpecification)
		.when()
			.delete("{cpf}", CPF_1)
		.then()
			.statusCode(204);
	}
	
	@DisplayName("Quando não insere conta digital para pessoa física com sucesso não deve ser retornado o código de status 201")
	@Test
	void testInsereContaDigitalPessoaFisica_SemSucesso_NaoDeveSerRetornadoCodigoStatus201() throws JsonProcessingException, Exception {
		String cpfNulo = null;
		String mensagemEsperada = "CPF não informado.";
		
		ContaDigitalPessoaFisicaInsercaoDto contaDigitalPessoaFisicaInsercaoDto = new ContaDigitalPessoaFisicaInsercaoDto(
				"1234567890", "0000000011", "12345678", "19980001234", "fulano@email.com", cpfNulo,
				"Fulano de Tal", LocalDate.of(2001, 1, 1), "Fulana de Tal");
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(contaDigitalPessoaFisicaInsercaoDto)
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
	
	@DisplayName("Quando busca conta digital para pessoa física sem sucesso "
			+ "deve ser retornado o código de status 404 e o body da resposta vazio")
	@Test
	void testBuscaContaDigitalPessoaFisica_SemSucesso_deveSerRetornadoCodigoStatus404SemConteudoBody()
			throws Exception {
		String conteudoBodyResposta = given()
				.spec(requestSpecification)
			.when()
				.get("{cpf}", CPF_SEM_CONTA_DIGITAL_ASSOCIADO_COM_ELE)
			.then()
				.statusCode(404)
			.extract()
				.body()
					.asString();

		assertEquals("", conteudoBodyResposta, () -> "O body da resposta possuí conteúdo, porém deveria estar vazio.");
	}
	
	@DisplayName("Quando altera conta digital para pessoa física sem sucesso não deve ser retornado o código de status 200")
	@Test
	void testAlteraContaDigitalPessoaFisica_SemSucesso_NaoDeveSerRetornadoCodigoStatus200() throws JsonProcessingException, Exception {
		String novoEmail = "novoEmailemail.com";
		String mensagemEsperada = "E-mail informado sem o símbolo @ (arroba).";
		
		ContaDigitalPessoaFisicaAlteracaoDto contaDigitalPessoaFisicaComNovosDados = new ContaDigitalPessoaFisicaAlteracaoDto(
				"1234567890", "0000000011", "12345678", "19980001234", novoEmail, 1L, CPF_1, "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification)
				.contentType(ContentType.JSON)
				.body(contaDigitalPessoaFisicaComNovosDados)
			.when()
				.put()
			.then()
				.assertThat().statusCode(not(equalTo(200)))
			.extract()
				.body()
					.asString();
		
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando remove conta digital para pessoa física sem sucesso não deve ser retornado o código de status 204")
	@Test
	void testRemoveContaDigitalPessoaFisica_SemSucesso_NaoDeveSerRetornadoCodigoStatus204() throws Exception {
		String mensagemEsperada = "Não foi encontrada uma conta com o CPF informado.";
		
		String conteudoBodyResposta = given()
			.spec(requestSpecification)
		.when()
			.delete("{cpf}", CPF_SEM_CONTA_DIGITAL_ASSOCIADO_COM_ELE)
		.then()
			.assertThat().statusCode(not(equalTo(204)))
		.extract()
			.body()
				.asString();
	
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	private ContaDigitalPessoaFisicaDTO1Busca buscaContaDigitalPessoaFisicaComSucessoPeloCpf(String cpf)
			throws JsonProcessingException, JsonMappingException {
		String conteudoBodyResposta = given()
					.spec(requestSpecification)
				.when()
					.get("{cpf}", cpf)
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisicaBuscadaBancoDados = objectMapper
				.readValue(conteudoBodyResposta, ContaDigitalPessoaFisicaDTO1Busca.class);
		return contaDigitalPessoaFisicaBuscadaBancoDados;
	}
}
