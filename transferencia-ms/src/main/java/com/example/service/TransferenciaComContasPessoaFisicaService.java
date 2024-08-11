package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.DadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceira;
import com.example.dto.DadosParaTransferenciaEntreContasClientesDiferentesDto;
import com.example.dto.DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto;
import com.example.dto.DadosParaTransferenciaEntreContasMesmoClienteDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.openfeign.feignclient.ContaCorrentePoupancaMsFeignClient;
import com.example.openfeign.feignclient.ContaDigitalClienteMsFeignClient;
import com.example.openfeign.feignclient.ExtratoBancarioMsFeignClient;

@Service
public class TransferenciaComContasPessoaFisicaService extends TransferenciaComContasService {
	@Autowired
	private ExtratoBancarioMsFeignClient extratoBancarioMsFeignClient;

	@Autowired
	private ContaCorrentePoupancaMsFeignClient contaCorrentePoupancaMsFeignClient;

	@Autowired
	private ContaDigitalClienteMsFeignClient contaDigitalClienteMsFeignClient;

	/**
	 * Efetua transferência entre contas do mesmo cliente, ou seja, de conta
	 * corrente para conta poupança ou de conta poupança para conta corrente, sendo
	 * essa contas do mesmo cliente.
	 * 
	 * @param dadosParaTransferenciaEntreContasMesmoClienteDto Os dados para a transferência.
	 */
	public void efetuaTransferenciaEntreContasMesmoCliente(
			DadosParaTransferenciaEntreContasMesmoClienteDto dadosParaTransferenciaEntreContasMesmoClienteDto) {
		TipoConta tipoContaOrigemDinheiro = dadosParaTransferenciaEntreContasMesmoClienteDto.tipoContaOrigemDinheiro();
		double valorTransferencia = dadosParaTransferenciaEntreContasMesmoClienteDto.valorTransferencia();
		String cpfCliente = dadosParaTransferenciaEntreContasMesmoClienteDto.cpfCliente();

		super.validaContaOrigemDinheiro(tipoContaOrigemDinheiro);
		super.validaValorTransferencia(valorTransferencia);
		super.validaCpf(cpfCliente);

		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica = buscaContaDigitalClientePeloCpf(cpfCliente);
		
		validaSeEncontradaContaDigitalClienteQueQuerTransferirDinheiro(contaDigitalPessoaFisica);

		if (tipoContaOrigemDinheiro == TipoConta.CORRENTE) {
			transfereDinheiroDaContaCorrenteParaContaPoupanca(valorTransferencia, contaDigitalPessoaFisica);
		} else {
			transfereDinheiroDaContaPoupancaParaContaCorrente(valorTransferencia, contaDigitalPessoaFisica);
		}
	}

	/**
	 * Transfere dinheiro da conta poupança para a conta corrente.
	 * 
	 * @param valorTransferencia
	 * @param contaDigitalPessoaFisica A conta digital do cliente.
	 */
	private void transfereDinheiroDaContaPoupancaParaContaCorrente(double valorTransferencia,
			ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica) {
		atualizaSaldoContaPoupancaPessoaFisica(- valorTransferencia, contaDigitalPessoaFisica.getCpf());
		insereItemExtratoContaPessoaFisica(TipoConta.POUPANCA,
				Operacao.TRANSFERENCIA_PARA_MESMA_INSTITUICAO_FINANCEIRA,
				Operacao.TRANSFERENCIA_PARA_MESMA_INSTITUICAO_FINANCEIRA.getDescricao(), 
				- valorTransferencia,
				ESSE_MESMO_BANCO, 
				contaDigitalPessoaFisica.getAgencia(), 
				contaDigitalPessoaFisica.getConta(),
				contaDigitalPessoaFisica.getCpf());

		atualizaSaldoContaCorrentePessoaFisica(valorTransferencia, contaDigitalPessoaFisica.getCpf());
		insereItemExtratoContaPessoaFisica(TipoConta.CORRENTE,
				Operacao.DEPOSITO,
				Operacao.DEPOSITO.getDescricao(), 
				valorTransferencia,
				ESSE_MESMO_BANCO, 
				contaDigitalPessoaFisica.getAgencia(), 
				contaDigitalPessoaFisica.getConta(),
				contaDigitalPessoaFisica.getCpf());
	}

