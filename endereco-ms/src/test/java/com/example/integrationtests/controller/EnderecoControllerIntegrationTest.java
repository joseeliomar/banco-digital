package com.example.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.example.dto.EnderecoAlteracaoDto;
import com.example.dto.EnderecoAlteradoDto;
import com.example.dto.EnderecoBuscaDto1;
import com.example.dto.EnderecoInsercaoDto;
import com.example.dto.EnderecoInseridoDto;
import com.example.enumeration.UnidadeFederativa;
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
class EnderecoControllerIntegrationTest extends ConfiguracaoAmbienteTestesParaUsoContainers {
	
	private static final String CAMINHO_BASE = "/endereco/";

	private RequestSpecification requestSpecification;
	
	@Autowired
	private TestConfigs testConfigs;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final Long ID_SEM_ASSOCIACAO_COM_ALGUM_ENDERECO = 999999999999999999L; // Não há nenhum endereço com esse ID
	
	private Long idPrimeiroEnderecoInseridoBancoDados;

	
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
	
	@DisplayName("Quando insere endereço com sucesso deve ser retornado o header Location e o código de status 201")
	@Order(1)
	@Test
	void testInsereEndereco_ComSucesso_DeveSerRetornadoHeaderLocationMaisCodigoStatus201()
			throws JsonProcessingException, Exception {
		EnderecoInsercaoDto enderecoInsercaoDto = new EnderecoInsercaoDto("Rua tal", 500, "Bairro tal", "Florianópolis",
				UnidadeFederativa.SC, "15444444");

		Response response = insereEndereco(enderecoInsercaoDto);
				
		response.then().statusCode(201);
				
		String valorHeaderLocation = response.getHeader("Location");
		String conteudoBodyResposta = response.asString();

		EnderecoInseridoDto enderecoInserido = objectMapper.readValue(conteudoBodyResposta, EnderecoInseridoDto.class);
		
		assertNotNull(valorHeaderLocation);
		
		String sufixoUriRecursoCriado = CAMINHO_BASE + enderecoInserido.getId(); // não contém a porta
		assertTrue(valorHeaderLocation.endsWith(sufixoUriRecursoCriado),
				() -> "O valor presente no header Location não termina com o sufixo esperado.");
		
		assertNotNull(enderecoInserido);
		assertNotNull(enderecoInserido.getId());
		assertNotNull(enderecoInserido.getRua());
		assertNotNull(enderecoInserido.getNumero());
		assertNotNull(enderecoInserido.getBairro());
		assertNotNull(enderecoInserido.getMunicipio());
		assertNotNull(enderecoInserido.getUnidadeFederativa());
		assertNotNull(enderecoInserido.getCep());
		assertNotNull(enderecoInserido.getDataHoraCadastro());
		assertNull(enderecoInserido.getDataHoraAlteracao(),
				() -> "Não deve haver data e hora da alteração, pois o endereço ainda não foi alterado, "
						+ "apenas inserido no banco de dados");
		
		assertTrue(enderecoInserido.getId() > 0);
		assertEquals(enderecoInsercaoDto.getRua(), enderecoInserido.getRua());
		assertEquals(enderecoInsercaoDto.getNumero(), enderecoInserido.getNumero());
		assertEquals(enderecoInsercaoDto.getBairro(), enderecoInserido.getBairro());
		assertEquals(enderecoInsercaoDto.getMunicipio(), enderecoInserido.getMunicipio());
		assertEquals(enderecoInsercaoDto.getUnidadeFederativa(), enderecoInserido.getUnidadeFederativa());
		assertEquals(enderecoInsercaoDto.getCep(), enderecoInserido.getCep());
		
		idPrimeiroEnderecoInseridoBancoDados = enderecoInserido.getId();
	}

	private Response insereEndereco(EnderecoInsercaoDto enderecoInsercaoDto) {
		Response response = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(enderecoInsercaoDto)
			.when()
				.post();
		return response;
	}
	
	@DisplayName("Quando busca endereço com sucesso deve ser retornado um objeto com os dados do endereço"
			+ " e o código de status 200")
	@Order(2)
	@Test
	void testBuscaEndereco_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		EnderecoBuscaDto1 enderecoBuscadoBancoDados = buscaEnderecoPeloId(idPrimeiroEnderecoInseridoBancoDados);

