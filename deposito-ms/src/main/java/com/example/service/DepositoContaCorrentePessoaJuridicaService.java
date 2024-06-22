package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.ContaDigitalPessoaJuridicaDTO1Busca;
import com.example.dto.ContaPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.DadosParaDepositoContaPessoaJuridicaDto;
import com.example.dto.ItemExtratoContaPessoaJuridicaInsercaoDto;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.openfeign.feignclient.ContaCorrentePoupancaMsFeignClient;
import com.example.openfeign.feignclient.ContaDigitalClienteMsFeignClient;
import com.example.openfeign.feignclient.ExtratoBancarioMsFeignClient;

@Service
public class DepositoContaCorrentePessoaJuridicaService extends DepositoContaCorrenteService {
	@Autowired
	private ExtratoBancarioMsFeignClient extratoBancarioMsFeignClient;

	@Autowired
	private ContaCorrentePoupancaMsFeignClient contaCorrentePoupancaMsFeignClient;

	@Autowired
	private ContaDigitalClienteMsFeignClient contaDigitalClienteMsFeignClient;

	/**
	 * Efetua depósito em uma conta corrente de pessoa jurídica.
	 * 
	 * @param dadosParaDepositoDto Os dados para o depósito
	 */
	public void efetuaDepositoContaCorrentePessoaJuridica(DadosParaDepositoContaPessoaJuridicaDto dadosParaDepositoDto) {
		super.validaValorDeposito(dadosParaDepositoDto.valorDeposito());
		
		atualizaSaldoContaCorrentePessoaJuridica(dadosParaDepositoDto);
		insereItemExtratoContaCorrentePessoaJuridica(dadosParaDepositoDto);
	}

	/**
	 * Atualiza o saldo de uma conta corrente de pessoa jurídica.
	 * 
	 * @param dadosParaDepositoDto Os dados para o depósito
	 */
	private void atualizaSaldoContaCorrentePessoaJuridica(
			DadosParaDepositoContaPessoaJuridicaDto dadosParaDepositoDto) {
		ContaPessoaJuridicaBuscaDto1 contaCorrentePessoaJuridica = buscaContaCorrentePessoaJuridica(dadosParaDepositoDto.cnpjCliente());
		
		double novoSaldoContaCorrente = contaCorrentePessoaJuridica.getSaldo() + dadosParaDepositoDto.valorDeposito();
		
		contaCorrentePoupancaMsFeignClient.alteraContaPessoaJuridica(
				new ContaPessoaJuridicaAlteracaoDto(contaCorrentePessoaJuridica.getId(), novoSaldoContaCorrente));
	}

	/**
	 * Busca uma conta corrente de pessoa jurídica.
	 * 
	 * @param cnpjCliente O CNPJ do cliente
	 * @return a conta corrente de pessoa jurídica buscada.
	 */
	private ContaPessoaJuridicaBuscaDto1 buscaContaCorrentePessoaJuridica(String cnpjCliente) {
		ResponseEntity<ContaPessoaJuridicaBuscaDto1> respostaBuscaContaPessoaJuridica = contaCorrentePoupancaMsFeignClient
				.buscaContaPessoaJuridica(cnpjCliente, TipoConta.CORRENTE);
		
		ContaPessoaJuridicaBuscaDto1 contaCorrentePessoaJuridica = respostaBuscaContaPessoaJuridica.getBody();
		return contaCorrentePessoaJuridica;
	}

	
	/**
	 * Insere novo item de extrato para uma conta corrente de pessoa física.
	 * 
	 * @param dadosParaDepositoDto Os dados para o depósito
	 */
	private void insereItemExtratoContaCorrentePessoaJuridica(DadosParaDepositoContaPessoaJuridicaDto dadosParaDepositoDto) {
		String cnpjCliente = dadosParaDepositoDto.cnpjCliente();
		
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalPessoaJuridica = buscaContaDigitalCliente(cnpjCliente);
		
		var novoItemExtratoContaCorrentePessoaJuridicaInsercaoDto = new ItemExtratoContaPessoaJuridicaInsercaoDto(
				TipoConta.CORRENTE,
				Operacao.DEPOSITO,
				Operacao.DEPOSITO.getDescricao(),
				ESSE_MESMO_BANCO,
				contaDigitalPessoaJuridica.getAgencia(),
				contaDigitalPessoaJuridica.getConta(),
				dadosParaDepositoDto.valorDeposito(),
				cnpjCliente);
		
		extratoBancarioMsFeignClient
				.insereItemExtratoContaPessoaJuridica(novoItemExtratoContaCorrentePessoaJuridicaInsercaoDto);
	}

	/**
	 * Busca conta digital de cliente.
	 * 
	 * @param cnpjCliente
	 * @return a conta digital de cliente.
	 */
	private ContaDigitalPessoaJuridicaDTO1Busca buscaContaDigitalCliente(String cnpjCliente) {
		ResponseEntity<ContaDigitalPessoaJuridicaDTO1Busca> respostaBuscaContaDigitalPessoaJuridica = contaDigitalClienteMsFeignClient
				.buscaContaDigitalPessoaJuridica(cnpjCliente);
		
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalPessoaJuridica = respostaBuscaContaDigitalPessoaJuridica.getBody();
		return contaDigitalPessoaJuridica;
	}

}