	/**
	 * Transfere dinheiro da conta corrente para a conta poupança.
	 * 
	 * @param valorTransferencia
	 * @param contaDigitalPessoaFisica A conta digital do cliente.
	 */
	private void transfereDinheiroDaContaCorrenteParaContaPoupanca(double valorTransferencia,
			ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica) {
		atualizaSaldoContaCorrentePessoaFisica(- valorTransferencia, contaDigitalPessoaFisica.getCpf());
		insereItemExtratoContaPessoaFisica(TipoConta.CORRENTE,
				Operacao.TRANSFERENCIA_PARA_MESMA_INSTITUICAO_FINANCEIRA,
				Operacao.TRANSFERENCIA_PARA_MESMA_INSTITUICAO_FINANCEIRA.getDescricao(), 
				- valorTransferencia,
				ESSE_MESMO_BANCO, 
				contaDigitalPessoaFisica.getAgencia(), 
				contaDigitalPessoaFisica.getConta(),
				contaDigitalPessoaFisica.getCpf());

		atualizaSaldoContaPoupancaPessoaFisica(valorTransferencia, contaDigitalPessoaFisica.getCpf());
		insereItemExtratoContaPessoaFisica(TipoConta.POUPANCA,
				Operacao.DEPOSITO,
				Operacao.DEPOSITO.getDescricao(),
				valorTransferencia,
				ESSE_MESMO_BANCO, 
				contaDigitalPessoaFisica.getAgencia(), 
				contaDigitalPessoaFisica.getConta(),
				contaDigitalPessoaFisica.getCpf());
	}

	/**
	 * Efetua transferência entre contas de clientes diferentes desse banco (JBank), ou
	 * seja, da conta corrente de um determinado cliente para a conta corrente de
	 * outro determinado cliente ou da conta poupança de um determinado cliente para
	 * a conta corrente de outro determinado cliente (a conta de destino do dinheiro
	 * sempre será uma conta corrente).
	 * 
	 * @param dadosParaTransferenciaEntreContasMesmoUsuarioDto Os dados para a transferência.
	 */
	public void efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(
			DadosParaTransferenciaEntreContasClientesDiferentesDto dadosParaTransferenciaEntreContasMesmoUsuarioDto) {
		String cpfClienteDonoContaQueDinheiroSai = dadosParaTransferenciaEntreContasMesmoUsuarioDto
				.cpfClienteDonoContaQueDinheiroSai();
		TipoConta tipoContaOrigemDinheiro = dadosParaTransferenciaEntreContasMesmoUsuarioDto.tipoContaOrigemDinheiro();
		double valorTransferencia = dadosParaTransferenciaEntreContasMesmoUsuarioDto.valorTransferencia();
		String cpfClienteDonoContaQueDinheiroEntra = dadosParaTransferenciaEntreContasMesmoUsuarioDto
				.cpfClienteDonoContaQueDinheiroEntra();

		super.validaCpf(cpfClienteDonoContaQueDinheiroSai);
		super.validaValorTransferencia(valorTransferencia);
		super.validaContaOrigemDinheiro(tipoContaOrigemDinheiro);
		super.validaCpf(cpfClienteDonoContaQueDinheiroEntra);

		ContaDigitalPessoaFisicaDTO1Busca contaDigitalClienteDonoContaQueDinheiroSai = buscaContaDigitalClientePeloCpf(
				cpfClienteDonoContaQueDinheiroSai);
		
		validaSeEncontradaContaDigitalClienteQueQuerTransferirDinheiro(contaDigitalClienteDonoContaQueDinheiroSai);
		
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalClienteDonoContaQueDinheiroEntra = buscaContaDigitalClientePeloCpf(
				cpfClienteDonoContaQueDinheiroEntra);
		
		validaSeEncontradaContaDigitalClienteQueDeveReceberDinheiro(contaDigitalClienteDonoContaQueDinheiroEntra);

		transfereDinheiroParaContaCorrenteOutroCliente(tipoContaOrigemDinheiro, valorTransferencia,
				contaDigitalClienteDonoContaQueDinheiroSai, contaDigitalClienteDonoContaQueDinheiroEntra);
	}

