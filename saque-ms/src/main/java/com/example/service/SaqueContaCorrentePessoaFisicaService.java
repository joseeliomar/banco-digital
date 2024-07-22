package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.DadosParaSaqueContaPessoaFisicaDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.openfeign.feignclient.ContaCorrentePoupancaMsFeignClient;
import com.example.openfeign.feignclient.ContaDigitalClienteMsFeignClient;
import com.example.openfeign.feignclient.ExtratoBancarioMsFeignClient;

@Service
public class SaqueContaCorrentePessoaFisicaService extends SaqueContaCorrenteService {
	@Autowired
	private ExtratoBancarioMsFeignClient extratoBancarioMsFeignClient;

	@Autowired
	private ContaCorrentePoupancaMsFeignClient contaCorrentePoupancaMsFeignClient;
	
	@Autowired
	private ContaDigitalClienteMsFeignClient contaDigitalClienteMsFeignClient;

	/**
	 * Efetua saque em uma conta corrente de pessoa física.
	 * 
	 * @param dadosParaSaqueDto Os dados para o saque
	 */
	public void efetuaSaqueContaCorrentePessoaFisica(DadosParaSaqueContaPessoaFisicaDto dadosParaSaqueDto) {
		super.validaValorSaque(dadosParaSaqueDto.valorSaque());
		
		atualizaSaldoContaCorrentePessoaFisica(dadosParaSaqueDto);
		insereItemExtratoContaCorrentePessoaFisica(dadosParaSaqueDto);
	}

	/**
	 * Atualiza o saldo de uma conta corrente de pessoa física.
	 * 
	 * @param dadosParaSaqueDto Os dados para o saque
	 */
	private void atualizaSaldoContaCorrentePessoaFisica(
			DadosParaSaqueContaPessoaFisicaDto dadosParaSaqueDto) {
		ContaPessoaFisicaBuscaDto1 contaCorrentePessoaFisica = buscaContaCorrentePessoaFisica(dadosParaSaqueDto.cpfCliente());
		
		double novoSaldoContaCorrente = contaCorrentePessoaFisica.getSaldo() - dadosParaSaqueDto.valorSaque();
		
		contaCorrentePoupancaMsFeignClient.alteraContaPessoaFisica(
				new ContaPessoaFisicaAlteracaoDto(contaCorrentePessoaFisica.getId(), novoSaldoContaCorrente));
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
	 * Insere novo item de extrato para uma conta corrente de pessoa física.
	 * 
	 * @param dadosParaSaqueDto Os dados para o saque
	 */
	private void insereItemExtratoContaCorrentePessoaFisica(DadosParaSaqueContaPessoaFisicaDto dadosParaSaqueDto) {
		String cpfCliente = dadosParaSaqueDto.cpfCliente();
		
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica = buscaContaDigitalCliente(cpfCliente);
		
		var novoItemExtratoContaCorrentePessoaFisicaInsercaoDto = new ItemExtratoContaPessoaFisicaInsercaoDto(
				TipoConta.CORRENTE,
				Operacao.SAQUE,
				Operacao.SAQUE.getDescricao(),
				ESSE_MESMO_BANCO,
				contaDigitalPessoaFisica.getAgencia(),
				contaDigitalPessoaFisica.getConta(),
				dadosParaSaqueDto.valorSaque(),
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
