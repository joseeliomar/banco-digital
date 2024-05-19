package com.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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

	private ContaDigitalPessoaFisicaInsercaoDto contaDigitalPessoaFisicaInsercaoDto;
	
	private ContaDigitalPessoaFisica contaDigitalPessoaFisica1;
	
	@BeforeEach
	public void setup() {
		String cpf1 = "12345678901";
		contaDigitalPessoaFisicaInsercaoDto = new ContaDigitalPessoaFisicaInsercaoDto("1234567890", "0000000011", "12345678",
				"19980001234", "fulano@email.com", 1L, null, null, cpf1, "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
		contaDigitalPessoaFisica1 = new ContaDigitalPessoaFisica("1234567890", "0000000011", "12345678",
				"19980001234", "fulano@email.com", 1L, null, null, "cpf1", "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
	}

	@DisplayName("Quando insere conta digital pessoa física com sucesso deve ser retornada a URI e o código de status 201")
	@Test
	void testInsereContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadaUriMaisCodigoStatus201() throws JsonProcessingException, Exception {
		// Given
		given(contaDigitalPessoaFisicaService.insereContaDigitalPessoaFisica(any(ContaDigitalPessoaFisicaInsercaoDto.class))).willReturn(contaDigitalPessoaFisica1);
		
		// When
		ResultActions resultActions = mockMvc.perform(post("/contaDigitalPessoaFisica")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contaDigitalPessoaFisicaInsercaoDto))
				);
		
		// Then
		resultActions.andDo(print()).andExpect(status().is(201))
				.andExpect(header().string("Location", "contaDigitalPessoaFisica/1"));
	}

}