	/**
	 * Transfere dinheiro para conta corrente de outro cliente.
	 * 
	 * @param tipoContaOrigemDinheiro
	 * @param valorTransferencia
	 * @param contaDigitalClienteDonoContaQueDinheiroSai
	 * @param contaDigitalClienteDonoContaQueDinheiroEntra
	 */
	private void transfereDinheiroParaContaCorrenteOutroCliente(TipoConta tipoContaOrigemDinheiro,
			double valorTransferencia, ContaDigitalPessoaFisicaDTO1Busca contaDigitalClienteDonoContaQueDinheiroSai,
			ContaDigitalPessoaFisicaDTO1Busca contaDigitalClienteDonoContaQueDinheiroEntra) {
		String cpfDonoContaDigitalClienteDonoContaQueDinheiroSai = contaDigitalClienteDonoContaQueDinheiroSai.getCpf();
		
		if (tipoContaOrigemDinheiro == TipoConta.CORRENTE) {
			atualizaSaldoContaCorrentePessoaFisica(- valorTransferencia, cpfDonoContaDigitalClienteDonoContaQueDinheiroSai);
			insereItemExtratoContaPessoaFisica(TipoConta.CORRENTE,
					Operacao.TRANSFERENCIA_PARA_MESMA_INSTITUICAO_FINANCEIRA,
					Operacao.TRANSFERENCIA_PARA_MESMA_INSTITUICAO_FINANCEIRA.getDescricao(),
					- valorTransferencia,
					ESSE_MESMO_BANCO, 
					contaDigitalClienteDonoContaQueDinheiroEntra.getAgencia(), 
					contaDigitalClienteDonoContaQueDinheiroEntra.getConta(),
					cpfDonoContaDigitalClienteDonoContaQueDinheiroSai);
		} else {
			atualizaSaldoContaPoupancaPessoaFisica(- valorTransferencia, cpfDonoContaDigitalClienteDonoContaQueDinheiroSai);
			insereItemExtratoContaPessoaFisica(TipoConta.POUPANCA,
					Operacao.TRANSFERENCIA_PARA_MESMA_INSTITUICAO_FINANCEIRA,
					Operacao.TRANSFERENCIA_PARA_MESMA_INSTITUICAO_FINANCEIRA.getDescricao(),
					-valorTransferencia,
					ESSE_MESMO_BANCO,
					contaDigitalClienteDonoContaQueDinheiroEntra.getAgencia(),
					contaDigitalClienteDonoContaQueDinheiroEntra.getConta(),
					cpfDonoContaDigitalClienteDonoContaQueDinheiroSai);
		}

		String cpfDonoContaDigitalClienteDonoContaQueDinheiroEntra = contaDigitalClienteDonoContaQueDinheiroEntra.getCpf();
		atualizaSaldoContaCorrentePessoaFisica(valorTransferencia, cpfDonoContaDigitalClienteDonoContaQueDinheiroEntra);
		insereItemExtratoContaPessoaFisica(TipoConta.CORRENTE,
				Operacao.DEPOSITO,
				Operacao.DEPOSITO.getDescricao() + ". Dinheiro vindo de: "
						+ ESSE_MESMO_BANCO.getRazaoSocial() + ", agência "
						+ contaDigitalClienteDonoContaQueDinheiroSai.getAgencia() + " e conta: "
						+ contaDigitalClienteDonoContaQueDinheiroSai.getConta(), 
				valorTransferencia,
				ESSE_MESMO_BANCO, 
				contaDigitalClienteDonoContaQueDinheiroEntra.getAgencia(), 
				contaDigitalClienteDonoContaQueDinheiroEntra.getConta(),
				cpfDonoContaDigitalClienteDonoContaQueDinheiroEntra);
	}

