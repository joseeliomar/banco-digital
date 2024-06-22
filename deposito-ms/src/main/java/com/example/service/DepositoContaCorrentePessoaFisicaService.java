package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.DadosParaDepositoContaPessoaFisicaDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.openfeign.feignclient.ContaCorrentePoupancaMsFeignClient;
import com.example.openfeign.feignclient.ContaDigitalClienteMsFeignClient;
import com.example.openfeign.feignclient.ExtratoBancarioMsFeignClient;

@Service
public class DepositoContaCorrentePessoaFisicaService extends DepositoContaCorrenteService {
	@Autowired
	private ExtratoBancarioMsFeignClient extratoBancarioMsFeignClient;

	@Autowired
	private ContaCorrentePoupancaMsFeignClient contaPessoaFisicaFeignClient;
	
	@Autowired
	private ContaDigitalClienteMsFeignClient contaDigitalClienteMsFeignClient;

	/**
	 * Efetua depósito em uma conta corrente de pessoa física.
	 * 
	 * @param dadosParaDepositoDto Os dados para o depósito
	 */
	public void efetuaDepositoContaCorrentePessoaFisica(DadosParaDepositoContaPessoaFisicaDto dadosParaDepositoDto) {
		super.validaValorDeposito(dadosParaDepositoDto.valorDeposito());
		
		atualizaSaldoContaCorrentePessoaFisica(dadosParaDepositoDto);
		insereItemExtratoContaCorrentePessoaFisica(dadosParaDepositoDto);
	}

	/**
	 * Atualiza o saldo de uma conta corrente de pessoa física.
	 * 
	 * @param dadosParaDepositoDto Os dados para o depósito
	 */
	private void atualizaSaldoContaCorrentePessoaFisica(
			DadosParaDepositoContaPessoaFisicaDto dadosParaDepositoDto) {
		ContaPessoaFisicaBuscaDto1 contaCorrentePessoaFisica = buscaContaCorrentePessoaFisica(dadosParaDepositoDto.cpfCliente());
		
		double novoSaldoContaCorrente = contaCorrentePessoaFisica.getSaldo() + dadosParaDepositoDto.valorDeposito();
		
		contaPessoaFisicaFeignClient.alteraContaPessoaFisica(
				new ContaPessoaFisicaAlteracaoDto(contaCorrentePessoaFisica.getId(), novoSaldoContaCorrente));
	}

	/**
	 * Busca uma conta corrente de pessoa física.
	 * 
	 * @param cpfCliente O CPF do cliente
	 * @return a conta corrente de pessoa física buscada.
	 */
	private ContaPessoaFisicaBuscaDto1 buscaContaCorrentePessoaFisica(String cpfCliente) {
		ResponseEntity<ContaPessoaFisicaBuscaDto1> respostaBuscaContaPessoaFisica = contaPessoaFisicaFeignClient
				.buscaContaPessoaFisica(cpfCliente, TipoConta.CORRENTE);
		
		ContaPessoaFisicaBuscaDto1 contaCorrentePessoaFisica = respostaBuscaContaPessoaFisica.getBody();
		return contaCorrentePessoaFisica;
	}

	/**
	 * Insere novo item de extrato para uma conta corrente de pessoa física.
	 * 
	 * @param dadosParaDepositoDto Os dados para o depósito
	 */
	private void insereItemExtratoContaCorrentePessoaFisica(DadosParaDepositoContaPessoaFisicaDto dadosParaDepositoDto) {
		String cpfCliente = dadosParaDepositoDto.cpfCliente();
		
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica = buscaContaDigitalCliente(cpfCliente);
		
		var novoItemExtratoContaCorrentePessoaFisicaInsercaoDto = new ItemExtratoContaPessoaFisicaInsercaoDto(
				TipoConta.CORRENTE,
				Operacao.DEPOSITO,
				Operacao.DEPOSITO.getDescricao(),
				ESSE_MESMO_BANCO,
				contaDigitalPessoaFisica.getAgencia(),
				contaDigitalPessoaFisica.getConta(),
				dadosParaDepositoDto.valorDeposito(),
				cpfCliente);
		
		extratoBancarioMsFeignClient
				.insereItemExtratoContaPessoaFisica(novoItemExtratoContaCorrentePessoaFisicaInsercaoDto);
	}

	/**
	 * Busca conta digital de cliente.
	 * 
	 * @param cpfCliente
	 * @return a conta digital de cliente.
	 */
	private ContaDigitalPessoaFisicaDTO1Busca buscaContaDigitalCliente(String cpfCliente) {
		ResponseEntity<ContaDigitalPessoaFisicaDTO1Busca> respostaBuscaContaDigitalPessoaFisica = contaDigitalClienteMsFeignClient
				.buscaContaDigitalPessoaFisica(cpfCliente);
		
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica = respostaBuscaContaDigitalPessoaFisica.getBody();
		return contaDigitalPessoaFisica;
	}

}
