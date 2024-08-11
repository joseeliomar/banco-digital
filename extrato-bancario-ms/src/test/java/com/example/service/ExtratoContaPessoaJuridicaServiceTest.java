package com.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.ItemExtratoContaPessoaJuridicaInsercaoDto;
import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.model.ItemExtratoContaPessoaJuridica;
import com.example.repository.ItemExtratoContaPessoaJuridicaRepository;

@ExtendWith(MockitoExtension.class)
public class ExtratoContaPessoaJuridicaServiceTest extends ItemExtratoContaServiceTest {
	
	private static final String CNPJ_1 = "12345678990001";
	
	@InjectMocks
	private ExtratoContaPessoaJuridicaService extratoContaPessoaJuridicaService;
	
	@Mock
	private ItemExtratoContaPessoaJuridicaRepository itemExtratoContaPessoaJuridicaRepository;

	private ItemExtratoContaPessoaJuridicaInsercaoDto itemExtratoContaPessoaJuridicaInsercaoDto;
	private ItemExtratoContaPessoaJuridica itemExtratoContaPessoaJuridica1;

	
	@BeforeEach
	private void setup() {
		// Given
		itemExtratoContaPessoaJuridicaInsercaoDto = new ItemExtratoContaPessoaJuridicaInsercaoDto(TipoConta.CORRENTE,
				Operacao.DEPOSITO, "Depósito", Banco.BANCO_BRASIL, "0000000011", "1234567890", 100.0, CNPJ_1);
		
		itemExtratoContaPessoaJuridica1 = new ItemExtratoContaPessoaJuridica(
				itemExtratoContaPessoaJuridicaInsercaoDto.getTipoContaDonaExtrato(), 
				itemExtratoContaPessoaJuridicaInsercaoDto.getOperacaoEfetuada(), 
				itemExtratoContaPessoaJuridicaInsercaoDto.getDescricaoOperacao(), 
				LocalDateTime.now(),
				itemExtratoContaPessoaJuridicaInsercaoDto.getBancoDestino(), 
				itemExtratoContaPessoaJuridicaInsercaoDto.getAgenciaDestino(), 
				itemExtratoContaPessoaJuridicaInsercaoDto.getContaDestino(), 
				itemExtratoContaPessoaJuridicaInsercaoDto.getValor(), 
				itemExtratoContaPessoaJuridicaInsercaoDto.getCnpjCliente());
	}

