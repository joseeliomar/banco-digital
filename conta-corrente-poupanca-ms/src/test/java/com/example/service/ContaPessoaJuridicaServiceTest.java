package com.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.ContaPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.ContaPessoaJuridicaInsercaoDto;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.model.ContaPessoaJuridica;
import com.example.repository.ContaPessoaJuridicaRepository;

@ExtendWith(MockitoExtension.class)
public class ContaPessoaJuridicaServiceTest extends ContaServiceTest {
	
	private static final String CNPJ_1 = "12345678990001";
	
	@InjectMocks
	private ContaPessoaJuridicaService contaPessoaJuridicaService;
	
	@Mock
	private ContaPessoaJuridicaRepository contaPessoaJuridicaRepository;
	
	private ContaPessoaJuridicaInsercaoDto contaPessoaJuridicaInsercaoDto;
	
	private ContaPessoaJuridicaAlteracaoDto contaPessoaJuridicaAlteracaoDto;
	
	private ContaPessoaJuridica contaPessoaJuridica1;
	
	private ContaPessoaJuridica contaPessoaJuridica2;
	
	@BeforeEach
	private void setup() {
		// Given
		contaPessoaJuridicaInsercaoDto = new ContaPessoaJuridicaInsercaoDto(TipoConta.CORRENTE, 0, CNPJ_1);
		
		contaPessoaJuridica1 = new ContaPessoaJuridica(
				contaPessoaJuridicaInsercaoDto.getTipoConta(),
				contaPessoaJuridicaInsercaoDto.getSaldo(), 
				contaPessoaJuridicaInsercaoDto.getCnpj());
		
		contaPessoaJuridicaAlteracaoDto = new ContaPessoaJuridicaAlteracaoDto(1L, 0);
		
		contaPessoaJuridica2 = new ContaPessoaJuridica(
				contaPessoaJuridicaInsercaoDto.getTipoConta(),
				contaPessoaJuridicaAlteracaoDto.getSaldo(), 
				contaPessoaJuridicaInsercaoDto.getCnpj());
	}

