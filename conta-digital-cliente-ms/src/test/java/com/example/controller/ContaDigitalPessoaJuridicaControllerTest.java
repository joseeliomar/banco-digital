package com.example.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.dto.ContaDigitalPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaJuridicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaJuridicaInsercaoDto;
import com.example.exception.ValidacaoException;
import com.example.model.ContaDigitalPessoaJuridica;
import com.example.service.ContaDigitalPessoaJuridicaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ContaDigitalPessoaJuridicaController.class)
public class ContaDigitalPessoaJuridicaControllerTest {

	private static final String CAMINHO_BASE = "/contaDigitalPessoaJuridica/";

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ContaDigitalPessoaJuridicaService contaDigitalPessoaJuridicaService;

	private ContaDigitalPessoaJuridicaInsercaoDto contaDigitalPessoaJuridicaInsercaoDto1;
	
	private ContaDigitalPessoaJuridica contaDigitalPessoaJuridica1;
	
	private ContaDigitalPessoaJuridicaAlteracaoDto contaDigitalPessoaJuridicaAlteracaoDto1;
	
	private String cnpj1;
	
	@BeforeEach
	public void setup() {
		cnpj1 = "12345678990001";
		
		contaDigitalPessoaJuridicaInsercaoDto1 = new ContaDigitalPessoaJuridicaInsercaoDto("0000000011", "1234567890",
				"12345678", "19980001234", "fulano@email.com", cnpj1, "Fábrica Tal");
		
		contaDigitalPessoaJuridica1 = new ContaDigitalPessoaJuridica(
				contaDigitalPessoaJuridicaInsercaoDto1.getAgencia(), 
				contaDigitalPessoaJuridicaInsercaoDto1.getConta(),
				contaDigitalPessoaJuridicaInsercaoDto1.getSenha(), 
				contaDigitalPessoaJuridicaInsercaoDto1.getTelefone(),
				contaDigitalPessoaJuridicaInsercaoDto1.getEmail(), 
				null, 
				null, 
				null,
				contaDigitalPessoaJuridicaInsercaoDto1.getCnpj(),
				contaDigitalPessoaJuridicaInsercaoDto1.getRazaoSocial());
		
		contaDigitalPessoaJuridicaAlteracaoDto1 = new ContaDigitalPessoaJuridicaAlteracaoDto(
				contaDigitalPessoaJuridicaInsercaoDto1.getAgencia(), 
				contaDigitalPessoaJuridicaInsercaoDto1.getConta(), 
				contaDigitalPessoaJuridicaInsercaoDto1.getSenha(),
				contaDigitalPessoaJuridicaInsercaoDto1.getTelefone(), 
				contaDigitalPessoaJuridicaInsercaoDto1.getEmail(), 
				1L, 
				contaDigitalPessoaJuridicaInsercaoDto1.getCnpj(), 
				contaDigitalPessoaJuridicaInsercaoDto1.getRazaoSocial());
	}

	@DisplayName("Quando insere conta digital para pessoa jurídica com sucesso deve ser retornada a URI e o código de status 201")
	@Test
	void testInsereContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadaUriMaisCodigoStatus201() throws JsonProcessingException, Exception {
		// Given
		given(contaDigitalPessoaJuridicaService
				.insereContaDigitalPessoaJuridica(any(ContaDigitalPessoaJuridicaInsercaoDto.class)))
				.willReturn(contaDigitalPessoaJuridica1);
		String localizacaoRecursoCriado = "http://localhost" + CAMINHO_BASE
				+ contaDigitalPessoaJuridicaInsercaoDto1.getCnpj();
		
		// When
		ResultActions resultActions = mockMvc.perform(post(CAMINHO_BASE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contaDigitalPessoaJuridicaInsercaoDto1))
				);
		
		// Then
		String cabecalhoLocation = resultActions
				.andDo(print())
				.andExpect(status().is(201))
				.andExpect(header().exists("Location"))
				.andReturn()
				.getResponse()
				.getHeader("Location");
		