	@DisplayName("Quando insere com sucesso um item de extrato de conta de pessoa jurídica, nenhuma exceção deve ser lançada")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComSucesso_NaoDeveSerLancadaExcecao() {
		// Given
		given(itemExtratoContaPessoaJuridicaRepository.save(any(ItemExtratoContaPessoaJuridica.class)))
				.willReturn(itemExtratoContaPessoaJuridica1);
		
		// When & Then
		ItemExtratoContaPessoaJuridica actual = assertDoesNotThrow(
				() -> extratoContaPessoaJuridicaService
						.insereItemExtratoContaPessoaJuridica(itemExtratoContaPessoaJuridicaInsercaoDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		verify(itemExtratoContaPessoaJuridicaRepository).save(any(ItemExtratoContaPessoaJuridica.class));
		assertNotNull(actual);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com o tipo de conta não informado deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComTipoContaNulo_DeveSerLancadaExcecao() {
		// Given
		TipoConta tipoContaNulo = null;
		itemExtratoContaPessoaJuridicaInsercaoDto.setTipoContaDonaExtrato(tipoContaNulo);
		String mensagemEsperada = "O tipo de conta não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com a operação efetuada não informada deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComOperacaoEfetuadaNula_DeveSerLancadaExcecao() {
		// Given
		Operacao operacaoNula = null;
		itemExtratoContaPessoaJuridicaInsercaoDto.setOperacaoEfetuada(operacaoNula);
		String mensagemEsperada = "A operação efetuada não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com a descrição da operação não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComDescricaoOperacaoNulo_DeveSerLancadaExcecao() {
		// Given
		String descricaoOperacaoNula = null;
		itemExtratoContaPessoaJuridicaInsercaoDto.setDescricaoOperacao(descricaoOperacaoNula);
		String mensagemEsperada = "A descrição da operação não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com a descrição da operação não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComDescricaoOperacaoEmBranco_DeveSerLancadaExcecao() {
		// Given
		String descricaoOperacaoEmBranco = "       ";
		itemExtratoContaPessoaJuridicaInsercaoDto.setDescricaoOperacao(descricaoOperacaoEmBranco);
		String mensagemEsperada = "A descrição da operação não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com o banco de destino não informado deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComBancoDestinoNulo_DeveSerLancadaExcecao() {
		// Given
		Banco bancoDestinoNulo = null;
		itemExtratoContaPessoaJuridicaInsercaoDto.setBancoDestino(bancoDestinoNulo);
		String mensagemEsperada = "O banco de destino não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com a agência de destino não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComAgenciaDestinoNula_DeveSerLancadaExcecao() {
		// Given
		String agenciaDestinoNula = null;
		itemExtratoContaPessoaJuridicaInsercaoDto.setAgenciaDestino(agenciaDestinoNula);
		String mensagemEsperada = "A agência de destino não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com a agência de destino não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComAgenciaDestinoEmBranco_DeveSerLancadaExcecao() {
		// Given
		String agenciaDestinoEmBranco = "       ";
		itemExtratoContaPessoaJuridicaInsercaoDto.setAgenciaDestino(agenciaDestinoEmBranco);
		String mensagemEsperada = "A agência de destino não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com a agência de destino com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComAgenciaDestinoComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaDestinoCom9Caracteres = "123456789";
		itemExtratoContaPessoaJuridicaInsercaoDto.setAgenciaDestino(agenciaDestinoCom9Caracteres);
		String mensagemEsperada = "A agência de destino está com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com a agência de destino com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComAgenciaDestinoComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaDestinoCom11Caracteres = "12345678901";
		itemExtratoContaPessoaJuridicaInsercaoDto.setAgenciaDestino(agenciaDestinoCom11Caracteres);
		String mensagemEsperada = "A agência de destino está com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com a conta de destino não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComContaDestinoNula_DeveSerLancadaExcecao() {
		// Given
		String contaDestinoNula = null;
		itemExtratoContaPessoaJuridicaInsercaoDto.setContaDestino(contaDestinoNula);
		String mensagemEsperada = "A conta de destino não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com a conta de destino não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComContaDestinoEmBranco_DeveSerLancadaExcecao() {
		// Given
		String contaDestinoEmBranco = "       ";
		itemExtratoContaPessoaJuridicaInsercaoDto.setContaDestino(contaDestinoEmBranco);
		String mensagemEsperada = "A conta de destino não foi informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com a conta de destino com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComContaDestinoComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String contaDestinoCom9Caracteres = "123456789";
		itemExtratoContaPessoaJuridicaInsercaoDto.setContaDestino(contaDestinoCom9Caracteres);
		String mensagemEsperada = "A conta de destino está com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com a conta de destino com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComContaDestinoComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String contaDestinoCom11Caracteres = "12345678901";
		itemExtratoContaPessoaJuridicaInsercaoDto.setContaDestino(contaDestinoCom11Caracteres);
		String mensagemEsperada = "A conta de destino está com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com o CNPJ do cliente não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComCnpjClienteNulo_DeveSerLancadaExcecao() {
		// Given
		String cnpjClienteNulo = null;
		itemExtratoContaPessoaJuridicaInsercaoDto.setCnpjCliente(cnpjClienteNulo);
		String mensagemEsperada = "O CNPJ do cliente não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com o CNPJ do cliente não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComCnpjClienteEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cnpjClienteEmBranco = "       ";
		itemExtratoContaPessoaJuridicaInsercaoDto.setCnpjCliente(cnpjClienteEmBranco);
		String mensagemEsperada = "O CNPJ do cliente não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com o CNPJ do cliente com menos de 14 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComCnpjClienteComMenos14Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cnpjCom13Caracteres = "1234567899000";
		itemExtratoContaPessoaJuridicaInsercaoDto.setCnpjCliente(cnpjCom13Caracteres);
		String mensagemEsperada = "O CNPJ do cliente está com menos de 14 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir um item de extrato de conta de pessoa jurídica"
			+ " com o CNPJ do cliente com mais de 14 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereItemExtratoContaPessoaJuridica_ComCnpjClienteComMais14Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cnpjCom15Caracteres = "123456789900001";
		itemExtratoContaPessoaJuridicaInsercaoDto.setCnpjCliente(cnpjCom15Caracteres);
		String mensagemEsperada = "O CNPJ do cliente está com mais de 14 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Na remoção de um item de extrato de conta de pessoa jurídica pelo código"
			+ " deve ser executado o método delete do repository")
	@Test
	void testRemoveItemExtratoContaPessoaJuridica_PeloId_DeveSerExecutadoMetodoDeleteDoRepository() {
		Long idItemExtratoContaPessoaJuridica = itemExtratoContaPessoaJuridica1.getId();
		given(itemExtratoContaPessoaJuridicaRepository.findById(idItemExtratoContaPessoaJuridica))
				.willReturn(Optional.of(itemExtratoContaPessoaJuridica1));
		willDoNothing().given(itemExtratoContaPessoaJuridicaRepository).delete(itemExtratoContaPessoaJuridica1);

		extratoContaPessoaJuridicaService.removeItemExtratoContaPessoaJuridica(idItemExtratoContaPessoaJuridica);

		verify(itemExtratoContaPessoaJuridicaRepository, times(1)).delete(itemExtratoContaPessoaJuridica1);
	}

	@DisplayName("Quando tenta remover um item de extrato de conta de pessoa jurídica"
			+ " com o código não informado (string nula) deve ser lançada uma exceção")
	@Test
	void testRemoveItemExtratoContaPessoaJuridica_ComIdNulo_DeveSerLancadaExcecao() {
		Long idItemExtratoContaPessoaJuridicaNulo = null;
		String mensagemEsperada = "Não foi encontrado um item de extrato de conta de pessoa jurídica com o código informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoItemExtratoContaPessoaJuridica(
				idItemExtratoContaPessoaJuridicaNulo);

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoItemExtratoContaPessoaJuridica() {
		return assertThrows(ValidacaoException.class,
				() -> extratoContaPessoaJuridicaService
						.insereItemExtratoContaPessoaJuridica(itemExtratoContaPessoaJuridicaInsercaoDto),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}

	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoItemExtratoContaPessoaJuridica(
			Long idItemExtratoContaPessoaJuridica) {
		return assertThrows(ValidacaoException.class,
				() -> extratoContaPessoaJuridicaService
						.removeItemExtratoContaPessoaJuridica(idItemExtratoContaPessoaJuridica),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
}