	@DisplayName("Quando insere com sucesso uma conta do tipo CORRENTE, nenhuma exceção deve ser lançada")
	@Test
	void testInsereContaCorrentePessoaJuridica_ComSucesso_NaoDeveSerLanacadaExcecao() {
		// Given
		given(contaPessoaJuridicaRepository.findByCnpjAndTipoConta(anyString(), any(TipoConta.class)))
				.willReturn(Optional.ofNullable(null));
		given(contaPessoaJuridicaRepository.save(any(ContaPessoaJuridica.class))).willReturn(contaPessoaJuridica1);
		
		// When & Then
		ContaPessoaJuridica actual = assertDoesNotThrow(
				() -> contaPessoaJuridicaService.insereContaPessoaJuridica(contaPessoaJuridicaInsercaoDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		verify(contaPessoaJuridicaRepository).save(any(ContaPessoaJuridica.class));
		assertNotNull(actual);
	}
	
	@DisplayName("Quando insere com sucesso uma conta do tipo POUPANÇA, nenhuma exceção deve ser lançada")
	@Test
	void testInsereContaPoupancaPessoaJuridica_ComSucesso_NaoDeveSerLanacadaExcecao() {
		// Given
		TipoConta tipoConta = TipoConta.POUPANCA;
		contaPessoaJuridicaInsercaoDto.setTipoConta(tipoConta);
		contaPessoaJuridica1.setTipoConta(contaPessoaJuridicaInsercaoDto.getTipoConta());
		given(contaPessoaJuridicaRepository.findByCnpjAndTipoConta(anyString(), any(TipoConta.class)))
		.willReturn(Optional.ofNullable(null));
		given(contaPessoaJuridicaRepository.save(any(ContaPessoaJuridica.class))).willReturn(contaPessoaJuridica1);
		
		// When & Then
		ContaPessoaJuridica actual = assertDoesNotThrow(
				() -> contaPessoaJuridicaService.insereContaPessoaJuridica(contaPessoaJuridicaInsercaoDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		verify(contaPessoaJuridicaRepository).save(any(ContaPessoaJuridica.class));
		assertNotNull(actual);
	}
	
	@DisplayName("Quando tenta inseir uma conta com o tipo de conta não informado deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaJuridica_ComTipoContaNulo_DeveSerLancadaExcecao() {
		// Given
		TipoConta tipoContaNulo = null;
		contaPessoaJuridicaInsercaoDto.setTipoConta(tipoContaNulo);
		String mensagemEsperada = "O tipo de conta não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com um saldo negativo deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaJuridica_ComSaldoNegativo_DeveSerLancadaExcecao() {
		// Given
		double saldoNegativo = -100;
		contaPessoaJuridicaInsercaoDto.setSaldo(saldoNegativo);
		String mensagemEsperada = "O saldo inicial deve ser zero.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com um saldo positivo deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaJuridica_ComSaldoPositivo_DeveSerLancadaExcecao() {
		// Given
		double saldoPositivo = 100;
		contaPessoaJuridicaInsercaoDto.setSaldo(saldoPositivo);
		String mensagemEsperada = "O saldo inicial deve ser zero.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com o CNPJ não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaJuridica_ComCnpjNulo_DeveSerLancadaExcecao() {
		// Given
		String cnpjNulo = null;
		contaPessoaJuridicaInsercaoDto.setCnpj(cnpjNulo);
		String mensagemEsperada = "CNPJ não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com o CNPJ não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaJuridica_ComCnpjEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cnpjEmBranco = "       ";
		contaPessoaJuridicaInsercaoDto.setCnpj(cnpjEmBranco);
		String mensagemEsperada = "CNPJ não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com o CNPJ com menos de 14 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaJuridica_ComCnpjComMenos14Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cnpjCom13Caracteres = "1234567890123";
		contaPessoaJuridicaInsercaoDto.setCnpj(cnpjCom13Caracteres);
		String mensagemEsperada = "CNPJ com menos de 14 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com o CNPJ com mais de 14 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaJuridica_ComCnpjComMais14Caracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom15Caracteres = "123456789012345";
		contaPessoaJuridicaInsercaoDto.setCnpj(contaCom15Caracteres);
		String mensagemEsperada = "CNPJ com mais de 14 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta corrente com o CNPJ associado a conta corrente"
			+ " já cadastrada deve ser lançada uma exceção.")
	@Test
	void testInsereContaCorrentePessoaJuridica_ComCnpjContaCorrenteJaCadastrada_DeveSerLancadaExcecao() {
		// Given
		String cnpj = contaPessoaJuridicaInsercaoDto.getCnpj();
		TipoConta tipoConta = contaPessoaJuridicaInsercaoDto.getTipoConta();
		given(contaPessoaJuridicaRepository.findByCnpjAndTipoConta(cnpj, tipoConta))
				.willReturn(Optional.of(contaPessoaJuridica1));
		
		String mensagemEsperada = "Já existe uma conta " + tipoConta.getNome().toLowerCase()
				+ " cadastrada com o CNPJ " + cnpj + ".";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta poupança com o CNPJ associado a conta poupança"
			+ " já cadastrada deve ser lançada uma exceção.")
	@Test
	void testInsereContaPoupancaPessoaJuridica_ComCnpjContaPoupancaJaCadastrada_DeveSerLancadaExcecao() {
		// Given
		String cnpj = contaPessoaJuridicaInsercaoDto.getCnpj();
		TipoConta tipoConta = TipoConta.POUPANCA;
		contaPessoaJuridicaInsercaoDto.setTipoConta(tipoConta);
		contaPessoaJuridica1.setTipoConta(contaPessoaJuridicaInsercaoDto.getTipoConta());
		given(contaPessoaJuridicaRepository.findByCnpjAndTipoConta(cnpj, tipoConta))
				.willReturn(Optional.of(contaPessoaJuridica1));
		
		String mensagemEsperada = "Já existe uma conta " + tipoConta.getNome().toLowerCase()
				+ " cadastrada com o CNPJ " + cnpj + ".";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando altera com sucesso uma conta, nenhuma exceção deve ser lançada")
	@Test
	void testAlteraContaPessoaJuridica_ComSucesso_NaoDeveSerLanacadaExcecao() {
		// Given
		double novoSaldo = 100;
		contaPessoaJuridicaAlteracaoDto.setSaldo(novoSaldo);
		contaPessoaJuridica2.setSaldo(contaPessoaJuridicaAlteracaoDto.getSaldo());
		given(contaPessoaJuridicaRepository.findById(contaPessoaJuridicaAlteracaoDto.getId()))
				.willReturn(Optional.of(contaPessoaJuridica1));
		given(contaPessoaJuridicaRepository.save(any(ContaPessoaJuridica.class))).willReturn(contaPessoaJuridica2);
		
		// When
		ContaPessoaJuridica actual = assertDoesNotThrow(
				() -> contaPessoaJuridicaService.alteraContaPessoaJuridica(contaPessoaJuridicaAlteracaoDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		// Then
		verify(contaPessoaJuridicaRepository).save(any(ContaPessoaJuridica.class));
		assertNotNull(actual);
	}
	
	@DisplayName("Quando tenta alterar uma conta com o ID nulo deve ser lançada uma exceção.")
	@Test
	void testAlteraContaPessoaJuridica_ComIdNulo_DeveSerLancadaExcecao() {
		// Given
		Long idNulo = null;
		contaPessoaJuridicaAlteracaoDto.setId(idNulo);
		String mensagemEsperada = "O código da conta não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaPessoaJuridica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando busca conta pelo CNPJ e tipo de conta com sucesso não deve ser lançada uma exceção.")
	@Test
	void testBuscaContaPessoaJuridica_peloCnpjTipoContaComSucesso_NaoDeveSerLancadaExcecao() {
		// Given
		String cnpj = contaPessoaJuridica1.getCnpj();
		TipoConta tipoConta = contaPessoaJuridica1.getTipoConta();
		given(contaPessoaJuridicaRepository.findByCnpjAndTipoConta(cnpj, tipoConta))
				.willReturn(Optional.of(contaPessoaJuridica1));
		
		// When & Then
		ContaPessoaJuridicaBuscaDto1 actual = assertDoesNotThrow(
				() -> contaPessoaJuridicaService.buscaContaCompleta(cnpj, tipoConta),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		assertNotNull(actual, () -> "Não foi encontrada uma conta com o CNPJ e o tipo de conta que foram informados.");
	}
	
	@DisplayName("Quando busca conta pelo CNPJ e tipo de conta sem sucesso não deve ser lançada uma exceção.")
	@Test
	void testBuscaContaPessoaJuridica_peloCnpjTipoContaSemSucesso_NaoDeveSerLancadaExcecao() {
		// Given
		String cnpj = contaPessoaJuridica1.getCnpj();
		TipoConta tipoConta = contaPessoaJuridica1.getTipoConta();
		given(contaPessoaJuridicaRepository.findByCnpjAndTipoConta(cnpj, tipoConta))
				.willReturn(Optional.ofNullable(null));
		
		// When & Then
		ContaPessoaJuridicaBuscaDto1 actual = assertDoesNotThrow(
				() -> contaPessoaJuridicaService.buscaContaCompleta(cnpj, tipoConta),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		assertNull(actual, () -> "Não deveria ser retonada nenhuma conta.");
	}
	
	@DisplayName("Quando tenta remover endereço com o ID não informado deve ser lançada uma exceção")
	@Test
	void testRemoveContaPessoaFisica_ComIdNulo_DeveSerLancadaExcecao() {
		Long id = null;
		given(contaPessoaJuridicaRepository.findById(any())).willReturn(Optional.ofNullable(null));
		String mensagemEsperada = "Não foi encontrada uma conta com o código informado.";
		
		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoContaPessoaJuridica(id);

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaJuridica() {
		return assertThrows(ValidacaoException.class,
				() -> contaPessoaJuridicaService.insereContaPessoaJuridica(contaPessoaJuridicaInsercaoDto),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaPessoaJuridica() {
		return assertThrows(ValidacaoException.class,
				() -> contaPessoaJuridicaService.alteraContaPessoaJuridica(contaPessoaJuridicaAlteracaoDto),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoContaPessoaJuridica(Long idContaPessoaJuridica) {
		return assertThrows(ValidacaoException.class,
				() -> contaPessoaJuridicaService.removeContaPessoaJuridica(idContaPessoaJuridica),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
}
