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

import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.DadosParaDepositoContaPessoaFisicaDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.enumeration.TipoConta;
import com.example.feignclient.ContaPessoaFisicaFeignClient;
import com.example.feignclient.ItemExtratoContaPessoaFisicaFeignClient;

@ExtendWith(MockitoExtension.class)
public class DepositoServiceTests {

	@InjectMocks
	private DepositoService depositoService;
	
	@Mock
	private ItemExtratoContaPessoaFisicaFeignClient itemExtratoContaPessoaFisicaFeignClient;

	@Mock
	private ContaPessoaFisicaFeignClient contaPessoaFisicaFeignClient;

	@DisplayName("Quando é efetuado um depósito para pessoa física com sucesso, nenhuma exceção pode ser lançada")
	@Test
	void testEfetuaDeposito_ComSucesso_NenhumaExcecaoDeveSerLancada() {
		// Given
		String cpfCliente = "12345678901";
		
		ContaPessoaFisicaBuscaDto1 contaCorrentePessoaFisica = new ContaPessoaFisicaBuscaDto1(1L, TipoConta.CORRENTE, 1000.0, cpfCliente);
		
		ResponseEntity<Object> respostaBuscaContaPessoaFisica = ResponseEntity.ok(contaCorrentePessoaFisica);
		
//        given(contaPessoaFisicaFeignClient.buscaContaPessoaFisica(anyString(), any(TipoConta.class)))
//                .willReturn(respostaBuscaContaPessoaFisica);
		
		DadosParaDepositoContaPessoaFisicaDto dadosParaDepositoDto = new DadosParaDepositoContaPessoaFisicaDto(
				cpfCliente, 100.0, "0000000011", "1234567890");
		
		// When & Then
		assertDoesNotThrow(
				() -> depositoService.efetuaDepositoContaCorrentePessoaFisica(dadosParaDepositoDto),
				() -> "Não deve ser lançada nehuma exceção.");

		verify(contaPessoaFisicaFeignClient).buscaContaPessoaFisica(anyString(), any(TipoConta.class));
		verify(contaPessoaFisicaFeignClient).alteraContaPessoaFisica(any(ContaPessoaFisicaAlteracaoDto.class));
		verify(itemExtratoContaPessoaFisicaFeignClient).insereItemExtratoContaPessoaFisica(any(ItemExtratoContaPessoaFisicaInsercaoDto.class));
	}
}
