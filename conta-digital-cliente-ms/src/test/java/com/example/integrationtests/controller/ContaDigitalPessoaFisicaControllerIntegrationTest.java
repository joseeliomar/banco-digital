package com.example.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

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
import io.restassured.specification.RequestSpecification;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ContaDigitalPessoaFisicaControllerIntegrationTest extends ConfiguracaoAmbienteTestesParaUsoContainers {
	
	private static final String CAMINHO_BASE = "/contaDigitalPessoaFisica/";

	private RequestSpecification requestSpecification;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private String cpf1;
	private String cpfSemContaDigitalAssociadoComEle;
	
	@BeforeAll
	public void setup() {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		requestSpecification = new RequestSpecBuilder()
				.setBasePath(CAMINHO_BASE)
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		cpf1 = "12345678901";
		cpfSemContaDigitalAssociadoComEle = "99999999999"; // Não há nenhuma conta digital com esse CPF
	}
	
	@DisplayName("Quando insere conta digital para pessoa física com sucesso deve ser retornada "
			+ "a URI (localização do recurso criado) e o código de status 201")
	@Order(1)
	@Test
	void testInsereContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadaUriMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		ContaDigitalPessoaFisicaInsercaoDto contaDigitalPessoaFisicaInsercaoDto = new ContaDigitalPessoaFisicaInsercaoDto(
				"1234567890", "0000000011", "12345678", "19980001234", "fulano@email.com", cpf1,
				"Fulano de Tal", LocalDate.of(2001, 1, 1), "Fulana de Tal");

		String localizacaoRecursoCriado = "http://localhost:" + TestConfigs.SERVER_PORT +  CAMINHO_BASE
				+ contaDigitalPessoaFisicaInsercaoDto.getCpf();

		String conteudoBodyResposta = given()
					.spec(requestSpecification).contentType(ContentType.JSON)
					.body(contaDigitalPessoaFisicaInsercaoDto)
				.when()
					.post()
				.then()
					.statusCode(201)
					.assertThat()
						.header("Location", equalTo(localizacaoRecursoCriado))
					.extract()
						.body()
							.asString();

		ContaDigitalPessoaFisicaInseridaDto contaDigitalPessoaFisicaInserida = objectMapper
				.readValue(conteudoBodyResposta, ContaDigitalPessoaFisicaInseridaDto.class);

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
				cpf1);

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
				cpf1);

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
	
	@DisplayName("Quando deleta conta digital para pessoa física com sucesso deve ser retornado o código de status 204")
	@Order(4)
	@Test
	void testDeletaContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadoCodigoStatus204() {
		given()
			.spec(requestSpecification)
		.when()
			.delete("{cpf}", cpf1)
		.then()
			.statusCode(204);
	}
	
	@DisplayName("Quando insere conta digital para pessoa física sem sucesso não deve ser retornado o código de status 201")
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
				.get("{cpf}", cpfSemContaDigitalAssociadoComEle)
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
				"1234567890", "0000000011", "12345678", "19980001234", novoEmail, 1L, cpf1, "Fulano de Tal",
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
			.delete("{cpf}", cpfSemContaDigitalAssociadoComEle)
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