		assertEquals(localizacaoRecursoCriado, cabecalhoLocation, () -> "A localização presente no cabeçalho Location é diferente do esperado");
	}
	
	@DisplayName("Quando insere conta digital para pessoa jurídica sem sucesso não deve ser retornado o código de status 201")
	@Test
	void testInsereContaDigitalPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus201() throws JsonProcessingException, Exception {
		// Given
		given(contaDigitalPessoaJuridicaService
				.insereContaDigitalPessoaJuridica(any(ContaDigitalPessoaJuridicaInsercaoDto.class)))
				.willThrow(new ValidacaoException("Teste", HttpStatus.BAD_REQUEST));
		
		contaDigitalPessoaJuridicaInsercaoDto1.setCnpj(null);
		
		// When
		ResultActions resultActions = mockMvc.perform(post(CAMINHO_BASE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contaDigitalPessoaJuridicaInsercaoDto1))
				);
		
		// Then
		int codigoStatusHttpResposta = resultActions
				.andDo(print())
				.andReturn().getResponse().getStatus();
		
		assertNotEquals(201, codigoStatusHttpResposta, () -> "O código de status http presente na resposta não pode ser igual a 201");
	}
	
	@DisplayName("Quando busca conta digital para pessoa jurídica com sucesso "
			+ "deve ser retornado o código de status 200 e conteúdo no body da resposta")
	@Test
	void testBuscaContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadoCodigoStatus200ConteudoNoBody() throws JsonProcessingException, Exception {
		// Given
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalPessoaJuridicaDTO1Busca = new ContaDigitalPessoaJuridicaDTO1Busca(
				contaDigitalPessoaJuridica1);
		given(contaDigitalPessoaJuridicaService.buscaContaDigitalPeloCnpjComRespostaSemSenha(anyString()))
				.willReturn(contaDigitalPessoaJuridicaDTO1Busca);
		
		// When
		ResultActions resultActions = mockMvc.perform(get(CAMINHO_BASE + "{cnpj}", this.cnpj1));
		String conteudoBodyResposta = resultActions.andReturn().getResponse().getContentAsString();
		
		// Then
		resultActions
		.andDo(print())
		.andExpect(status().is(200));
		
		assertEquals(objectMapper.writeValueAsString(contaDigitalPessoaJuridicaDTO1Busca), conteudoBodyResposta,
				() -> "O conteúdo do body da resposta é diferente do esperado.");
	}
	
	@DisplayName("Quando busca conta digital para pessoa jurídica sem sucesso "
			+ "deve ser retornado o código de status 404 e o body da resposta vazio")
	@Test
	void testBuscaContaDigitalPessoaJuridica_SemSucesso_deveSerRetornadoCodigoStatus404SemConteudoBody()
			throws Exception {
		// Given
		given(contaDigitalPessoaJuridicaService.buscaContaDigitalPeloCnpjComRespostaSemSenha(anyString()))
				.willReturn(null);

		// When
		ResultActions resultActions = mockMvc.perform(get(CAMINHO_BASE + "{cnpj}", this.cnpj1));
		String conteudoBodyResposta = resultActions.andReturn().getResponse().getContentAsString();

		// Then
		resultActions.andDo(print()).andExpect(status().is(404));
		
		assertEquals("", conteudoBodyResposta, () -> "O body da resposta possuí conteúdo, porém deveria estar vazio.");
	}

	@DisplayName("Quando altera conta digital para pessoa jurídica com sucesso deve ser retornado o código de status 200")
	@Test
	void testAlteraContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadoCodigoStatus200() throws JsonProcessingException, Exception {
		// Given
		given(contaDigitalPessoaJuridicaService.alteraContaDigitalPessoaJuridica(any(ContaDigitalPessoaJuridicaAlteracaoDto.class))).willReturn(contaDigitalPessoaJuridica1);
		
		String novoEmail = "novoEmail@email.com";
		contaDigitalPessoaJuridicaAlteracaoDto1.setEmail(novoEmail);
		contaDigitalPessoaJuridica1.setEmail(novoEmail);
		
		// When
		ResultActions resultActions = mockMvc.perform(put(CAMINHO_BASE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contaDigitalPessoaJuridicaAlteracaoDto1))
				);
		
		// Then
		resultActions
				.andDo(print())
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.cnpj", is(contaDigitalPessoaJuridicaAlteracaoDto1.getCnpj())))
				.andExpect(jsonPath("$.email", is(contaDigitalPessoaJuridicaAlteracaoDto1.getEmail())));
	}
	
	@DisplayName("Quando altera conta digital para pessoa jurídica sem sucesso não deve ser retornado o código de status 200")
	@Test
	void testAlteraContaDigitalPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus200() throws JsonProcessingException, Exception {
		// Given
		given(contaDigitalPessoaJuridicaService.alteraContaDigitalPessoaJuridica(any(ContaDigitalPessoaJuridicaAlteracaoDto.class)))
		.willThrow(new ValidacaoException("Teste", HttpStatus.BAD_REQUEST));
		
		String novoEmail = "novoEmail@email.com";
		contaDigitalPessoaJuridicaAlteracaoDto1.setEmail(novoEmail);;
		contaDigitalPessoaJuridica1.setEmail(novoEmail);
		
		// When
		ResultActions resultActions = mockMvc.perform(put(CAMINHO_BASE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contaDigitalPessoaJuridicaAlteracaoDto1))
				);
		
		// Then
		int codigoStatusHttpResposta = resultActions
				.andDo(print())
				.andReturn().getResponse().getStatus();
		
		assertNotEquals(200, codigoStatusHttpResposta, () -> "O código de status http presente na resposta não pode ser igual a 200");
	}
	
	@DisplayName("Quando remove conta digital para pessoa jurídica com sucesso deve ser retornado o código de status 204")
	@Test
	void testRemoveContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadoCodigoStatus204() throws Exception {
		// Given
		willDoNothing().given(contaDigitalPessoaJuridicaService).removeContaDigitalPessoaJuridica(anyString());
		
		// When
		ResultActions resultActions = mockMvc.perform(delete(CAMINHO_BASE + "{cnpj}", this.cnpj1));
		
		// Then
		resultActions
				.andDo(print())
				.andExpect(status().is(204));
	}
	
	@DisplayName("Quando remove conta digital para pessoa jurídica sem sucesso não deve ser retornado o código de status 204")
	@Test
	void testRemoveContaDigitalPessoaJuridica_SemSucesso_NaoDeveSerRetornadoCodigoStatus204() throws Exception {
		// Given
		willThrow(new ValidacaoException("Teste", HttpStatus.BAD_REQUEST)).given(contaDigitalPessoaJuridicaService)
				.removeContaDigitalPessoaJuridica(anyString());
		
		// When
		ResultActions resultActions = mockMvc.perform(delete(CAMINHO_BASE + "{cnpj}", this.cnpj1));
		
		// Then
		int codigoStatusHttpResposta = resultActions
				.andDo(print())
				.andReturn().getResponse().getStatus();
		
		assertNotEquals(204, codigoStatusHttpResposta, () -> "O código de status http presente na resposta não pode ser igual a 204");
	}
}
