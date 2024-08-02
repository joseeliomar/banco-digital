package com.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.DadosParaSaqueContaPessoaFisicaDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.enumeration.TipoConta;

@ExtendWith(MockitoExtension.class)
public class SaqueContaCorrentePessoaFisicaServiceTests extends SaqueContaCorrenteServiceTests {
	
	@InjectMocks
	private SaqueContaCorrentePessoaFisicaService saqueContaCorrentePessoaFisicaService;
	
	@DisplayName("Quando é efetuado um saque para pessoa física com sucesso, nenhuma exceção pode ser lançada")
	@Test
	void testEfetuaSaque_ComSucesso_NenhumaExcecaoDeveSerLancada() {
		// Given
		String cpfCliente = "12345678901";

		var contaCorrentePessoaFisica = new ContaPessoaFisicaBuscaDto1(1L, TipoConta.CORRENTE, 1000.0, cpfCliente);
		var respostaBuscaContaPessoaFisica = ResponseEntity.ok(contaCorrentePessoaFisica);
		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaFisica(anyString(), any(TipoConta.class)))
				.willReturn(respostaBuscaContaPessoaFisica);
		
		var contaDigitalPessoaFisicaDTO1Busca = new ContaDigitalPessoaFisicaDTO1Busca("0000000011", "0000000001", 2,
				"19980001234", "fulano@email.com", 1L, null, null, cpfCliente, "Fulano de Tal", null, "Fulana de Tal");
		var respostaBuscaContaDigitalPessoaFisica = ResponseEntity.ok(contaDigitalPessoaFisicaDTO1Busca);
		given(contaDigitalClienteMsFeignClient.buscaContaDigitalPessoaFisica(cpfCliente))
				.willReturn(respostaBuscaContaDigitalPessoaFisica);

		DadosParaSaqueContaPessoaFisicaDto dadosParaSaqueDto = new DadosParaSaqueContaPessoaFisicaDto(
				cpfCliente, 100.0);

		// When & Then
		assertDoesNotThrow(
				() -> saqueContaCorrentePessoaFisicaService
						.efetuaSaqueContaCorrentePessoaFisica(dadosParaSaqueDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);

		verify(contaCorrentePoupancaMsFeignClient).buscaContaPessoaFisica(anyString(), any(TipoConta.class));
		verify(contaCorrentePoupancaMsFeignClient).alteraContaPessoaFisica(any(ContaPessoaFisicaAlteracaoDto.class));
		verify(extratoBancarioMsFeignClient)
				.insereItemExtratoContaPessoaFisica(any(ItemExtratoContaPessoaFisicaInsercaoDto.class));
		verify(contaDigitalClienteMsFeignClient).buscaContaDigitalPessoaFisica(cpfCliente);
	}
	
}
