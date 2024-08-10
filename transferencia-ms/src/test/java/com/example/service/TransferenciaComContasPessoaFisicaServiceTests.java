package com.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
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
import com.example.dto.DadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceira;
import com.example.dto.DadosParaTransferenciaEntreContasClientesDiferentesDto;
import com.example.dto.DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto;
import com.example.dto.DadosParaTransferenciaEntreContasMesmoClienteDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.enumeration.Banco;
import com.example.enumeration.TipoConta;

@ExtendWith(MockitoExtension.class)
public class TransferenciaComContasPessoaFisicaServiceTests extends TransferenciaComContasServiceTests {
	
	@InjectMocks
	private TransferenciaComContasPessoaFisicaService transferenciaComContasPessoaFisicaService;
	
	private static final String CPF_CLIENTE_1 = "12345678901";
	
	private static final String CPF_CLIENTE_2 = "12345678902";
	
	@DisplayName("Quando é efetuada uma transferência de conta corrente para conta poupança com sucesso,"
			+ " nenhuma exceção pode ser lançada")
	@Test
	void testEfetuaTranferenciaContaCorrenteParaPoupanca_ComSucesso_NenhumaExcecaoDeveSerLancada() {
		// Given
		String cpfCliente = CPF_CLIENTE_1;

		var contaCorrentePessoaFisica = new ContaPessoaFisicaBuscaDto1(1L, TipoConta.CORRENTE, 1000.0, cpfCliente);
		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaFisica(anyString(), any(TipoConta.class)))
				.willReturn(ResponseEntity.ok(contaCorrentePessoaFisica));
		
		var contaDigitalPessoaFisicaDTO1Busca = criaContaDigitalDonoContaQueDinheiroSai(cpfCliente);
		given(contaDigitalClienteMsFeignClient.buscaContaDigitalPessoaFisica(cpfCliente))
				.willReturn(ResponseEntity.ok(contaDigitalPessoaFisicaDTO1Busca));

		var dadosParaTransferenciaDto = new DadosParaTransferenciaEntreContasMesmoClienteDto(cpfCliente, 100.0,
				TipoConta.CORRENTE);

		// When & Then
		assertDoesNotThrow(
				() -> transferenciaComContasPessoaFisicaService
						.efetuaTransferenciaEntreContasMesmoCliente(dadosParaTransferenciaDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);

		verify(contaCorrentePoupancaMsFeignClient, times(2))
				.buscaContaPessoaFisica(anyString(), any(TipoConta.class));
		
		verify(contaCorrentePoupancaMsFeignClient, times(2))
				.alteraContaPessoaFisica(any(ContaPessoaFisicaAlteracaoDto.class));
		
		verify(extratoBancarioMsFeignClient, times(2))
				.insereItemExtratoContaPessoaFisica(any(ItemExtratoContaPessoaFisicaInsercaoDto.class));
		
		verify(contaDigitalClienteMsFeignClient).buscaContaDigitalPessoaFisica(cpfCliente);
	}
	
	@DisplayName("Quando é efetuada uma transferência de conta poupança para conta corrente com sucesso,"
			+ " nenhuma exceção pode ser lançada")
	@Test
	void testEfetuaTranferenciaContaPoupancaParaCorrentePoupanca_ComSucesso_NenhumaExcecaoDeveSerLancada() {
		// Given
		String cpfCliente = CPF_CLIENTE_1;

		var contaCorrentePessoaFisica = new ContaPessoaFisicaBuscaDto1(1L, TipoConta.POUPANCA, 1000.0, cpfCliente);
		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaFisica(anyString(), any(TipoConta.class)))
				.willReturn(ResponseEntity.ok(contaCorrentePessoaFisica));
		
		var contaDigitalPessoaFisicaDTO1Busca = criaContaDigitalDonoContaQueDinheiroSai(cpfCliente);
		given(contaDigitalClienteMsFeignClient.buscaContaDigitalPessoaFisica(cpfCliente))
				.willReturn(ResponseEntity.ok(contaDigitalPessoaFisicaDTO1Busca));

		var dadosParaTransferenciaDto = new DadosParaTransferenciaEntreContasMesmoClienteDto(cpfCliente, 100.0,
				TipoConta.POUPANCA);

