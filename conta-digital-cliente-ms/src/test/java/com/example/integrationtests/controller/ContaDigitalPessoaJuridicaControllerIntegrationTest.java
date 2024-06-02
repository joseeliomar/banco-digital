package com.example.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import com.example.dto.ContaDigitalPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaJuridicaAlteradaDto;
import com.example.dto.ContaDigitalPessoaJuridicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaJuridicaInsercaoDto;
import com.example.dto.ContaDigitalPessoaJuridicaInseridaDto;
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
import io.restassured.specification.RequestSpecification;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ContaDigitalPessoaJuridicaControllerIntegrationTest extends ConfiguracaoAmbienteTestesParaUsoContainers {
	
	private static final String CAMINHO_BASE = "/contaDigitalPessoaJuridica/";

	private RequestSpecification requestSpecification;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private String cnpj;
	private String cnpjSemContaDigitalAssociadoComEle;
	
	@BeforeAll
	public void setup() {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		requestSpecification = new RequestSpecBuilder()
				.setBasePath(CAMINHO_BASE)
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		cnpj = "12345678990001";
		cnpjSemContaDigitalAssociadoComEle = "99999999999999"; // Não há nenhuma conta digital com esse CNPJ
	}
	
	@DisplayName("Quando insere conta digital para pessoa jurídica com sucesso deve ser retornada "
			+ "a URI (localização do recurso criado) e o código de status 201")
	@Order(1)
	@Test
	void testInsereContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadaUriMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		ContaDigitalPessoaJuridicaInsercaoDto contaDigitalPessoaJuridicaInsercaoDto = new ContaDigitalPessoaJuridicaInsercaoDto("0000000011", "1234567890",
				"12345678", "19980001234", "fulano@email.com", cnpj, "Fábrica Tal");

		String localizacaoRecursoCriado = "http://localhost:" + TestConfigs.SERVER_PORT +  CAMINHO_BASE
				+ contaDigitalPessoaJuridicaInsercaoDto.getCnpj();

		String conteudoBodyResposta = given()
					.spec(requestSpecification).contentType(ContentType.JSON)
					.body(contaDigitalPessoaJuridicaInsercaoDto)
				.when()
					.post()
				.then()
					.statusCode(201)
					.assertThat()
						.header("Location", equalTo(localizacaoRecursoCriado))
					.extract()
						.body()
							.asString();

		ContaDigitalPessoaJuridicaInseridaDto contaDigitalPessoaJuridicaInserida = objectMapper
				.readValue(conteudoBodyResposta, ContaDigitalPessoaJuridicaInseridaDto.class);

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
	
	@DisplayName("Quando busca conta digital para pessoa jurídica com sucesso deve ser retornado "
			+ "um objeto com os dados da conta digital e o código de status 200")
	@Order(2)
	@Test
	void testBuscaContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalPessoaJuridicaBuscadaBancoDados = buscaContaDigitalPessoaJuridicaComSucessoPeloCnpj(
				cnpj);

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
	@Order(3)
	@Test
	void testAlteraContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalPessoaJuridicaBuscadaBancoDados = buscaContaDigitalPessoaJuridicaComSucessoPeloCnpj(
				cnpj);

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
	
	@DisplayName("Quando deleta conta digital para pessoa jurídica com sucesso deve ser retornado o código de status 204")
	@Order(4)
	@Test
	void testDeletaContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadoCodigoStatus204() {
		given()
			.spec(requestSpecification)
		.when()
			.delete("{cnpj}", cnpj)
		.then()
			.statusCode(204);
	}
	
	@DisplayName("Quando insere conta digital para pessoa jurídica sem sucesso não deve ser retornado o código de status 201")
	@Test
	void testInsereContaDigitalPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus201() throws JsonProcessingException, Exception {
		String cnpjNulo = null;
		String mensagemEsperada = "CNPJ não informado.";
		
		ContaDigitalPessoaJuridicaInsercaoDto contaDigitalPessoaJuridicaInsercaoDto = new ContaDigitalPessoaJuridicaInsercaoDto("0000000022", "1234567999",
				"12345678", "19980001234", "ciclano@email.com", cnpjNulo, "Fábrica Tal");
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
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
				.spec(requestSpecification)
			.when()
				.get("{cnpj}", cnpjSemContaDigitalAssociadoComEle)
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
				"0000000011", "1234567890", "12345678", "19980001234", novoEmail, 1L, cnpj, "Fábrica Tal");
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification)
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
			.spec(requestSpecification)
		.when()
			.delete("{cnpj}", cnpjSemContaDigitalAssociadoComEle)
		.then()
			.assertThat().statusCode(not(equalTo(204)))
		.extract()
			.body()
				.asString();
	
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	private ContaDigitalPessoaJuridicaDTO1Busca buscaContaDigitalPessoaJuridicaComSucessoPeloCnpj(String cnpj)
			throws JsonProcessingException, JsonMappingException {
		String conteudoBodyResposta = given()
					.spec(requestSpecification)
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
