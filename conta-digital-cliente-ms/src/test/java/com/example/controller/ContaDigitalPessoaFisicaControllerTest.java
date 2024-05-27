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

import java.time.LocalDate;

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

import com.example.dto.ContaDigitalPessoaFisicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaFisicaInsercaoDto;
import com.example.exception.ValidacaoException;
import com.example.model.ContaDigitalPessoaFisica;
import com.example.service.ContaDigitalPessoaFisicaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
class ContaDigitalPessoaFisicaControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ContaDigitalPessoaFisicaService contaDigitalPessoaFisicaService;

	private ContaDigitalPessoaFisicaInsercaoDto contaDigitalPessoaFisicaInsercaoDto1;
	
	private ContaDigitalPessoaFisica contaDigitalPessoaFisica1;
	
	private String cpf1;
	
	@BeforeEach
	public void setup() {
		cpf1 = "12345678901";
		
		contaDigitalPessoaFisicaInsercaoDto1 = new ContaDigitalPessoaFisicaInsercaoDto("1234567890", "0000000011", "12345678",
				"19980001234", "fulano@email.com", 1L, null, null, cpf1, "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
		
		contaDigitalPessoaFisica1 = new ContaDigitalPessoaFisica("1234567890", "0000000011", "12345678",
				"19980001234", "fulano@email.com", 1L, null, null, cpf1, "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
	}

	@DisplayName("Quando insere conta digital para pessoa física com sucesso deve ser retornada a URI e o código de status 201")
	@Test
	void testInsereContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadaUriMaisCodigoStatus201() throws JsonProcessingException, Exception {
		// Given
		given(contaDigitalPessoaFisicaService.insereContaDigitalPessoaFisica(any(ContaDigitalPessoaFisicaInsercaoDto.class))).willReturn(contaDigitalPessoaFisica1);
		String localizacaoRecursoCriado = "http://localhost" + "/contaDigitalPessoaFisica/"
				+ contaDigitalPessoaFisicaInsercaoDto1.getCpf();
		
		// When
		ResultActions resultActions = mockMvc.perform(post("/contaDigitalPessoaFisica/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contaDigitalPessoaFisicaInsercaoDto1))
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
	
	@DisplayName("Quando insere conta digital para pessoa física sem sucesso não deve ser retornado o código de status 201")
	@Test
	void testInsereContaDigitalPessoaFisica_SemSucesso_NaoDeveSerRetornadoCodigoStatus201() throws JsonProcessingException, Exception {
		// Given
		given(contaDigitalPessoaFisicaService
				.insereContaDigitalPessoaFisica(any(ContaDigitalPessoaFisicaInsercaoDto.class)))
				.willThrow(new ValidacaoException("Teste", HttpStatus.BAD_REQUEST));
		
		contaDigitalPessoaFisicaInsercaoDto1.setCpf(null);
		
		// When
		ResultActions resultActions = mockMvc.perform(post("/contaDigitalPessoaFisica/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contaDigitalPessoaFisicaInsercaoDto1))
				);
		
		// Then
		int codigoStatusHttpResposta = resultActions
				.andDo(print())
				.andReturn().getResponse().getStatus();
		
		assertNotEquals(201, codigoStatusHttpResposta, () -> "O código de status http presente na resposta não pode ser igual a 201");
	}
	
	@DisplayName("Quando busca conta digital para pessoa física com sucesso "
			+ "deve ser retornado o código de status 200 e conteúdo no body da resposta")
	@Test
	void testBuscaContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadoCodigoStatus200ConteudoNoBody() throws JsonProcessingException, Exception {
		// Given
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisicaDTO1Busca = new ContaDigitalPessoaFisicaDTO1Busca(
				contaDigitalPessoaFisica1);
		given(contaDigitalPessoaFisicaService.buscaContaDigitalPeloCpfComRespostaSemSenha(anyString()))
				.willReturn(contaDigitalPessoaFisicaDTO1Busca);
		
		// When
		ResultActions resultActions = mockMvc.perform(get("/contaDigitalPessoaFisica/{cpf}", this.cpf1));
		String conteudoBodyResposta = resultActions.andReturn().getResponse().getContentAsString();
		
		// Then
		resultActions
		.andDo(print())
		.andExpect(status().is(200));
		
		assertEquals(objectMapper.writeValueAsString(contaDigitalPessoaFisicaDTO1Busca), conteudoBodyResposta,
				() -> "O conteúdo do body da resposta é diferente do esperado.");
	}
	
	@DisplayName("Quando busca conta digital para pessoa física sem sucesso "
			+ "deve ser retornado o código de status 404 e sem conteúdo no body da resposta")
	@Test
	void testBuscaContaDigitalPessoaFisica_SemSucesso_deveSerRetornadoCodigoStatus404SemConteudoBody()
			throws Exception {
		// Given
		given(contaDigitalPessoaFisicaService.buscaContaDigitalPeloCpfComRespostaSemSenha(anyString()))
				.willReturn(null);

		// When
		ResultActions resultActions = mockMvc.perform(get("/contaDigitalPessoaFisica/{cpf}", this.cpf1));
		String conteudoBodyResposta = resultActions.andReturn().getResponse().getContentAsString();

		// Then
		resultActions.andDo(print()).andExpect(status().is(404));
		
		assertEquals("", conteudoBodyResposta, () -> "O body da resposta possuí conteúdo, porém deveria estar vazio.");
	}

	@DisplayName("Quando altera conta digital para pessoa física com sucesso deve ser retornado o código de status 200")
	@Test
	void testAlteraContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadoCodigoStatus200() throws JsonProcessingException, Exception {
		// Given
		given(contaDigitalPessoaFisicaService.alteraContaDigitalPessoaFisica(any(ContaDigitalPessoaFisicaAlteracaoDto.class))).willReturn(contaDigitalPessoaFisica1);
		
		String novoEmail = "novoEmail@email.com";
		ContaDigitalPessoaFisicaAlteracaoDto contaDigitalPessoaFisicaComNovosDados = new ContaDigitalPessoaFisicaAlteracaoDto(
				"1234567890", "0000000011", "12345678", "19980001234", novoEmail, 1L, cpf1, "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
		
		contaDigitalPessoaFisica1.setEmail(novoEmail);
		
		// When
		ResultActions resultActions = mockMvc.perform(put("/contaDigitalPessoaFisica/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contaDigitalPessoaFisicaComNovosDados))
				);
		
		// Then
		resultActions
				.andDo(print())
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.cpf", is(contaDigitalPessoaFisicaComNovosDados.getCpf())))
				.andExpect(jsonPath("$.email", is(contaDigitalPessoaFisicaComNovosDados.getEmail())));
	}
	
	@DisplayName("Quando altera conta digital para pessoa física sem sucesso não deve ser retornado o código de status 200")
	@Test
	void testAlteraContaDigitalPessoaFisica_SemSucesso_NaoDeveSerRetornadoCodigoStatus200() throws JsonProcessingException, Exception {
		// Given
		given(contaDigitalPessoaFisicaService.alteraContaDigitalPessoaFisica(any(ContaDigitalPessoaFisicaAlteracaoDto.class)))
		.willThrow(new ValidacaoException("Teste", HttpStatus.BAD_REQUEST));
		
		String novoEmail = "novoEmail@email.com";
		ContaDigitalPessoaFisicaAlteracaoDto contaDigitalPessoaFisicaComNovosDados = new ContaDigitalPessoaFisicaAlteracaoDto(
				"1234567890", "0000000011", "12345678", "19980001234", novoEmail, 1L, cpf1, "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
		
		contaDigitalPessoaFisica1.setEmail(novoEmail);
		
		// When
		ResultActions resultActions = mockMvc.perform(put("/contaDigitalPessoaFisica/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contaDigitalPessoaFisicaComNovosDados))
				);
		
		// Then
		int codigoStatusHttpResposta = resultActions
				.andDo(print())
				.andReturn().getResponse().getStatus();
		
		assertNotEquals(200, codigoStatusHttpResposta, () -> "O código de status http presente na resposta não pode ser igual a 200");
	}
	
	@DisplayName("Quando remove conta digital para pessoa física com sucesso deve ser retornado o código de status 204")
	@Test
	void testRemoveContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadoCodigoStatus204() throws Exception {
		// Given
		willDoNothing().given(contaDigitalPessoaFisicaService).removeContaDigitalPessoaFisica(anyString());
		
		// When
		ResultActions resultActions = mockMvc.perform(delete("/contaDigitalPessoaFisica/{cpf}", this.cpf1));
		
		// Then
		resultActions
				.andDo(print())
				.andExpect(status().is(204));
	}
	
	@DisplayName("Quando remove conta digital para pessoa física sem sucesso não deve ser retornado o código de status 204")
	@Test
	void testRemoveContaDigitalPessoaFisica_SemSucesso_NaoDeveSerRetornadoCodigoStatus204() throws Exception {
		// Given
		willThrow(new ValidacaoException("Teste", HttpStatus.BAD_REQUEST)).given(contaDigitalPessoaFisicaService)
				.removeContaDigitalPessoaFisica(anyString());
		
		// When
		ResultActions resultActions = mockMvc.perform(delete("/contaDigitalPessoaFisica/{cpf}", this.cpf1));
		
		// Then
		int codigoStatusHttpResposta = resultActions
				.andDo(print())
				.andReturn().getResponse().getStatus();
		
		assertNotEquals(204, codigoStatusHttpResposta, () -> "O código de status http presente na resposta não pode ser igual a 204");
	}
}