		// When & Then
		assertDoesNotThrow(
				() -> transferenciaComContasPessoaFisicaService
						.efetuaTransferenciaEntreContasMesmoCliente(dadosParaTransferenciaDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);

		verify(contaCorrentePoupancaMsFeignClient, times(2))
				.buscaContaPessoaFisica(anyString(), any(TipoConta.class));
		
		verify(contaCorrentePoupancaMsFeignClient, times(2))
				.alteraContaPessoaFisica(any(ContaPessoaFisicaAlteracaoDto.class));
		
		verify(extratoBancarioMsFeignClient, times(2))
				.insereItemExtratoContaPessoaFisica(any(ItemExtratoContaPessoaFisicaInsercaoDto.class));
		
		verify(contaDigitalClienteMsFeignClient).buscaContaDigitalPessoaFisica(cpfCliente);
	}
	
	@DisplayName("Quando é efetuada uma transferência entre contas de clientes diferentes "
			+ "desse banco (JBank) com sucesso, sendo a trasferência de uma conta corrente "
			+ "para uma outra conta corrente, nenhuma exceção pode ser lançada")
	@Test
	void testEfetuaTranferenciaEntreContasClientesDiferentesContaCorrenteParaContaCorrente_ComSucesso_NenhumaExcecaoDeveSerLancada() {
		// Given
		String cpfClienteDonoContaQueDinheiroSai = CPF_CLIENTE_1;
		String cpfClienteDonoContaQueDinheiroEntra = CPF_CLIENTE_2;
		
		var contaDigitalDonoContaQueDinheiroSai = criaContaDigitalDonoContaQueDinheiroSai(cpfClienteDonoContaQueDinheiroSai);
		given(contaDigitalClienteMsFeignClient.buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroSai))
				.willReturn(ResponseEntity.ok(contaDigitalDonoContaQueDinheiroSai));
		
