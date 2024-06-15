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

import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.ContaPessoaFisicaInsercaoDto;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.model.ContaPessoaFisica;
import com.example.repository.ContaPessoaFisicaRepository;

@ExtendWith(MockitoExtension.class)
public class ContaPessoaFisicaServiceTest extends ContaServiceTest {
	
	private static final String CPF_1 = "12345678901";
	
	@InjectMocks
	private ContaPessoaFisicaService contaPessoaFisicaService;
	
	@Mock
	private ContaPessoaFisicaRepository contaPessoaFisicaRepository;
	
	private ContaPessoaFisicaInsercaoDto contaPessoaFisicaInsercaoDto;
	
	private ContaPessoaFisicaAlteracaoDto contaPessoaFisicaAlteracaoDto;
	
	private ContaPessoaFisica contaPessoaFisica1;
	
	private ContaPessoaFisica contaPessoaFisica2;
	
	@BeforeEach
	private void setup() {
		// Given
		contaPessoaFisicaInsercaoDto = new ContaPessoaFisicaInsercaoDto(TipoConta.CORRENTE, 0, CPF_1);
		
		contaPessoaFisica1 = new ContaPessoaFisica(
				contaPessoaFisicaInsercaoDto.getTipoConta(),
				contaPessoaFisicaInsercaoDto.getSaldo(), 
				contaPessoaFisicaInsercaoDto.getCpf());
		
		contaPessoaFisicaAlteracaoDto = new ContaPessoaFisicaAlteracaoDto(1L, 0);
		
		contaPessoaFisica2 = new ContaPessoaFisica(
				contaPessoaFisicaInsercaoDto.getTipoConta(),
				contaPessoaFisicaAlteracaoDto.getSaldo(), 
				contaPessoaFisicaInsercaoDto.getCpf());
	}

