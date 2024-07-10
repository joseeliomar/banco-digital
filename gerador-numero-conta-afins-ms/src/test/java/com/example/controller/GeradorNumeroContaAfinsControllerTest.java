package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.dto.DadosContaDto;
import com.example.service.ContaService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = GeradorNumeroContaAfinsController.class)
public class GeradorNumeroContaAfinsControllerTest {

	private static final String CAMINHO_BASE = "/geradorNumeroContaAfins/";

	@Autowired
	MockMvc mockMvc;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@MockBean
	private ContaService contaService;
	
	@BeforeAll
	public static void setupAll() {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@DisplayName("Gera os dados da conta com sucesso.")
	@Test
	void testGeraDadosContaComSucesso() throws UnsupportedEncodingException, Exception {
		// Given
		DadosContaDto dadosContaGeradaDto = new DadosContaDto("0000000001", 2);
		given(contaService.geraDadosConta()).willReturn(dadosContaGeradaDto);
		
		// When
		ResultActions resultActions = mockMvc.perform(get(CAMINHO_BASE));
		
		// Then
		String conteudoBodyResposta = resultActions
				.andDo(print())
				.andExpect(status().is(200))
				.andReturn()
				.getResponse()
				.getContentAsString();
		
		DadosContaDto actual = objectMapper.readValue(conteudoBodyResposta, DadosContaDto.class);
		
		assertNotNull(actual);
		assertEquals(dadosContaGeradaDto.numeroConta(), actual.numeroConta());
		assertEquals(dadosContaGeradaDto.digitoVerificadorConta(), actual.digitoVerificadorConta());
	}
}
