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

import com.example.dto.ContaDigitalPessoaJuridicaDTO1Busca;
import com.example.dto.ContaPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.DadosParaSaqueContaPessoaJuridicaDto;
import com.example.dto.ItemExtratoContaPessoaJuridicaInsercaoDto;
import com.example.enumeration.TipoConta;

@ExtendWith(MockitoExtension.class)
public class SaqueContaCorrentePessoaJuridicaServiceTests extends SaqueContaCorrenteServiceTests {
	
	@InjectMocks
	private SaqueContaCorrentePessoaJuridicaService saqueContaCorrentePessoaJuridicaService;
	
	@DisplayName("Quando é efetuado um depósito para pessoa jurídica com sucesso, nenhuma exceção pode ser lançada")
	@Test
	void testEfetuaSaque_ComSucesso_NenhumaExcecaoDeveSerLancada() {
		// Given
		String cnpjCliente = "12345678910001";

		var contaCorrentePessoaJuridica = new ContaPessoaJuridicaBuscaDto1(1L, TipoConta.CORRENTE, 1000.0, cnpjCliente);
		var respostaBuscaContaPessoaJuridica = ResponseEntity.ok(contaCorrentePessoaJuridica);
		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaJuridica(anyString(), any(TipoConta.class)))
				.willReturn(respostaBuscaContaPessoaJuridica);
		
		var contaDigitalPessoaJuridicaDTO1Busca = new ContaDigitalPessoaJuridicaDTO1Busca("0000000011", "0000000001", 2,
				"19980001234", "fulano@email.com", 1L, null, null, cnpjCliente, "Fábrica Tal");
		var respostaBuscaContaDigitalPessoaJuridica = ResponseEntity.ok(contaDigitalPessoaJuridicaDTO1Busca);
		given(contaDigitalClienteMsFeignClient.buscaContaDigitalPessoaJuridica(cnpjCliente))
				.willReturn(respostaBuscaContaDigitalPessoaJuridica);

		DadosParaSaqueContaPessoaJuridicaDto dadosParaSaqueDto = new DadosParaSaqueContaPessoaJuridicaDto(
				cnpjCliente, 100.0);

		// When & Then
		assertDoesNotThrow(
				() -> saqueContaCorrentePessoaJuridicaService
						.efetuaSaqueContaCorrentePessoaJuridica(dadosParaSaqueDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);

		verify(contaCorrentePoupancaMsFeignClient).buscaContaPessoaJuridica(anyString(), any(TipoConta.class));
		verify(contaCorrentePoupancaMsFeignClient).alteraContaPessoaJuridica(any(ContaPessoaJuridicaAlteracaoDto.class));
		verify(extratoBancarioMsFeignClient)
				.insereItemExtratoContaPessoaJuridica(any(ItemExtratoContaPessoaJuridicaInsercaoDto.class));
		verify(contaDigitalClienteMsFeignClient).buscaContaDigitalPessoaJuridica(cnpjCliente);
	}
	
}