	@DisplayName("Quando insere com sucesso uma conta do tipo CORRENTE, nenhuma exceção deve ser lançada")
	@Test
	void testInsereContaCorrentePessoaFisica_ComSucesso_NaoDeveSerLancadaExcecao() {
		// Given
		given(contaPessoaFisicaRepository.findByCpfAndTipoConta(anyString(), any(TipoConta.class)))
				.willReturn(Optional.ofNullable(null));
		given(contaPessoaFisicaRepository.save(any(ContaPessoaFisica.class))).willReturn(contaPessoaFisica1);
		
		// When & Then
		ContaPessoaFisica actual = assertDoesNotThrow(
				() -> contaPessoaFisicaService.insereContaPessoaFisica(contaPessoaFisicaInsercaoDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		verify(contaPessoaFisicaRepository).save(any(ContaPessoaFisica.class));
		assertNotNull(actual);
	}
	
	@DisplayName("Quando insere com sucesso uma conta do tipo POUPANÇA, nenhuma exceção deve ser lançada")
	@Test
	void testInsereContaPoupancaPessoaFisica_ComSucesso_NaoDeveSerLancadaExcecao() {
		// Given
		TipoConta tipoConta = TipoConta.POUPANCA;
		contaPessoaFisicaInsercaoDto.setTipoConta(tipoConta);
		contaPessoaFisica1.setTipoConta(contaPessoaFisicaInsercaoDto.getTipoConta());
		given(contaPessoaFisicaRepository.findByCpfAndTipoConta(anyString(), any(TipoConta.class)))
		.willReturn(Optional.ofNullable(null));
		given(contaPessoaFisicaRepository.save(any(ContaPessoaFisica.class))).willReturn(contaPessoaFisica1);
		
		// When & Then
		ContaPessoaFisica actual = assertDoesNotThrow(
				() -> contaPessoaFisicaService.insereContaPessoaFisica(contaPessoaFisicaInsercaoDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		verify(contaPessoaFisicaRepository).save(any(ContaPessoaFisica.class));
		assertNotNull(actual);
	}
	
	@DisplayName("Quando tenta inseir uma conta com o tipo de conta não informado deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaFisica_ComTipoContaNulo_DeveSerLancadaExcecao() {
		// Given
		TipoConta tipoContaNulo = null;
		contaPessoaFisicaInsercaoDto.setTipoConta(tipoContaNulo);
		String mensagemEsperada = "O tipo de conta não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com um saldo negativo deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaFisica_ComSaldoNegativo_DeveSerLancadaExcecao() {
		// Given
		double saldoNegativo = -100;
		contaPessoaFisicaInsercaoDto.setSaldo(saldoNegativo);
		String mensagemEsperada = "O saldo inicial deve ser zero.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com um saldo positivo deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaFisica_ComSaldoPositivo_DeveSerLancadaExcecao() {
		// Given
		double saldoPositivo = 100;
		contaPessoaFisicaInsercaoDto.setSaldo(saldoPositivo);
		String mensagemEsperada = "O saldo inicial deve ser zero.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com o CPF não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaFisica_ComCpfNulo_DeveSerLancadaExcecao() {
		// Given
		String cpfNulo = null;
		contaPessoaFisicaInsercaoDto.setCpf(cpfNulo);
		String mensagemEsperada = "CPF não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com o CPF não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaFisica_ComCpfEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cpfEmBranco = "       ";
		contaPessoaFisicaInsercaoDto.setCpf(cpfEmBranco);
		String mensagemEsperada = "CPF não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com o CPF com menos de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaFisica_ComCpfComMenos11Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cpfCom10Caracteres = "1234567890";
		contaPessoaFisicaInsercaoDto.setCpf(cpfCom10Caracteres);
		String mensagemEsperada = "CPF com menos de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta com o CPF com mais de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testInsereContaPessoaFisica_ComCpfComMaisOnzeCaracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom12Caracteres = "123456789012";
		contaPessoaFisicaInsercaoDto.setCpf(contaCom12Caracteres);
		String mensagemEsperada = "CPF com mais de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta corrente com o CPF associado a conta corrente"
			+ " já cadastrada deve ser lançada uma exceção.")
	@Test
	void testInsereContaCorrentePessoaFisica_ComCpfContaCorrenteJaCadastrada_DeveSerLancadaExcecao() {
		// Given
		String cpf = contaPessoaFisicaInsercaoDto.getCpf();
		TipoConta tipoConta = contaPessoaFisicaInsercaoDto.getTipoConta();
		given(contaPessoaFisicaRepository.findByCpfAndTipoConta(cpf, tipoConta))
				.willReturn(Optional.of(contaPessoaFisica1));
		
		String mensagemEsperada = "Já existe uma conta " + tipoConta.getNome().toLowerCase()
				+ " cadastrada com o CPF " + cpf + ".";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta inseir uma conta poupança com o CPF associado a conta poupança"
			+ " já cadastrada deve ser lançada uma exceção.")
	@Test
	void testInsereContaPoupancaPessoaFisica_ComCpfContaPoupancaJaCadastrada_DeveSerLancadaExcecao() {
		// Given
		String cpf = contaPessoaFisicaInsercaoDto.getCpf();
		TipoConta tipoConta = TipoConta.POUPANCA;
		contaPessoaFisicaInsercaoDto.setTipoConta(tipoConta);
		contaPessoaFisica1.setTipoConta(contaPessoaFisicaInsercaoDto.getTipoConta());
		given(contaPessoaFisicaRepository.findByCpfAndTipoConta(cpf, tipoConta))
				.willReturn(Optional.of(contaPessoaFisica1));
		
		String mensagemEsperada = "Já existe uma conta " + tipoConta.getNome().toLowerCase()
				+ " cadastrada com o CPF " + cpf + ".";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando altera com sucesso uma conta, nenhuma exceção deve ser lançada")
	@Test
	void testAlteraContaPessoaFisica_ComSucesso_NaoDeveSerLancadaExcecao() {
		// Given
		double novoSaldo = 100;
		contaPessoaFisicaAlteracaoDto.setSaldo(novoSaldo);
		contaPessoaFisica2.setSaldo(contaPessoaFisicaAlteracaoDto.getSaldo());
		given(contaPessoaFisicaRepository.findById(contaPessoaFisicaAlteracaoDto.getId()))
				.willReturn(Optional.of(contaPessoaFisica1));
		given(contaPessoaFisicaRepository.save(any(ContaPessoaFisica.class))).willReturn(contaPessoaFisica2);
		
		// When
		ContaPessoaFisica actual = assertDoesNotThrow(
				() -> contaPessoaFisicaService.alteraContaPessoaFisica(contaPessoaFisicaAlteracaoDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		// Then
		verify(contaPessoaFisicaRepository).save(any(ContaPessoaFisica.class));
		assertNotNull(actual);
	}
	
	@DisplayName("Quando tenta alterar uma conta com o ID nulo deve ser lançada uma exceção.")
	@Test
	void testAlteraContaPessoaFisica_ComIdNulo_DeveSerLancadaExcecao() {
		// Given
		Long idNulo = null;
		contaPessoaFisicaAlteracaoDto.setId(idNulo);
		String mensagemEsperada = "O código da conta não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaPessoaFisica();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando busca conta pelo CPF e tipo de conta com sucesso não deve ser lançada uma exceção.")
	@Test
	void testBuscaContaPessoaFisica_peloCpfTipoContaComSucesso_NaoDeveSerLancadaExcecao() {
		// Given
		String cpf = contaPessoaFisica1.getCpf();
		TipoConta tipoConta = contaPessoaFisica1.getTipoConta();
		given(contaPessoaFisicaRepository.findByCpfAndTipoConta(cpf, tipoConta))
				.willReturn(Optional.of(contaPessoaFisica1));
		
		// When & Then
		ContaPessoaFisicaBuscaDto1 actual = assertDoesNotThrow(
				() -> contaPessoaFisicaService.buscaContaCompleta(cpf, tipoConta),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		assertNotNull(actual, () -> "Não foi encontrada uma conta com o CPF e o tipo de conta que foram informados.");
	}
	
	@DisplayName("Quando busca conta pelo CPF e tipo de conta sem sucesso não deve ser lançada uma exceção.")
	@Test
	void testBuscaContaPessoaFisica_peloCpfTipoContaSemSucesso_NaoDeveSerLancadaExcecao() {
		// Given
		String cpf = contaPessoaFisica1.getCpf();
		TipoConta tipoConta = contaPessoaFisica1.getTipoConta();
		given(contaPessoaFisicaRepository.findByCpfAndTipoConta(cpf, tipoConta))
				.willReturn(Optional.ofNullable(null));
		
		// When & Then
		ContaPessoaFisicaBuscaDto1 actual = assertDoesNotThrow(
				() -> contaPessoaFisicaService.buscaContaCompleta(cpf, tipoConta),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		assertNull(actual, () -> "Não deveria ser retonada nenhuma conta.");
	}
	
	@DisplayName("Quando tenta remover endereço com o ID não informado deve ser lançada uma exceção")
	@Test
	void testRemoveContaPessoaFisica_ComIdNulo_DeveSerLancadaExcecao() {
		Long id = null;
		given(contaPessoaFisicaRepository.findById(any())).willReturn(Optional.ofNullable(null));
		String mensagemEsperada = "Não foi encontrada uma conta com o código informado.";
		
		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoContaPessoaFisica(id);

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoInsercaoContaPessoaFisica() {
		return assertThrows(ValidacaoException.class,
				() -> contaPessoaFisicaService.insereContaPessoaFisica(contaPessoaFisicaInsercaoDto),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaPessoaFisica() {
		return assertThrows(ValidacaoException.class,
				() -> contaPessoaFisicaService.alteraContaPessoaFisica(contaPessoaFisicaAlteracaoDto),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoContaPessoaFisica(Long idContaPessoaFisica) {
		return assertThrows(ValidacaoException.class,
				() -> contaPessoaFisicaService.removeContaPessoaFisica(idContaPessoaFisica),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
}
