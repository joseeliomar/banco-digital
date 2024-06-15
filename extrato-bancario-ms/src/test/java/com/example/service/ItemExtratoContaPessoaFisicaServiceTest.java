package com.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.model.ItemExtratoContaPessoaFisica;
import com.example.repository.ItemExtratoContaPessoaFisicaRepository;

@ExtendWith(MockitoExtension.class)
public class ItemExtratoContaPessoaFisicaServiceTest extends ItemExtratoContaServiceTest {
	
	private static final String CPF_1 = "12345678901";
	
	@InjectMocks
	private ItemExtratoContaPessoaFisicaService itemExtratoContaPessoaFisicaService;
	
	@Mock
	private ItemExtratoContaPessoaFisicaRepository itemExtratoContaPessoaFisicaRepository;

	private ItemExtratoContaPessoaFisicaInsercaoDto itemExtratoContaPessoaFisicaInsercaoDto;
	private ItemExtratoContaPessoaFisica itemExtratoContaPessoaFisica1;

	
	@BeforeEach
	private void setup() {
		// Given
		itemExtratoContaPessoaFisicaInsercaoDto = new ItemExtratoContaPessoaFisicaInsercaoDto(TipoConta.CORRENTE,
				Operacao.DEPOSITO, "Depósito", Banco.BANCO_BRASIL, "0000000011", "1234567890", 100.0, CPF_1);
		
		itemExtratoContaPessoaFisica1 = new ItemExtratoContaPessoaFisica(
				itemExtratoContaPessoaFisicaInsercaoDto.getTipoContaDonaExtrato(), 
				itemExtratoContaPessoaFisicaInsercaoDto.getOperacaoEfetuada(), 
				itemExtratoContaPessoaFisicaInsercaoDto.getDescricaoOperacao(), 
				LocalDateTime.now(),
				itemExtratoContaPessoaFisicaInsercaoDto.getBancoDestino(), 
				itemExtratoContaPessoaFisicaInsercaoDto.getAgenciaDestino(), 
				itemExtratoContaPessoaFisicaInsercaoDto.getContaDestino(), 
				itemExtratoContaPessoaFisicaInsercaoDto.getValor(), 
				itemExtratoContaPessoaFisicaInsercaoDto.getCpfCliente());
	}

	@DisplayName("Quando insere com sucesso um item de extrato de conta de pessoa física, nenhuma exceção deve ser lançada")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComSucesso_NaoDeveSerLancadaExcecao() {
		// Given
		given(itemExtratoContaPessoaFisicaRepository.save(any(ItemExtratoContaPessoaFisica.class)))
				.willReturn(itemExtratoContaPessoaFisica1);
		
		// When & Then
		ItemExtratoContaPessoaFisica actual = assertDoesNotThrow(
				() -> itemExtratoContaPessoaFisicaService
						.insereItemExtratoContaPessoaFisica(itemExtratoContaPessoaFisicaInsercaoDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		verify(itemExtratoContaPessoaFisicaRepository).save(any(ItemExtratoContaPessoaFisica.class));
		assertNotNull(actual);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com o tipo de conta não informado deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComTipoContaNulo_DeveSerLancadaExcecao() {
		// Given
		TipoConta tipoContaNulo = null;
		itemExtratoContaPessoaFisicaInsercaoDto.setTipoContaDonaExtrato(tipoContaNulo);
		String mensagemEsperada = "O tipo de conta não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com a operação efetuada não informada deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComOperacaoEfetuadaNula_DeveSerLancadaExcecao() {
		// Given
		Operacao operacaoNula = null;
		itemExtratoContaPessoaFisicaInsercaoDto.setOperacaoEfetuada(operacaoNula);
		String mensagemEsperada = "A operação efetuada não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com a descrição da operação não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComDescricaoOperacaoNulo_DeveSerLancadaExcecao() {
		// Given
		String descricaoOperacaoNula = null;
		itemExtratoContaPessoaFisicaInsercaoDto.setDescricaoOperacao(descricaoOperacaoNula);
		String mensagemEsperada = "A descrição da operação foi não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com a descrição da operação não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComDescricaoOperacaoEmBranco_DeveSerLancadaExcecao() {
		// Given
		String descricaoOperacaoEmBranco = "       ";
		itemExtratoContaPessoaFisicaInsercaoDto.setDescricaoOperacao(descricaoOperacaoEmBranco);
		String mensagemEsperada = "A descrição da operação foi não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica() {
		return assertThrows(ValidacaoException.class,
				() -> itemExtratoContaPessoaFisicaService
						.insereItemExtratoContaPessoaFisica(itemExtratoContaPessoaFisicaInsercaoDto),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
}
