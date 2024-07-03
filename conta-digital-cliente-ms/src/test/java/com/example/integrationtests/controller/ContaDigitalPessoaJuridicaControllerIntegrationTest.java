package com.example.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.example.dto.ContaDigitalPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaJuridicaAlteradaDto;
import com.example.dto.ContaDigitalPessoaJuridicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaJuridicaInsercaoDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.DetalhesExcecaoDto;
import com.example.dto.EnderecoInseridoDto;
import com.example.enumeration.TipoConta;
import com.example.integrationtests.testcontainers.ConfiguracaoAmbienteTestesParaUsoContainers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
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
class ContaDigitalPessoaJuridicaControllerIntegrationTest extends ConfiguracaoAmbienteTestesParaUsoContainers {
	
	private static final String CAMINHO_BASE = "/contaDigitalPessoaJuridica/";

	private RequestSpecification requestSpecificationContaDigitalPessoaJuridica;
	
	private RequestSpecification requestSpecificationContaPessoaJuridica;
	
	@Autowired
	private TestConfigs testConfigs;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MetodosEmComumIntegrationTest metodosEmComumIntegrationTest;
	
	private static final String CNPJ_1 = "12345678990001";
	private static final String CNPJ_2 = "12345678990002";
	private static final String CNPJ_SEM_CONTA_DIGITAL_ASSOCIADO_COM_ELE = "99999999999999"; // Não há nenhuma conta digital com esse CNPJ
	
	private ContaDigitalPessoaJuridicaInsercaoDto contaDigitalPessoaJuridicaInsercaoDto1;

	private ContaDigitalPessoaJuridicaAlteracaoDto contaDigitalPessoaJuridicaAlteracaoDto1;
	
	@BeforeAll
	public void setupAll() {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		RestAssured.filters(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL));
		
		requestSpecificationContaDigitalPessoaJuridica = new RequestSpecBuilder()
				.setBasePath(CAMINHO_BASE)
				.setPort(testConfigs.getServerPort())
				.build();
		
