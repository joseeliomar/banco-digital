package com.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.ExtratoContaCorrenteDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.dto.MovimentacaoDto;
import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.model.ItemExtratoContaPessoaFisica;
import com.example.repository.ItemExtratoContaPessoaFisicaRepository;

@ExtendWith(MockitoExtension.class)
public class ExtratoContaPessoaFisicaServiceTest extends ItemExtratoContaServiceTest {
	
	private static final String CPF_1 = "12345678901";
	
	@InjectMocks
	private ExtratoContaPessoaFisicaService extratoContaPessoaFisicaService;
	
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
				() -> extratoContaPessoaFisicaService
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
		String mensagemEsperada = "A descrição da operação não foi informada.";

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
		String mensagemEsperada = "A descrição da operação não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com o banco de destino não informado deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComBancoDestinoNulo_DeveSerLancadaExcecao() {
		// Given
		Banco bancoDestinoNulo = null;
		itemExtratoContaPessoaFisicaInsercaoDto.setBancoDestino(bancoDestinoNulo);
		String mensagemEsperada = "O banco de destino não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com a agência de destino não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComAgenciaDestinoNula_DeveSerLancadaExcecao() {
		// Given
		String agenciaDestinoNula = null;
		itemExtratoContaPessoaFisicaInsercaoDto.setAgenciaDestino(agenciaDestinoNula);
		String mensagemEsperada = "A agência de destino não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com a agência de destino não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComAgenciaDestinoEmBranco_DeveSerLancadaExcecao() {
		// Given
		String agenciaDestinoEmBranco = "       ";
		itemExtratoContaPessoaFisicaInsercaoDto.setAgenciaDestino(agenciaDestinoEmBranco);
		String mensagemEsperada = "A agência de destino não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com a agência de destino com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComAgenciaDestinoComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaDestinoCom9Caracteres = "123456789";
		itemExtratoContaPessoaFisicaInsercaoDto.setAgenciaDestino(agenciaDestinoCom9Caracteres);
		String mensagemEsperada = "A agência de destino está com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com a agência de destino com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComAgenciaDestinoComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaDestinoCom11Caracteres = "12345678901";
		itemExtratoContaPessoaFisicaInsercaoDto.setAgenciaDestino(agenciaDestinoCom11Caracteres);
		String mensagemEsperada = "A agência de destino está com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com a conta de destino não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComContaDestinoNula_DeveSerLancadaExcecao() {
		// Given
		String contaDestinoNula = null;
		itemExtratoContaPessoaFisicaInsercaoDto.setContaDestino(contaDestinoNula);
		String mensagemEsperada = "A conta de destino não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com a conta de destino não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComContaDestinoEmBranco_DeveSerLancadaExcecao() {
		// Given
		String contaDestinoEmBranco = "       ";
		itemExtratoContaPessoaFisicaInsercaoDto.setContaDestino(contaDestinoEmBranco);
		String mensagemEsperada = "A conta de destino não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com a conta de destino com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComContaDestinoComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String contaDestinoCom9Caracteres = "123456789";
		itemExtratoContaPessoaFisicaInsercaoDto.setContaDestino(contaDestinoCom9Caracteres);
		String mensagemEsperada = "A conta de destino está com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com a conta de destino com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComContaDestinoComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String contaDestinoCom11Caracteres = "12345678901";
		itemExtratoContaPessoaFisicaInsercaoDto.setContaDestino(contaDestinoCom11Caracteres);
		String mensagemEsperada = "A conta de destino está com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com o CPF do cliente não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComCpfClienteNulo_DeveSerLancadaExcecao() {
		// Given
		String cpfClienteNulo = null;
		itemExtratoContaPessoaFisicaInsercaoDto.setCpfCliente(cpfClienteNulo);
		String mensagemEsperada = "O CPF do cliente não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com o CPF do cliente não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComCpfClienteEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cpfClienteEmBranco = "       ";
		itemExtratoContaPessoaFisicaInsercaoDto.setCpfCliente(cpfClienteEmBranco);
		String mensagemEsperada = "O CPF do cliente não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com o CPF do cliente com menos de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComCpfClienteComMenos11Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cpfClienteCom10Caracteres = "1234567890";
		itemExtratoContaPessoaFisicaInsercaoDto.setCpfCliente(cpfClienteCom10Caracteres);
		String mensagemEsperada = "O CPF do cliente está com menos de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa física"
			+ " com o CPF do cliente com mais de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaFisica_ComCpfClienteComMaisOnzeCaracteres_DeveSerLancadaExcecao() {
		// Given
		String cpfCom12Caracteres = "123456789012";
		itemExtratoContaPessoaFisicaInsercaoDto.setCpfCliente(cpfCom12Caracteres);
		String mensagemEsperada = "O CPF do cliente está com mais de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Na remoção de um item de extrato de conta de pessoa física pelo código"
			+ " deve ser executado o método delete do repository")
	@Test
	void testRemoveItemExtratoContaPessoaFisica_PeloId_DeveSerExecutadoMetodoDeleteDoRepository() {
		Long idItemExtratoContaPessoaFisica = itemExtratoContaPessoaFisica1.getId();
		given(itemExtratoContaPessoaFisicaRepository.findById(idItemExtratoContaPessoaFisica))
				.willReturn(Optional.of(itemExtratoContaPessoaFisica1));
		willDoNothing().given(itemExtratoContaPessoaFisicaRepository).delete(itemExtratoContaPessoaFisica1);

		extratoContaPessoaFisicaService.removeItemExtratoContaPessoaFisica(idItemExtratoContaPessoaFisica);

		verify(itemExtratoContaPessoaFisicaRepository, times(1)).delete(itemExtratoContaPessoaFisica1);
	}

	@DisplayName("Quando tenta remover um item de extrato de conta de pessoa física com o código não informado (string nula) deve ser lançada uma exceção")
	@Test
	void testRemoveItemExtratoContaPessoaFisica_ComIdNulo_DeveSerLancadaExcecao() {
		Long idItemExtratoContaPessoaFisicaNulo = null;
		String mensagemEsperada = "Não foi encontrado um item de extrato de conta de pessoa física com o código informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoItemExtratoContaPessoaFisica(
				idItemExtratoContaPessoaFisicaNulo);

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando gera o extrato conta corrente com sucesso, nenhuma exceção deve ser lançada")
	@Test
	void testGeraExtratoContaCorrente_ComSucesso_NaoDeveSerLancadaExcecao() {
		// Given
		String cpfCliente = CPF_1;
		int quantidadeDias = 30;
		
		List<ItemExtratoContaPessoaFisica> itensExtratoContaCorrente = new ArrayList<>();
		
		itensExtratoContaCorrente.add(new ItemExtratoContaPessoaFisica(TipoConta.CORRENTE, Operacao.DEPOSITO,
				"Depósito", LocalDateTime.now(), Banco.BANCO_BRASIL, "0000000011", "1234567890", 100.0, cpfCliente));
		
		LocalDate dataFinalPeriodo = LocalDate.now();
		LocalDate dataInicialPeriodo = dataFinalPeriodo.minusDays(quantidadeDias);
		
		given(itemExtratoContaPessoaFisicaRepository
				.buscaItensExtrato(
						cpfCliente, TipoConta.CORRENTE, dataInicialPeriodo, dataFinalPeriodo))
				.willReturn(itensExtratoContaCorrente);
		
		// When & Then
		ExtratoContaCorrenteDto actual = assertDoesNotThrow(
				() -> extratoContaPessoaFisicaService
						.geraExtratoContaCorrente(cpfCliente, quantidadeDias),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		assertNotNull(actual);
		assertNotNull(actual.nome());
		assertNotNull(actual.cpfCliente());
		assertNotNull(actual.agencia());
		assertNotNull(actual.conta());
		assertNotNull(actual.totalEntradas());
		assertNotNull(actual.totalSaidas());
		assertNotNull(actual.movimentacoes());
		
		for (MovimentacaoDto movimentacao : actual.movimentacoes()) {
			assertNotNull(movimentacao);
			assertNotNull(movimentacao.dia());
			assertNotNull(movimentacao.horario());
			assertNotNull(movimentacao.banco());
			assertNotNull(movimentacao.agenciaDestino());
			assertNotNull(movimentacao.contaDestino());
			assertNotNull(movimentacao.operacaoEfetuada());
			assertNotNull(movimentacao.descricaoOperacao());
			assertNotEquals(0.0, movimentacao.valor());
		}
	}

	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaFisica() {
		return assertThrows(ValidacaoException.class,
				() -> extratoContaPessoaFisicaService
						.insereItemExtratoContaPessoaFisica(itemExtratoContaPessoaFisicaInsercaoDto),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}

	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoItemExtratoContaPessoaFisica(
			Long idItemExtratoContaPessoaFisica) {
		return assertThrows(ValidacaoException.class,
				() -> extratoContaPessoaFisicaService
						.removeItemExtratoContaPessoaFisica(idItemExtratoContaPessoaFisica),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
}
