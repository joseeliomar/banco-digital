package com.example.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.model.ContaDigitalPessoaFisica;
import com.example.service.ContaDigitalPessoaFisicaService;
import com.example.services.controller.dto.ContaDigitalPessoaFisicaAlteracaoDto;
import com.example.services.controller.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.services.controller.dto.ContaDigitalPessoaFisicaInsercaoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
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

	@DisplayName("Quando insere conta digital pessoa física com sucesso deve ser retornada a URI e o código de status 201")
	@Test
	void testInsereContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadaUriMaisCodigoStatus201() throws JsonProcessingException, Exception {
		// Given
		given(contaDigitalPessoaFisicaService.insereContaDigitalPessoaFisica(any(ContaDigitalPessoaFisicaInsercaoDto.class))).willReturn(contaDigitalPessoaFisica1);
		
		// When
		ResultActions resultActions = mockMvc.perform(post("/contaDigitalPessoaFisica/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contaDigitalPessoaFisicaInsercaoDto1))
				);
		
		// Then
		resultActions
				.andDo(print())
				.andExpect(status().is(201));
	}
	
	@DisplayName("Quando busca conta digital pessoa física com sucesso "
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
		
		// Then
		resultActions
				.andDo(print())
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.cpf", is(contaDigitalPessoaFisicaDTO1Busca.getCpf())))
				.andExpect(jsonPath("$.email", is(contaDigitalPessoaFisicaDTO1Busca.getEmail())))
				.andExpect(jsonPath("$.nomeCompleto", is(contaDigitalPessoaFisicaDTO1Busca.getNomeCompleto())));
	}

	@DisplayName("Quando altera conta digital pessoa física com sucesso deve ser retornado o código de status 200")
	@Test
	void testAlteraContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadoCodigoStatus201() throws JsonProcessingException, Exception {
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
	
	@DisplayName("Quando remove conta digital pessoa física com sucesso deve ser retornado o código de status 204")
	@Test
	void testRemoveContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadoCodigoStatus204() throws JsonProcessingException, Exception {
		// Given
		willDoNothing().given(contaDigitalPessoaFisicaService).removeContaDigitalPessoaFisica(anyString());
		
		// When
		ResultActions resultActions = mockMvc.perform(delete("/contaDigitalPessoaFisica/{cpf}", this.cpf1));
		
		// Then
		resultActions
				.andDo(print())
				.andExpect(status().is(204));
	}
}
