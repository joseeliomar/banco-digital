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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.example.dto.ContaPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.DadosParaDepositoContaPessoaJuridicaDto;
import com.example.dto.ItemExtratoContaPessoaJuridicaInsercaoDto;
import com.example.enumeration.TipoConta;
import com.example.openfeign.feignclient.ContaCorrentePoupancaMsFeignClient;
import com.example.openfeign.feignclient.ExtratoBancarioMsFeignClient;

@ExtendWith(MockitoExtension.class)
public class DepositoContaCorrentePessoaJuridicaServiceTests extends DepositoContaCorrenteServiceTests {
	
	@InjectMocks
	private DepositoContaCorrentePessoaJuridicaService depositoContaCorrentePessoaJuridicaService;
	
	@Mock
	private ExtratoBancarioMsFeignClient extratoBancarioMsFeignClient;

	@Mock
	private ContaCorrentePoupancaMsFeignClient contaCorrentePoupancaMsFeignClient;

	@DisplayName("Quando é efetuado um depósito para pessoa jurídica com sucesso, nenhuma exceção pode ser lançada")
	@Test
	void testEfetuaDeposito_ComSucesso_NenhumaExcecaoDeveSerLancada() {
		// Given
		String cnpjCliente = "12345678910001";

		ContaPessoaJuridicaBuscaDto1 contaCorrentePessoaJuridica = new ContaPessoaJuridicaBuscaDto1(1L, TipoConta.CORRENTE,
				1000.0, cnpjCliente);

		ResponseEntity<ContaPessoaJuridicaBuscaDto1> respostaBuscaContaPessoaJuridica = ResponseEntity
				.ok(contaCorrentePessoaJuridica);

		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaJuridica(anyString(), any(TipoConta.class)))
				.willReturn(respostaBuscaContaPessoaJuridica);

		DadosParaDepositoContaPessoaJuridicaDto dadosParaDepositoDto = new DadosParaDepositoContaPessoaJuridicaDto(
				cnpjCliente, 100.0);

		// When & Then
		assertDoesNotThrow(
				() -> depositoContaCorrentePessoaJuridicaService
						.efetuaDepositoContaCorrentePessoaJuridica(dadosParaDepositoDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);

		verify(contaCorrentePoupancaMsFeignClient).buscaContaPessoaJuridica(anyString(), any(TipoConta.class));
		verify(contaCorrentePoupancaMsFeignClient).alteraContaPessoaJuridica(any(ContaPessoaJuridicaAlteracaoDto.class));
		verify(extratoBancarioMsFeignClient)
				.insereItemExtratoContaPessoaJuridica(any(ItemExtratoContaPessoaJuridicaInsercaoDto.class));
	}
	
}