		assertNotNull(enderecoBuscadoBancoDados);
		assertNotNull(enderecoBuscadoBancoDados.getRua());
		assertNotNull(enderecoBuscadoBancoDados.getNumero());
		assertNotNull(enderecoBuscadoBancoDados.getBairro());
		assertNotNull(enderecoBuscadoBancoDados.getMunicipio());
		assertNotNull(enderecoBuscadoBancoDados.getUnidadeFederativa());
		assertNotNull(enderecoBuscadoBancoDados.getCep());
		assertNotNull(enderecoBuscadoBancoDados.getDataHoraCadastro());
		assertNull(enderecoBuscadoBancoDados.getDataHoraAlteracao(),
				() -> "Não deve haver data e hora da alteração, pois o endereço ainda não foi alterado, "
				+ "apenas inserido no banco de dados");
	}

	@DisplayName("Quando altera endereço com sucesso deve ser retornado "
			+ "um objeto com os dados atualizados e o código de status 200")
	@Order(3)
	@Test
	void testAlteraEndereco_ComSucesso_DeveSerRetornadoObjetoComDadosAtualizadosMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		EnderecoBuscaDto1 enderecoBuscadoBancoDados = buscaEnderecoPeloId(idPrimeiroEnderecoInseridoBancoDados);

		String novaRua = "Rua do Frio", novoBairro = "Vila Tal", novoMunicipio = "Curitiba", novoCep = "18444444";
		Integer novoNumero = 199;
		UnidadeFederativa novaUnidadeFederativa = UnidadeFederativa.PR;
		
		EnderecoAlteracaoDto novosDadosParaAlteracao = new EnderecoAlteracaoDto(
				enderecoBuscadoBancoDados.getId(),
				novaRua, 
				novoNumero, 
				novoBairro,
				novoMunicipio, 
				novaUnidadeFederativa, 
				novoCep);

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

		EnderecoAlteradoDto enderecoAlterado = objectMapper.readValue(conteudoBodyResposta, EnderecoAlteradoDto.class);

		assertNotNull(enderecoAlterado);
		assertNotNull(enderecoAlterado.getRua());
		assertNotNull(enderecoAlterado.getNumero());
		assertNotNull(enderecoAlterado.getBairro());
		assertNotNull(enderecoAlterado.getMunicipio());
		assertNotNull(enderecoAlterado.getUnidadeFederativa());
		assertNotNull(enderecoAlterado.getCep());
		assertNotNull(enderecoAlterado.getDataHoraCadastro());
		assertNotNull(enderecoAlterado.getDataHoraAlteracao());
		
		assertEquals(novosDadosParaAlteracao.getRua(), enderecoAlterado.getRua());
		assertEquals(novosDadosParaAlteracao.getNumero(), enderecoAlterado.getNumero());
		assertEquals(novosDadosParaAlteracao.getBairro(), enderecoAlterado.getBairro());
		assertEquals(novosDadosParaAlteracao.getMunicipio(), enderecoAlterado.getMunicipio());
		assertEquals(novosDadosParaAlteracao.getUnidadeFederativa(), enderecoAlterado.getUnidadeFederativa());
		assertEquals(novosDadosParaAlteracao.getCep(), enderecoAlterado.getCep());
		assertEquals(enderecoBuscadoBancoDados.getDataHoraCadastro(), enderecoAlterado.getDataHoraCadastro());
	}
	
	@DisplayName("Quando deleta um endereço com sucesso deve ser retornado o código de status 204")
	@Order(4)
	@Test
	void testDeletaEndereco_ComSucesso_DeveSerRetornadoCodigoStatus204() {
		given()
			.spec(requestSpecification)
		.when()
			.delete("{idEndereco}", idPrimeiroEnderecoInseridoBancoDados)
		.then()
			.statusCode(204);
	}
	
	@DisplayName("Quando insere um endereço sem sucesso não deve ser retornado o código de status 201")
	@Test
	void testInsereEndereco_SemSucesso_NaoDeveSerRetornadoCodigoStatus201() throws JsonProcessingException, Exception {
		String cepNulo = null;
		String mensagemEsperada = "CEP não informado.";
		
		EnderecoInsercaoDto enderecoInsercaoDto = new EnderecoInsercaoDto("Rua tal", 100, "Bairro tal", "São Paulo",
				UnidadeFederativa.SP, cepNulo);
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification).contentType(ContentType.JSON)
				.body(enderecoInsercaoDto)
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
	
	@DisplayName("Quando busca um endereço sem sucesso "
			+ "deve ser retornado o código de status 404 e o body da resposta vazio")
	@Test
	void testBuscaEndereco_SemSucesso_deveSerRetornadoCodigoStatus404SemConteudoBody()
			throws Exception {
		String conteudoBodyResposta = given()
				.spec(requestSpecification)
			.when()
				.get("{idEndereco}", ID_SEM_ASSOCIACAO_COM_ALGUM_ENDERECO)
			.then()
				.statusCode(404)
			.extract()
				.body()
					.asString();

		assertEquals("", conteudoBodyResposta, () -> "O body da resposta possuí conteúdo, porém deveria estar vazio.");
	}
	
	@DisplayName("Quando altera um endereço sem sucesso não deve ser retornado o código de status 200")
	@Test
	void testAlteraEndereco_SemSucesso_NaoDeveSerRetornadoCodigoStatus200() throws JsonProcessingException, Exception {
		String mensagemEsperada = "Não foi encontrado um endereço com o código informado.";
		
		EnderecoAlteracaoDto enderecoAlteracaoDto = new EnderecoAlteracaoDto(ID_SEM_ASSOCIACAO_COM_ALGUM_ENDERECO,
				"Rua tal", 200, "Bairro tal", "Rio de Janeiro", UnidadeFederativa.RJ, "14444444");
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification)
				.contentType(ContentType.JSON)
				.body(enderecoAlteracaoDto)
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
	
	@DisplayName("Quando remove um endereço sem sucesso não deve ser retornado o código de status 204")
	@Test
	void testRemoveEndereco_SemSucesso_NaoDeveSerRetornadoCodigoStatus204() throws Exception {
		String mensagemEsperada = "Não foi encontrado um endereço com o código informado.";
		
		String conteudoBodyResposta = given()
			.spec(requestSpecification)
		.when()
			.delete("{idEndereco}", ID_SEM_ASSOCIACAO_COM_ALGUM_ENDERECO)
		.then()
			.assertThat().statusCode(not(equalTo(204)))
		.extract()
			.body()
				.asString();
	
		DetalhesExcecaoDto detalhesExcecaoDto = objectMapper.readValue(conteudoBodyResposta, DetalhesExcecaoDto.class);
		
		assertEquals(mensagemEsperada, detalhesExcecaoDto.error());
	}
	
	@DisplayName("Quando busca endereços com sucesso deve ser retornado o body com conteúdo"
			+ " e o código de status 200")
	@Test
	void testBuscaEnderecos_ComSucesso_DeveSerRetornadoBodyComConteudoMaisCodigoStatus200()
			throws JsonMappingException, JsonProcessingException {
		var endereco1 = new EnderecoInsercaoDto("Rua tal", 501, "Bairro tal", "Florianópolis", UnidadeFederativa.SC, "15444444");
		var endereco2 = new EnderecoInsercaoDto("Rua tal", 502, "Bairro tal", "Florianópolis", UnidadeFederativa.SC, "15444444");
		
		insereEndereco(endereco1);
		insereEndereco(endereco2);
		
		String conteudoBodyResposta = given()
				.spec(requestSpecification)
			.when()
				.get()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();
	
		assertNotNull(conteudoBodyResposta);
		assertTrue(conteudoBodyResposta.length() > 0);
	}
	
	private EnderecoBuscaDto1 buscaEnderecoPeloId(Long idEndereco)
			throws JsonProcessingException, JsonMappingException {
		String conteudoBodyResposta = given()
					.spec(requestSpecification)
				.when()
					.get("{idEndereco}", idEndereco)
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		EnderecoBuscaDto1 enderecoBuscadoBancoDados = objectMapper
				.readValue(conteudoBodyResposta, EnderecoBuscaDto1.class);
		return enderecoBuscadoBancoDados;
	}
}