		var contaDigitalDonoContaQueDinheiroEntra = criaContaDigitalDonoContaQueDinheiroEntra(cpfClienteDonoContaQueDinheiroEntra);
		given(contaDigitalClienteMsFeignClient.buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroEntra))
				.willReturn(ResponseEntity.ok(contaDigitalDonoContaQueDinheiroEntra));
		
		var contaCorrenteQueDinheiroSai = new ContaPessoaFisicaBuscaDto1(1L, TipoConta.CORRENTE, 1000.0, cpfClienteDonoContaQueDinheiroSai);
		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroSai, TipoConta.CORRENTE))
				.willReturn(ResponseEntity.ok(contaCorrenteQueDinheiroSai));

		var contaCorrenteQueDinheiroEntra = new ContaPessoaFisicaBuscaDto1(2L, TipoConta.CORRENTE, 1000.0, cpfClienteDonoContaQueDinheiroEntra);
		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroEntra, TipoConta.CORRENTE))
				.willReturn(ResponseEntity.ok(contaCorrenteQueDinheiroEntra));
		
		var dadosParaTransferenciaDto = new DadosParaTransferenciaEntreContasClientesDiferentesDto(
				cpfClienteDonoContaQueDinheiroSai, 100.0, TipoConta.CORRENTE, cpfClienteDonoContaQueDinheiroEntra);

		// When & Then
		assertDoesNotThrow(
				() -> transferenciaComContasPessoaFisicaService
						.efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(dadosParaTransferenciaDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);

		verify(contaCorrentePoupancaMsFeignClient).buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroSai, TipoConta.CORRENTE);
		verify(contaCorrentePoupancaMsFeignClient).buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroEntra, TipoConta.CORRENTE);
		
		
		verify(contaDigitalClienteMsFeignClient).buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroSai);
		verify(contaDigitalClienteMsFeignClient).buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroEntra);
		
		verify(contaCorrentePoupancaMsFeignClient, times(2))
				.alteraContaPessoaFisica(any(ContaPessoaFisicaAlteracaoDto.class));
		
		verify(extratoBancarioMsFeignClient, times(2))
				.insereItemExtratoContaPessoaFisica(any(ItemExtratoContaPessoaFisicaInsercaoDto.class));
	}

	@DisplayName("Quando é efetuada uma transferência entre contas de clientes diferentes desse "
			+ "banco (JBank) com sucesso, sendo a trasferência de uma conta poupança para uma "
			+ "conta corrente, nenhuma exceção pode ser lançada")
	@Test
	void testEfetuaTranferenciaEntreContasClientesDiferentesContaPoupancaParaContaCorrente_ComSucesso_NenhumaExcecaoDeveSerLancada() {
		// Given
		String cpfClienteDonoContaQueDinheiroSai = CPF_CLIENTE_1;
		String cpfClienteDonoContaQueDinheiroEntra = CPF_CLIENTE_2;
		
		var contaDigitalDonoContaQueDinheiroSai = criaContaDigitalDonoContaQueDinheiroSai(cpfClienteDonoContaQueDinheiroSai);
		given(contaDigitalClienteMsFeignClient.buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroSai))
				.willReturn(ResponseEntity.ok(contaDigitalDonoContaQueDinheiroSai));
		
		var contaDigitalDonoContaQueDinheiroEntra = criaContaDigitalDonoContaQueDinheiroEntra(cpfClienteDonoContaQueDinheiroEntra);
		given(contaDigitalClienteMsFeignClient.buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroEntra))
				.willReturn(ResponseEntity.ok(contaDigitalDonoContaQueDinheiroEntra));
		
		var contaPoupancaQueDinheiroSai = new ContaPessoaFisicaBuscaDto1(1L, TipoConta.POUPANCA, 1000.0, cpfClienteDonoContaQueDinheiroSai);
		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroSai, TipoConta.POUPANCA))
				.willReturn(ResponseEntity.ok(contaPoupancaQueDinheiroSai));

		var contaCorrenteQueDinheiroEntra = new ContaPessoaFisicaBuscaDto1(2L, TipoConta.CORRENTE, 1000.0, cpfClienteDonoContaQueDinheiroEntra);
		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroEntra, TipoConta.CORRENTE))
				.willReturn(ResponseEntity.ok(contaCorrenteQueDinheiroEntra));
		
		var dadosParaTransferenciaDto = new DadosParaTransferenciaEntreContasClientesDiferentesDto(
				cpfClienteDonoContaQueDinheiroSai, 100.0, TipoConta.POUPANCA, cpfClienteDonoContaQueDinheiroEntra);

		// When & Then
		assertDoesNotThrow(
				() -> transferenciaComContasPessoaFisicaService
						.efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(dadosParaTransferenciaDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);

		verify(contaCorrentePoupancaMsFeignClient).buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroSai, TipoConta.POUPANCA);
		verify(contaCorrentePoupancaMsFeignClient).buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroEntra, TipoConta.CORRENTE);
		
		
		verify(contaDigitalClienteMsFeignClient).buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroSai);
		verify(contaDigitalClienteMsFeignClient).buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroEntra);
		
		verify(contaCorrentePoupancaMsFeignClient, times(2))
				.alteraContaPessoaFisica(any(ContaPessoaFisicaAlteracaoDto.class));
		
		verify(extratoBancarioMsFeignClient, times(2))
				.insereItemExtratoContaPessoaFisica(any(ItemExtratoContaPessoaFisicaInsercaoDto.class));
	}
	
	@DisplayName("Quando é efetuada uma transferência entre contas de instituições financeiras diferentes com sucesso, "
			+ "sendo a trasferência a partir de uma conta corrente do JBank, nenhuma exceção pode ser lançada")
	@Test
	void testEfetuaTranferenciaEntreContasInstituicoesFinanceirasDiferentesAPartirContaCorrente_ComSucesso_NenhumaExcecaoDeveSerLancada() {
		// Given
		String cpfClienteDonoContaQueDinheiroSai = CPF_CLIENTE_1;
		
		var contaDigitalDonoContaQueDinheiroSai = criaContaDigitalDonoContaQueDinheiroSai(cpfClienteDonoContaQueDinheiroSai);
		given(contaDigitalClienteMsFeignClient.buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroSai))
				.willReturn(ResponseEntity.ok(contaDigitalDonoContaQueDinheiroSai));
		
		var contaCorrenteQueDinheiroSai = new ContaPessoaFisicaBuscaDto1(1L, TipoConta.CORRENTE, 1000.0, cpfClienteDonoContaQueDinheiroSai);
		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroSai, TipoConta.CORRENTE))
				.willReturn(ResponseEntity.ok(contaCorrenteQueDinheiroSai));
		
		var valorTransferencia = 100.0;
		var tipoContaOrigemDinheiro = TipoConta.CORRENTE;
		var bancoDestino = Banco.BANCO_BRASIL;
		String agenciaDestino = "0000000011", contaDestino = "0000000001";
		var dadosParaTransferenciaDto = new DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto(
				cpfClienteDonoContaQueDinheiroSai, valorTransferencia, tipoContaOrigemDinheiro, bancoDestino,
				agenciaDestino, contaDestino);
		
		// When & Then
		assertDoesNotThrow(
				() -> transferenciaComContasPessoaFisicaService
						.efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(dadosParaTransferenciaDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		verify(contaDigitalClienteMsFeignClient).buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroSai);
		
		verify(contaCorrentePoupancaMsFeignClient).buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroSai, TipoConta.CORRENTE);
		
		verify(contaCorrentePoupancaMsFeignClient)
				.alteraContaPessoaFisica(any(ContaPessoaFisicaAlteracaoDto.class));
		
		verify(extratoBancarioMsFeignClient)
				.insereItemExtratoContaPessoaFisica(any(ItemExtratoContaPessoaFisicaInsercaoDto.class));
	}
	
	@DisplayName("Quando é efetuada uma transferência entre contas de instituições financeiras diferentes com sucesso, "
			+ "sendo a trasferência a partir de uma conta poupança do JBank, nenhuma exceção pode ser lançada")
	@Test
	void testEfetuaTranferenciaEntreContasInstituicoesFinanceirasDiferentesAPartirContaPoupanca_ComSucesso_NenhumaExcecaoDeveSerLancada() {
		// Given
		String cpfClienteDonoContaQueDinheiroSai = CPF_CLIENTE_1;
		
		var contaDigitalDonoContaQueDinheiroSai = criaContaDigitalDonoContaQueDinheiroSai(cpfClienteDonoContaQueDinheiroSai);
		given(contaDigitalClienteMsFeignClient.buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroSai))
				.willReturn(ResponseEntity.ok(contaDigitalDonoContaQueDinheiroSai));
		
		var contaPoupancaQueDinheiroSai = new ContaPessoaFisicaBuscaDto1(1L, TipoConta.POUPANCA, 1000.0, cpfClienteDonoContaQueDinheiroSai);
		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroSai, TipoConta.POUPANCA))
				.willReturn(ResponseEntity.ok(contaPoupancaQueDinheiroSai));
		
		var valorTransferencia = 100.0;
		var tipoContaOrigemDinheiro = TipoConta.POUPANCA;
		var bancoDestino = Banco.BANCO_BRASIL;
		String agenciaDestino = "0000000011", contaDestino = "0000000001";
		var dadosParaTransferenciaDto = new DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto(
				cpfClienteDonoContaQueDinheiroSai, valorTransferencia, tipoContaOrigemDinheiro, bancoDestino,
				agenciaDestino, contaDestino);
		
		// When & Then
		assertDoesNotThrow(
				() -> transferenciaComContasPessoaFisicaService
						.efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(dadosParaTransferenciaDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		verify(contaDigitalClienteMsFeignClient).buscaContaDigitalPessoaFisica(cpfClienteDonoContaQueDinheiroSai);
		
		verify(contaCorrentePoupancaMsFeignClient).buscaContaPessoaFisica(cpfClienteDonoContaQueDinheiroSai, TipoConta.POUPANCA);
		
		verify(contaCorrentePoupancaMsFeignClient)
				.alteraContaPessoaFisica(any(ContaPessoaFisicaAlteracaoDto.class));
		
		verify(extratoBancarioMsFeignClient)
				.insereItemExtratoContaPessoaFisica(any(ItemExtratoContaPessoaFisicaInsercaoDto.class));
	}
	
	@DisplayName("Quando é recebido dinheiro vindo de outra instituição financeira com sucesso, "
			+ "nenhuma exceção pode ser lançada")
	@Test
	void testRecebeDinheiroVindoOutraInstituicaoFinanceira_ComSucesso_NenhumaExcecaoDeveSerLancada() {
		// Given
		String cpfClienteDonoContaQueDinheiroEntra = CPF_CLIENTE_1;
		
		var contaDigitalDonoContaQueDinheiroEntra = criaContaDigitalDonoContaQueDinheiroEntra(cpfClienteDonoContaQueDinheiroEntra);
		String agenciaDestinoDinheiro = contaDigitalDonoContaQueDinheiroEntra.getAgencia();
		String contaDestinoDinheiro = contaDigitalDonoContaQueDinheiroEntra.getConta();
		String cpfContaDigitalDonoContaQueDinheiroEntra = contaDigitalDonoContaQueDinheiroEntra.getCpf();
		
		given(contaDigitalClienteMsFeignClient.buscaContaDigitalPessoaFisica(agenciaDestinoDinheiro,
				contaDestinoDinheiro)).willReturn(ResponseEntity.ok(contaDigitalDonoContaQueDinheiroEntra));
		
		var contaCorrenteQueDinheiroEntra = new ContaPessoaFisicaBuscaDto1(1L, TipoConta.CORRENTE, 1000.0,
				cpfClienteDonoContaQueDinheiroEntra);
		given(contaCorrentePoupancaMsFeignClient.buscaContaPessoaFisica(contaDigitalDonoContaQueDinheiroEntra.getCpf(),
				TipoConta.CORRENTE)).willReturn(ResponseEntity.ok(contaCorrenteQueDinheiroEntra));
		
		var bancoOrigemDinheiro = Banco.BANCO_BRASIL;
		String agenciaOrigemDinheiro = "0000000011", contaOrigemDinheiro = "0000000001";
		var valorTransferencia = 100.0;
		var dadosParaTransferenciaDto = new DadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceira(
				bancoOrigemDinheiro, agenciaOrigemDinheiro, contaOrigemDinheiro, valorTransferencia,
				agenciaDestinoDinheiro, contaDestinoDinheiro);
		
		// When & Then
		assertDoesNotThrow(
				() -> transferenciaComContasPessoaFisicaService
						.recebeDinheiroVindoOutraInstituicaoFinanceira(dadosParaTransferenciaDto),
				() -> NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO);
		
		verify(contaDigitalClienteMsFeignClient).buscaContaDigitalPessoaFisica(agenciaDestinoDinheiro,
				contaDestinoDinheiro);
		
		verify(contaCorrentePoupancaMsFeignClient).buscaContaPessoaFisica(cpfContaDigitalDonoContaQueDinheiroEntra,
				TipoConta.CORRENTE);
		
		verify(contaCorrentePoupancaMsFeignClient)
				.alteraContaPessoaFisica(any(ContaPessoaFisicaAlteracaoDto.class));
		
		verify(extratoBancarioMsFeignClient)
				.insereItemExtratoContaPessoaFisica(any(ItemExtratoContaPessoaFisicaInsercaoDto.class));
	}
	
	/**
	 * Cria a conta digital do dono da conta que dinheiro sai.
	 * 
	 * @param cpfClienteDonoContaQueDinheiroSai
	 * @return a conta digital do dono da conta que dinheiro sai.
	 */
	private ContaDigitalPessoaFisicaDTO1Busca criaContaDigitalDonoContaQueDinheiroSai(
			String cpfClienteDonoContaQueDinheiroSai) {
		return new ContaDigitalPessoaFisicaDTO1Busca(
				"0000000011", "0000000001", 2, "19980001234", "fulano@email.com", 1L, 
				null, null, cpfClienteDonoContaQueDinheiroSai, "Fulano de Tal",
				null, "Fulana de Tal");
	}
	
	/**
	 * Cria a conta digital do dono da conta que dinheiro entra.
	 * 
	 * @param cpfClienteDonoContaQueDinheiroSai
	 * @return a conta digital do dono da conta que dinheiro sai.
	 */
	private ContaDigitalPessoaFisicaDTO1Busca criaContaDigitalDonoContaQueDinheiroEntra(
			String cpfClienteDonoContaQueDinheiroEntra) {
		return new ContaDigitalPessoaFisicaDTO1Busca(
				"0000000011", "0000000002", 3, "19980001235", "fulano2@email.com", 1L, 
				null, null, cpfClienteDonoContaQueDinheiroEntra, "Fulano de Tal 2",
				null, "Fulana de Tal 2");
	}
}