		requestSpecificationContaPessoaJuridica = new RequestSpecBuilder()
				.setBasePath("/contaPessoaJuridica/")
				.setPort(metodosEmComumIntegrationTest.obterUmaPortaMicrosservico("conta-corrente-poupanca-ms"))
				.build();
	}
	
	@BeforeEach
	public void setup() {
		contaDigitalPessoaJuridicaInsercaoDto1 = new ContaDigitalPessoaJuridicaInsercaoDto("0000000013", "1234567890",
				"12345678", "19980001234", "fulano@email.com", CNPJ_2, "Fábrica Tal");
		
		contaDigitalPessoaJuridicaAlteracaoDto1 = new ContaDigitalPessoaJuridicaAlteracaoDto("0000000012", "1234567999",
				"654115897", "19980009999", "email@email.com", 2L, CNPJ_2, "Fábrica Tal");
	}
	
	@DisplayName("Quando insere conta digital para pessoa jurídica com sucesso deve ser retornado "
			+ "o header Location e o código de status 201")
	@Order(1)
	@Test
	void testInsereContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadoHeaderLocationMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		ContaDigitalPessoaJuridicaInsercaoDto contaDigitalPessoaJuridicaInsercaoDto = new ContaDigitalPessoaJuridicaInsercaoDto("0000000011", "1234567890",
				"12345678", "19980001234", "fulano@email.com", CNPJ_1, "Fábrica Tal");

		String sufixoUriRecursoCriado = CAMINHO_BASE + contaDigitalPessoaJuridicaInsercaoDto.getCnpj(); // não contém a porta

		Response response = given()
				.spec(requestSpecificationContaDigitalPessoaJuridica).contentType(ContentType.JSON)
				.body(contaDigitalPessoaJuridicaInsercaoDto)
			.when()
				.post();
				
		response.then().statusCode(201);
				
		String valorHeaderLocation = response.getHeader("Location");
		String conteudoBodyResposta = response.asString();

		EnderecoInseridoDto contaDigitalPessoaJuridicaInserida = objectMapper
				.readValue(conteudoBodyResposta, EnderecoInseridoDto.class);
		
		assertNotNull(valorHeaderLocation);
		assertTrue(valorHeaderLocation.endsWith(sufixoUriRecursoCriado),
				() -> "O valor presente no header Location não termina com o sufixo esperado.");
		assertNotNull(contaDigitalPessoaJuridicaInserida);
		assertNotNull(contaDigitalPessoaJuridicaInserida.getAgencia());
		assertNotNull(contaDigitalPessoaJuridicaInserida.getConta());
		assertNotNull(contaDigitalPessoaJuridicaInserida.getSenha());
		assertNotNull(contaDigitalPessoaJuridicaInserida.getTelefone());
		assertNotNull(contaDigitalPessoaJuridicaInserida.getEmail());
		assertNotNull(contaDigitalPessoaJuridicaInserida.getDataHoraCadastro());
		assertNull(contaDigitalPessoaJuridicaInserida.getDataHoraAlteracao(),
				() -> "Não deve haver data e hora da alteração, pois a conta digital ainda não foi alterada, "
						+ "apenas inserida no banco de dados");
		assertNotNull(contaDigitalPessoaJuridicaInserida.getCnpj());
		assertNotNull(contaDigitalPessoaJuridicaInserida.getRazaoSocial());
		
		assertEquals(contaDigitalPessoaJuridicaInsercaoDto.getAgencia(), contaDigitalPessoaJuridicaInserida.getAgencia());
		assertEquals(contaDigitalPessoaJuridicaInsercaoDto.getConta(), contaDigitalPessoaJuridicaInserida.getConta());
		assertEquals(contaDigitalPessoaJuridicaInsercaoDto.getSenha(), contaDigitalPessoaJuridicaInserida.getSenha());
		assertEquals(contaDigitalPessoaJuridicaInsercaoDto.getTelefone(), contaDigitalPessoaJuridicaInserida.getTelefone());
		assertEquals(contaDigitalPessoaJuridicaInsercaoDto.getEmail(), contaDigitalPessoaJuridicaInserida.getEmail());
		assertEquals(contaDigitalPessoaJuridicaInsercaoDto.getCnpj(), contaDigitalPessoaJuridicaInserida.getCnpj());
		assertEquals(contaDigitalPessoaJuridicaInsercaoDto.getRazaoSocial(), contaDigitalPessoaJuridicaInserida.getRazaoSocial());
	}
	
	@DisplayName("Quando busca a conta corrente de pessoa jurídica, conta esta que deveria ser inserida"
			+ " com sucesso durante a inserção da conta digital, deve ser retornado um objeto não nulo"
			+ " com os dados da conta corrente inserida")
	@Order(2)
	@Test
	void testBuscaContaCorrentePessoaJuridica_ComSucesso_DeveSerRetornadoObjetoComDadosContaCorrente()
			throws JsonMappingException, JsonProcessingException {
		ContaPessoaJuridicaBuscaDto1 contaCorrentePessoaJuridica = buscaContaPessoaJuridica(CNPJ_1, TipoConta.CORRENTE, 200);
		
		assertNotNull(contaCorrentePessoaJuridica);
	}
	
	@DisplayName("Quando busca a conta poupança de pessoa jurídica, conta esta que deveria ser inserida"
			+ " com sucesso durante a inserção da conta digital, deve ser retornado um objeto não nulo"
			+ " com os dados da conta poupança inserida")
	@Order(3)
	@Test
	void testBuscaContaPoupancaPessoaJuridica_ComSucesso_DeveSerRetornadoObjetoComDadosContaPoupanca()
			throws JsonMappingException, JsonProcessingException {
		ContaPessoaJuridicaBuscaDto1 contaCorrentePessoaJuridica = buscaContaPessoaJuridica(CNPJ_1, TipoConta.POUPANCA, 200);
		
		assertNotNull(contaCorrentePessoaJuridica);
	}
	
	@DisplayName("Quando busca conta digital para pessoa jurídica com sucesso deve ser retornado "
			+ "um objeto com os dados da conta digital e o código de status 200")
	@Order(4)
	@Test
	void testBuscaContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalPessoaJuridicaBuscadaBancoDados = buscaContaDigitalPessoaJuridicaComSucessoPeloCnpj(
				CNPJ_1);

		assertNotNull(contaDigitalPessoaJuridicaBuscadaBancoDados);
		assertNotNull(contaDigitalPessoaJuridicaBuscadaBancoDados.getAgencia());
		assertNotNull(contaDigitalPessoaJuridicaBuscadaBancoDados.getConta());
		assertNotNull(contaDigitalPessoaJuridicaBuscadaBancoDados.getTelefone());
		assertNotNull(contaDigitalPessoaJuridicaBuscadaBancoDados.getEmail());
		assertNull(contaDigitalPessoaJuridicaBuscadaBancoDados.getIdEndereco(), () -> "Não deve haver um código de endereço,"
				+ " pois na inserção da conta não foi associado a essa conta um endereço");
		assertNotNull(contaDigitalPessoaJuridicaBuscadaBancoDados.getDataHoraCadastro());
		assertNull(contaDigitalPessoaJuridicaBuscadaBancoDados.getDataHoraAlteracao(),
				() -> "Não deve haver data e hora da alteração, pois a conta digital ainda não foi alterada, "
				+ "apenas inserida no banco de dados");
		assertNotNull(contaDigitalPessoaJuridicaBuscadaBancoDados.getCnpj());
		assertNotNull(contaDigitalPessoaJuridicaBuscadaBancoDados.getRazaoSocial());
	}

	@DisplayName("Quando altera conta digital para pessoa jurídica com sucesso deve ser retornado "
			+ "um objeto com os dados atualizados e o código de status 200")
	@Order(5)
	@Test
	void testAlteraContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalPessoaJuridicaBuscadaBancoDados = buscaContaDigitalPessoaJuridicaComSucessoPeloCnpj(
				CNPJ_1);

		String novaSenha = "123456@55", novoTelefone = "19980005555", novoEmail = "ciclano@email.com",
				razaoSocial = "Fábrica A";
		Long idEndereco = 1L;
		ContaDigitalPessoaJuridicaAlteracaoDto novosDadosParaAlteracao = new ContaDigitalPessoaJuridicaAlteracaoDto(
				contaDigitalPessoaJuridicaBuscadaBancoDados.getAgencia(), 
				contaDigitalPessoaJuridicaBuscadaBancoDados.getConta(), 
				novaSenha, 
				novoTelefone, 
				novoEmail, 
				idEndereco, 
				contaDigitalPessoaJuridicaBuscadaBancoDados.getCnpj(),
				razaoSocial);

		String conteudoBodyResposta = given()
					.spec(requestSpecificationContaDigitalPessoaJuridica)
					.contentType(ContentType.JSON)
					.body(novosDadosParaAlteracao)
				.when()
					.put()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();

		ContaDigitalPessoaJuridicaAlteradaDto contaDigitalPessoaJuridicaAlterada = objectMapper
				.readValue(conteudoBodyResposta, ContaDigitalPessoaJuridicaAlteradaDto.class);

		assertNotNull(contaDigitalPessoaJuridicaAlterada);
		assertNotNull(contaDigitalPessoaJuridicaAlterada.getAgencia());
		assertNotNull(contaDigitalPessoaJuridicaAlterada.getConta());
		assertNotNull(contaDigitalPessoaJuridicaAlterada.getSenha());
		assertNotNull(contaDigitalPessoaJuridicaAlterada.getTelefone());
		assertNotNull(contaDigitalPessoaJuridicaAlterada.getEmail());
		assertNotNull(contaDigitalPessoaJuridicaAlterada.getIdEndereco());
		assertNotNull(contaDigitalPessoaJuridicaAlterada.getDataHoraCadastro());
		assertNotNull(contaDigitalPessoaJuridicaAlterada.getDataHoraAlteracao());
		assertNotNull(contaDigitalPessoaJuridicaAlterada.getCnpj());
		assertNotNull(contaDigitalPessoaJuridicaAlterada.getRazaoSocial());
		
		assertEquals(novosDadosParaAlteracao.getAgencia(), contaDigitalPessoaJuridicaAlterada.getAgencia());
		assertEquals(novosDadosParaAlteracao.getConta(), contaDigitalPessoaJuridicaAlterada.getConta());
		assertEquals(novosDadosParaAlteracao.getSenha(), contaDigitalPessoaJuridicaAlterada.getSenha());
		assertEquals(novosDadosParaAlteracao.getTelefone(), contaDigitalPessoaJuridicaAlterada.getTelefone());
		assertEquals(novosDadosParaAlteracao.getEmail(), contaDigitalPessoaJuridicaAlterada.getEmail());
		assertEquals(novosDadosParaAlteracao.getIdEndereco(), contaDigitalPessoaJuridicaAlterada.getIdEndereco());
		assertEquals(contaDigitalPessoaJuridicaBuscadaBancoDados.getDataHoraCadastro(), contaDigitalPessoaJuridicaAlterada.getDataHoraCadastro());
		assertEquals(novosDadosParaAlteracao.getCnpj(), contaDigitalPessoaJuridicaAlterada.getCnpj());
		assertEquals(novosDadosParaAlteracao.getRazaoSocial(), contaDigitalPessoaJuridicaAlterada.getRazaoSocial());
	}
	
	@DisplayName("Quando tenta inserir conta digital com o CNPJ de uma conta digital já cadastrada deve ser lançada uma exceção.")
	@Test
	@Order(6)
	void testInsereContaDigital_ComCnpjContaDigitalJaCadastrada_DeveSerLancadaExcecao() throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalCadastrada = buscaContaDigitalPessoaJuridicaComSucessoPeloCnpj(CNPJ_1);
		String cnpjContaDigitalCadastrada = contaDigitalCadastrada.getCnpj();
		contaDigitalPessoaJuridicaInsercaoDto1.setCnpj(cnpjContaDigitalCadastrada);
		String mensagemEsperada = "Já existe uma conta digital cadastrada com o CNPJ " + cnpjContaDigitalCadastrada + ".";
		
		String conteudoBodyResposta = given()
				.spec(requestSpecificationContaDigitalPessoaJuridica).contentType(ContentType.JSON)
				.body(contaDigitalPessoaJuridicaInsercaoDto1)
			.when()
				.post()
			.then()
				.extract()
					.body()
						.asString();
		
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CNPJ de uma conta digital já cadastrada,"
			+ " essa conta digital deve ser atualizada com os novos dados e não deve ser lançada uma exceção.")
	@Test
	@Order(7)
	void testAlteraContaDigital_ComCnpjContaDigitalJaCadastrada_ContaDigitalDeveSerAtualizadaNaoDeveSerLancadaExcecao()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalCadastrada = buscaContaDigitalPessoaJuridicaComSucessoPeloCnpj(CNPJ_1);
		String cnpjContaDigitalCadastrada = contaDigitalCadastrada.getCnpj();
		contaDigitalPessoaJuridicaAlteracaoDto1.setCnpj(cnpjContaDigitalCadastrada);
		
		String conteudoBodyResposta = given()
			.spec(requestSpecificationContaDigitalPessoaJuridica).contentType(ContentType.JSON)
			.body(contaDigitalPessoaJuridicaAlteracaoDto1)
		.when()
			.put()
		.then()
			.statusCode(200)
			.extract()
				.body()
					.asString();
		
		ContaDigitalPessoaJuridicaAlteradaDto contaDigitalAlterada = objectMapper.readValue(conteudoBodyResposta,
				ContaDigitalPessoaJuridicaAlteradaDto.class);
		
		assertEquals(contaDigitalPessoaJuridicaAlteracaoDto1.getAgencia(), contaDigitalAlterada.getAgencia());
		assertEquals(contaDigitalPessoaJuridicaAlteracaoDto1.getConta(), contaDigitalAlterada.getConta());
		assertEquals(contaDigitalPessoaJuridicaAlteracaoDto1.getSenha(), contaDigitalAlterada.getSenha());
		assertEquals(contaDigitalPessoaJuridicaAlteracaoDto1.getTelefone(), contaDigitalAlterada.getTelefone());
		assertEquals(contaDigitalPessoaJuridicaAlteracaoDto1.getEmail(), contaDigitalAlterada.getEmail());
		assertEquals(contaDigitalPessoaJuridicaAlteracaoDto1.getIdEndereco(), contaDigitalAlterada.getIdEndereco());
		assertEquals(contaDigitalCadastrada.getDataHoraCadastro(), contaDigitalAlterada.getDataHoraCadastro(),
				() -> "A data e a hora devem permanecer as mesmas que antes da alteração");
		assertEquals(contaDigitalPessoaJuridicaAlteracaoDto1.getCnpj(), contaDigitalAlterada.getCnpj());
		assertEquals(contaDigitalPessoaJuridicaAlteracaoDto1.getRazaoSocial(), contaDigitalAlterada.getRazaoSocial());
	}
	
	@DisplayName("Quando tenta inserir conta digital com a agência e a conta de uma conta digital já cadastrada "
			+ "deve ser lançada uma exceção.")
	@Order(8)
	@Test
	void testInsereContaDigital_ComAgenciaContaUtilizadasContaDigitalJaCadastrada_DeveSerLancadaExcecao()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalCadastrada = buscaContaDigitalPessoaJuridicaComSucessoPeloCnpj(CNPJ_1);
		String agencia = contaDigitalCadastrada.getAgencia();
		String conta = contaDigitalCadastrada.getConta();
		
		contaDigitalPessoaJuridicaInsercaoDto1.setAgencia(agencia);
		contaDigitalPessoaJuridicaInsercaoDto1.setConta(conta);
		
		String mensagemEsperada = "Já existe uma conta digital cadastrada a agência " + agencia + " e a conta " + conta
				+ ".";
		
		String conteudoBodyResposta = given()
				.spec(requestSpecificationContaDigitalPessoaJuridica).contentType(ContentType.JSON)
				.body(contaDigitalPessoaJuridicaInsercaoDto1)
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
	@Order(9)
	@Test
	void testAlteraContaDigital_ComAgenciaContaUtilizadasOutraContaDigitalJaCadastrada_DeveSerLancadaExcecao()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalCadastrada = buscaContaDigitalPessoaJuridicaComSucessoPeloCnpj(CNPJ_1);
		String agencia = contaDigitalCadastrada.getAgencia();
		String conta = contaDigitalCadastrada.getConta();
		
		contaDigitalPessoaJuridicaAlteracaoDto1.setAgencia(agencia);
		contaDigitalPessoaJuridicaAlteracaoDto1.setConta(conta);
		
		String mensagemEsperada = "Já existe uma outra conta digital cadastrada a agência " + agencia + " e a conta " + conta
				+ ".";
		
		String conteudoBodyResposta = given()
				.spec(requestSpecificationContaDigitalPessoaJuridica).contentType(ContentType.JSON)
				.body(contaDigitalPessoaJuridicaAlteracaoDto1)
			.when()
				.put()
			.then()
				.extract()
					.body()
						.asString();

		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando deleta conta digital para pessoa jurídica com sucesso deve ser retornado o código de status 204")
	@Order(10)
	@Test
	void testDeletaContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadoCodigoStatus204() {
		given()
			.spec(requestSpecificationContaDigitalPessoaJuridica)
		.when()
			.delete("{cnpj}", CNPJ_1)
		.then()
			.statusCode(204);
	}
	
	@DisplayName("Quando busca a conta corrente de pessoa jurídica após a remoção da conta digital "
			+ "deve ser retornado um objeto nulo, pois essa conta corrente deveria ter sido "
			+ "removida junto com a conta digital do cliente")
	@Order(11)
	@Test
	void testBuscaContaCorrentePessoaJuridica_ComSucessoAposRemocaoContaDigital_DeveSerRetornadoObjetoNulo()
			throws JsonMappingException, JsonProcessingException {
		ContaPessoaJuridicaBuscaDto1 contaCorrentePessoaJuridica = buscaContaPessoaJuridica(CNPJ_1, TipoConta.CORRENTE, 404);
		
		assertNull(contaCorrentePessoaJuridica);
	}
	
	@DisplayName("Quando busca a conta poupança de pessoa jurídica após a remoção da conta digital "
			+ "deve ser retornado um objeto nulo, pois essa conta poupança deveria ter sido "
			+ "removida junto com a conta digital do cliente")
	@Order(12)
	@Test
	void testBuscaContaPoupancaPessoaJuridica_ComSucessoAposRemocaoContaDigital_DeveSerRetornadoObjetoNulo()
			throws JsonMappingException, JsonProcessingException {
		ContaPessoaJuridicaBuscaDto1 contaCorrentePessoaJuridica = buscaContaPessoaJuridica(CNPJ_1, TipoConta.POUPANCA, 404);
		
		assertNull(contaCorrentePessoaJuridica);
	}
	
	@DisplayName("Quando insere conta digital para pessoa jurídica sem sucesso não deve ser retornado o código de status 201")
	@Test
	void testInsereContaDigitalPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus201() throws JsonProcessingException, Exception {
		String cnpjNulo = null;
		String mensagemEsperada = "CNPJ não informado.";
		
		ContaDigitalPessoaJuridicaInsercaoDto contaDigitalPessoaJuridicaInsercaoDto = new ContaDigitalPessoaJuridicaInsercaoDto("0000000022", "1234567999",
				"12345678", "19980001234", "ciclano@email.com", cnpjNulo, "Fábrica Tal");
		
		String conteudoBodyResposta = given()
				.spec(requestSpecificationContaDigitalPessoaJuridica).contentType(ContentType.JSON)
				.body(contaDigitalPessoaJuridicaInsercaoDto)
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
	
	@DisplayName("Quando busca conta digital para pessoa jurídica sem sucesso "
			+ "deve ser retornado o código de status 404 e o body da resposta vazio")
	@Test
	void testBuscaContaDigitalPessoaJuridica_SemSucesso_deveSerRetornadoCodigoStatus404SemConteudoBody()
			throws Exception {
		String conteudoBodyResposta = given()
				.spec(requestSpecificationContaDigitalPessoaJuridica)
			.when()
				.get("{cnpj}", CNPJ_SEM_CONTA_DIGITAL_ASSOCIADO_COM_ELE)
			.then()
				.statusCode(404)
			.extract()
				.body()
					.asString();

		assertEquals("", conteudoBodyResposta, () -> "O body da resposta possuí conteúdo, porém deveria estar vazio.");
	}
	
	@DisplayName("Quando altera conta digital para pessoa jurídica sem sucesso não deve ser retornado o código de status 200")
	@Test
	void testAlteraContaDigitalPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus200() throws JsonProcessingException, Exception {
		String novoEmail = "novoEmailemail.com";
		String mensagemEsperada = "E-mail informado sem o símbolo @ (arroba).";
		
		ContaDigitalPessoaJuridicaAlteracaoDto contaDigitalPessoaJuridicaComNovosDados = new ContaDigitalPessoaJuridicaAlteracaoDto(
				"0000000011", "1234567890", "12345678", "19980001234", novoEmail, 1L, CNPJ_1, "Fábrica Tal");
		
		String conteudoBodyResposta = given()
				.spec(requestSpecificationContaDigitalPessoaJuridica)
				.contentType(ContentType.JSON)
				.body(contaDigitalPessoaJuridicaComNovosDados)
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
	
	@DisplayName("Quando remove conta digital para pessoa jurídica sem sucesso não deve ser retornado o código de status 204")
	@Test
	void testRemoveContaDigitalPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus204() throws Exception {
		String mensagemEsperada = "Não foi encontrada uma conta com o CNPJ informado.";
		
		String conteudoBodyResposta = given()
			.spec(requestSpecificationContaDigitalPessoaJuridica)
		.when()
			.delete("{cnpj}", CNPJ_SEM_CONTA_DIGITAL_ASSOCIADO_COM_ELE)
		.then()
			.assertThat().statusCode(not(equalTo(204)))
		.extract()
			.body()
				.asString();
	
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	private ContaPessoaJuridicaBuscaDto1 buscaContaPessoaJuridica(String cnpj, TipoConta tipoConta,
			int codigoStatusHttpEsperado) throws JsonProcessingException, JsonMappingException {
		ValidatableResponse validatableResponse = given()
					.spec(requestSpecificationContaPessoaJuridica)
				.when()
					.get("{cnpj}/{tipoConta}", cnpj, tipoConta)
				.then()
					.statusCode(codigoStatusHttpEsperado);
		
		if (codigoStatusHttpEsperado == 404) {
			return null;
		}
		
		String conteudoBodyResposta = validatableResponse.extract().body().asString();
		ContaPessoaJuridicaBuscaDto1 contaPessoaJuridica = objectMapper.readValue(conteudoBodyResposta,
				ContaPessoaJuridicaBuscaDto1.class);
		return contaPessoaJuridica;
	}
	
	private ContaDigitalPessoaJuridicaDTO1Busca buscaContaDigitalPessoaJuridicaComSucessoPeloCnpj(String cnpj)
			throws JsonProcessingException, JsonMappingException {
		String conteudoBodyResposta = given()
					.spec(requestSpecificationContaDigitalPessoaJuridica)
				.when()
					.get("{cnpj}", cnpj)
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalPessoaJuridicaBuscadaBancoDados = objectMapper
				.readValue(conteudoBodyResposta, ContaDigitalPessoaJuridicaDTO1Busca.class);
		return contaDigitalPessoaJuridicaBuscadaBancoDados;
	}
}