	/**
	 * Efetua transferência entre contas de instituições financeiras diferentes, ou
	 * seja, da conta corrente de um determinado cliente dessa instituição
	 * financeira para a conta de determinado cliente de outra instituição
	 * financeira ou da conta poupança de um determinado cliente dessa instituição
	 * financeira para a conta de determinado cliente de outra instituição
	 * financeira.
	 * 
	 * @param dadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto Os dados para a transferência.
	 */
	public void efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
			DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto dadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto) {
		String cpfClienteDonoContaQueDinheiroSai = dadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto
				.cpfClienteDonoContaQueDinheiroSai();
		TipoConta contaOrigemDinheiro = dadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto
				.tipoContaOrigemDinheiro();
		double valorTransferencia = dadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto
				.valorTransferencia();
		Banco bancoDestino = dadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto
				.bancoDestino();
		String agenciaDestino = dadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto
				.agenciaDestino();
		String contaDestino = dadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto
				.contaDestino();

		super.validaCpf(cpfClienteDonoContaQueDinheiroSai);
		super.validaValorTransferencia(valorTransferencia);
		super.validaContaOrigemDinheiro(contaOrigemDinheiro);
		super.validaBancoDestino(bancoDestino);
		super.validaAgenciaDestino(agenciaDestino);
		super.validaContaDestino(contaDestino);

		ContaDigitalPessoaFisicaDTO1Busca contaDigitalClienteDonoContaQueDinheiroSai = buscaContaDigitalClientePeloCpf(
				cpfClienteDonoContaQueDinheiroSai);
		
		validaSeEncontradaContaDigitalClienteQueQuerTransferirDinheiro(contaDigitalClienteDonoContaQueDinheiroSai);

		if (contaOrigemDinheiro == TipoConta.CORRENTE) {
			atualizaSaldoContaCorrentePessoaFisica(- valorTransferencia, contaDigitalClienteDonoContaQueDinheiroSai.getCpf());
		} else {
			atualizaSaldoContaPoupancaPessoaFisica(- valorTransferencia, contaDigitalClienteDonoContaQueDinheiroSai.getCpf());
		}

		if (bancoDestino == Banco.BANCO_BRASIL) {
			// Aqui seria efetuada a transferência para o Banco do Brasil
		} else if (bancoDestino == Banco.BRADESCO) {
			// Aqui seria efetuada a transferência para o banco Bradesco
		} else if (bancoDestino == Banco.CAIXA) {
			// Aqui seria efetuada a transferência para o banco Caixa
		} else if (bancoDestino == Banco.ITAU) {
			// Aqui seria efetuada a transferência para o banco Itaú
		} else if (bancoDestino == Banco.SANTANDER) {
			// Aqui seria efetuada a transferência para o Banco Santander
		} else if (bancoDestino == Banco.NUBANK) {
			// Aqui seria efetuada a transferência para o banco Nubank
		} else if (bancoDestino == Banco.INTER) {
			// Aqui seria efetuada a transferência para o banco Inter
		} else if (bancoDestino == Banco.JBANK) {
			throw new ValidacaoException("A opção selecionada de banco de destino é inválida. "
					+ "Para transferência entre contas bancárias da JBANK verifique os outros "
					+ "modos de trasferência.", HttpStatus.BAD_REQUEST);
		} else {
			throw new ValidacaoException("Ainda não é possível realizar uma transferência de dinheiro"
					+ " a partir do JBANK para a instituição financeira informada. Contate o suporte"
					+ " para mais informações.", HttpStatus.BAD_REQUEST);
		}
		
		insereItemExtratoContaPessoaFisica(contaOrigemDinheiro,
				Operacao.TRANSFERENCIA_PARA_OUTRA_INSTITUICAO_FINANCEIRA,
				Operacao.TRANSFERENCIA_PARA_OUTRA_INSTITUICAO_FINANCEIRA.getDescricao(), 
				valorTransferencia,
				bancoDestino, 
				agenciaDestino, 
				contaDestino, 
				cpfClienteDonoContaQueDinheiroSai);
	}

	/**
	 * Valida se encontrada a conta digital do cliente que quer transferir dinheiro.
	 * 
	 * @param contaDigitalClienteQueQuerTransferirDinheiro
	 */
	private void validaSeEncontradaContaDigitalClienteQueQuerTransferirDinheiro(
			ContaDigitalPessoaFisicaDTO1Busca contaDigitalClienteQueQuerTransferirDinheiro) {
		if (contaDigitalClienteQueQuerTransferirDinheiro == null) {
			throw new ValidacaoException(
					"Não foi encontrada a conta digital do cliente que quer transferir o dinheiro.",
					HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Valida se foi encontrada a conta digital do cliente que deve receber o dinheiro.
	 * 
	 * @param contaDigitalClienteQueDeveReceberDinheiro
	 */
	private void validaSeEncontradaContaDigitalClienteQueDeveReceberDinheiro(
			ContaDigitalPessoaFisicaDTO1Busca contaDigitalClienteQueDeveReceberDinheiro) {
		if (contaDigitalClienteQueDeveReceberDinheiro == null) {
			throw new ValidacaoException(
					"Não foi encontrada a conta digital do cliente que deve receber o dinheiro.",
					HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Recebe dinheiro vindo de outra instituição financeira.
	 * 
	 * @param dadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceiraDto Os dados da transferência recebida.
	 */
	public void recebeDinheiroVindoOutraInstituicaoFinanceira(
			DadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceira dadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceiraDto) {
		Banco bancoOrigemDiheiro = dadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceiraDto.bancoOrigemDinheiro();
		String agenciaOrigemDiheiro = dadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceiraDto.agenciaOrigemDinheiro();
		String contaOrigemDiheiro = dadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceiraDto.contaOrigemDinheiro();
		double valorTransferencia = dadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceiraDto.valorTransferencia();
		String agenciaDestinoDiheiro = dadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceiraDto.agenciaDestinoDinheiro();
		String contaDestinoDiheiro = dadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceiraDto.contaDestinoDinheiro();

		super.validaBancoOrigem(bancoOrigemDiheiro);
		super.validaAgenciaOrigem(agenciaOrigemDiheiro);
		super.validaContaOrigem(contaOrigemDiheiro);
		super.validaValorTransferencia(valorTransferencia);
		super.validaAgenciaDestino(agenciaDestinoDiheiro);
		super.validaContaDestino(contaDestinoDiheiro);

		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica = buscaContaDigitalClientePelaAgenciaConta(
				agenciaDestinoDiheiro, contaDestinoDiheiro);

		String cpfClienteDonoContaDigital = contaDigitalPessoaFisica.getCpf();
		
		atualizaSaldoContaCorrentePessoaFisica(valorTransferencia, cpfClienteDonoContaDigital);
		insereItemExtratoContaPessoaFisica(TipoConta.CORRENTE,
				Operacao.TRANSFERENCIA_VINDA_DE_OUTRA_INSTITUICAO_FINANCEIRA,
				Operacao.TRANSFERENCIA_VINDA_DE_OUTRA_INSTITUICAO_FINANCEIRA.getDescricao() + ". Dinheiro vindo de: "
						+ bancoOrigemDiheiro.getRazaoSocial() + ", agência "
						+ agenciaOrigemDiheiro + " e conta: "
						+ contaOrigemDiheiro,
				valorTransferencia, 
				ESSE_MESMO_BANCO, 
				contaDigitalPessoaFisica.getAgencia(),
				contaDigitalPessoaFisica.getConta(), 
				cpfClienteDonoContaDigital);
	}

	/**
	 * Atualiza o saldo de uma conta corrente de pessoa física.
	 * 
	 * @param valor
	 * @param cpfCliente
	 */
	private void atualizaSaldoContaCorrentePessoaFisica(double valor, String cpfCliente) {
		ContaPessoaFisicaBuscaDto1 contaCorrentePessoaFisica = buscaContaCorrentePessoaFisica(cpfCliente);

		double novoSaldoContaCorrente = contaCorrentePessoaFisica.getSaldo() + valor;

		contaCorrentePoupancaMsFeignClient.alteraContaPessoaFisica(
				new ContaPessoaFisicaAlteracaoDto(contaCorrentePessoaFisica.getId(), novoSaldoContaCorrente));
	}

	/**
	 * Atualiza o saldo de uma conta poupança de pessoa física.
	 * 
	 * @param valor
	 * @param cpfCliente
	 */
	private void atualizaSaldoContaPoupancaPessoaFisica(double valor, String cpfCliente) {
		ContaPessoaFisicaBuscaDto1 contaPoupancaPessoaFisica = buscaContaPoupancaPessoaFisica(cpfCliente);

		double novoSaldoContaCorrente = contaPoupancaPessoaFisica.getSaldo() + valor;

		contaCorrentePoupancaMsFeignClient.alteraContaPessoaFisica(
				new ContaPessoaFisicaAlteracaoDto(contaPoupancaPessoaFisica.getId(), novoSaldoContaCorrente));
	}

	/**
	 * Busca uma conta corrente de pessoa física.
	 * 
	 * @param cpfCliente O CPF do cliente
	 * @return a conta corrente de pessoa física buscada.
	 */
	private ContaPessoaFisicaBuscaDto1 buscaContaCorrentePessoaFisica(String cpfCliente) {
		ResponseEntity<ContaPessoaFisicaBuscaDto1> respostaBuscaContaPessoaFisica = contaCorrentePoupancaMsFeignClient
				.buscaContaPessoaFisica(cpfCliente, TipoConta.CORRENTE);

		ContaPessoaFisicaBuscaDto1 contaCorrentePessoaFisica = respostaBuscaContaPessoaFisica.getBody();
		return contaCorrentePessoaFisica;
	}

	/**
	 * Busca uma conta poupança de pessoa física.
	 * 
	 * @param cpfCliente O CPF do cliente
	 * @return a conta poupança de pessoa física buscada.
	 */
	private ContaPessoaFisicaBuscaDto1 buscaContaPoupancaPessoaFisica(String cpfCliente) {
		ResponseEntity<ContaPessoaFisicaBuscaDto1> respostaBuscaContaPessoaFisica = contaCorrentePoupancaMsFeignClient
				.buscaContaPessoaFisica(cpfCliente, TipoConta.POUPANCA);

		ContaPessoaFisicaBuscaDto1 contaPoupancaPessoaFisica = respostaBuscaContaPessoaFisica.getBody();
		return contaPoupancaPessoaFisica;
	}

	/**
	 * Insere novo item de extrato para uma conta corrente de pessoa física.
	 * 
	 * @param tipoConta
	 * @param valor
	 * @param cpfCliente
	 */
	private void insereItemExtratoContaPessoaFisica(TipoConta tipoConta, Operacao operacao, String descricaoOperacao,
			double valor, Banco bancoDestino, String agenciaDestino, String contaDestino, String cpfCliente) {

		var novoItemExtratoContaCorrentePessoaFisicaInsercaoDto = new ItemExtratoContaPessoaFisicaInsercaoDto(tipoConta,
				operacao, descricaoOperacao, bancoDestino, agenciaDestino, contaDestino, valor, cpfCliente);

		extratoBancarioMsFeignClient
				.insereItemExtratoContaPessoaFisica(novoItemExtratoContaCorrentePessoaFisicaInsercaoDto);
	}

	/**
	 * Busca conta digital de cliente pelo CPF.
	 * 
	 * @param cpfCliente
	 * @return a conta digital de cliente.
	 */
	private ContaDigitalPessoaFisicaDTO1Busca buscaContaDigitalClientePeloCpf(String cpfCliente) {
		ResponseEntity<ContaDigitalPessoaFisicaDTO1Busca> respostaBuscaContaDigitalPessoaFisica = contaDigitalClienteMsFeignClient
				.buscaContaDigitalPessoaFisica(cpfCliente);

		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica = respostaBuscaContaDigitalPessoaFisica.getBody();
		return contaDigitalPessoaFisica;
	}
	
	/**
	 * Busca conta digital de cliente pela agência de conta.
	 * 
	 * @param agencia
	 * @param conta
	 * @return a conta digital de cliente.
	 */
	private ContaDigitalPessoaFisicaDTO1Busca buscaContaDigitalClientePelaAgenciaConta(String agencia, String conta) {
		ResponseEntity<ContaDigitalPessoaFisicaDTO1Busca> respostaBuscaContaDigitalPessoaFisica = contaDigitalClienteMsFeignClient
				.buscaContaDigitalPessoaFisica(agencia, conta);

		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica = respostaBuscaContaDigitalPessoaFisica.getBody();
		return contaDigitalPessoaFisica;
	}

}
