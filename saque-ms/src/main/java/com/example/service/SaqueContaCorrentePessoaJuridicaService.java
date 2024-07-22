package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.ContaDigitalPessoaJuridicaDTO1Busca;
import com.example.dto.ContaPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.DadosParaSaqueContaPessoaJuridicaDto;
import com.example.dto.ItemExtratoContaPessoaJuridicaInsercaoDto;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.openfeign.feignclient.ContaCorrentePoupancaMsFeignClient;
import com.example.openfeign.feignclient.ContaDigitalClienteMsFeignClient;
import com.example.openfeign.feignclient.ExtratoBancarioMsFeignClient;

@Service
public class SaqueContaCorrentePessoaJuridicaService extends SaqueContaCorrenteService {
	@Autowired
	private ExtratoBancarioMsFeignClient extratoBancarioMsFeignClient;

	@Autowired
	private ContaCorrentePoupancaMsFeignClient contaCorrentePoupancaMsFeignClient;

	@Autowired
	private ContaDigitalClienteMsFeignClient contaDigitalClienteMsFeignClient;

	/**
	 * Efetua saque em uma conta corrente de pessoa jurídica.
	 * 
	 * @param dadosParaSaqueDto Os dados para o saque
	 */
	public void efetuaSaqueContaCorrentePessoaJuridica(DadosParaSaqueContaPessoaJuridicaDto dadosParaSaqueDto) {
		super.validaValorSaque(dadosParaSaqueDto.valorSaque());
		
		atualizaSaldoContaCorrentePessoaJuridica(dadosParaSaqueDto);
		insereItemExtratoContaCorrentePessoaJuridica(dadosParaSaqueDto);
	}

	/**
	 * Atualiza o saldo de uma conta corrente de pessoa jurídica.
	 * 
	 * @param dadosParaSaqueDto Os dados para o saque
	 */
	private void atualizaSaldoContaCorrentePessoaJuridica(
			DadosParaSaqueContaPessoaJuridicaDto dadosParaSaqueDto) {
		ContaPessoaJuridicaBuscaDto1 contaCorrentePessoaJuridica = buscaContaCorrentePessoaJuridica(dadosParaSaqueDto.cnpjCliente());
		
		double novoSaldoContaCorrente = contaCorrentePessoaJuridica.getSaldo() - dadosParaSaqueDto.valorSaque();
		
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
	 * @param dadosParaSaqueDto Os dados para o saque
	 */
	private void insereItemExtratoContaCorrentePessoaJuridica(DadosParaSaqueContaPessoaJuridicaDto dadosParaSaqueDto) {
		String cnpjCliente = dadosParaSaqueDto.cnpjCliente();
		
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalPessoaJuridica = buscaContaDigitalCliente(cnpjCliente);
		
		var novoItemExtratoContaCorrentePessoaJuridicaInsercaoDto = new ItemExtratoContaPessoaJuridicaInsercaoDto(
				TipoConta.CORRENTE,
				Operacao.SAQUE,
				Operacao.SAQUE.getDescricao(),
				ESSE_MESMO_BANCO,
				contaDigitalPessoaJuridica.getAgencia(),
				contaDigitalPessoaJuridica.getConta(),
				dadosParaSaqueDto.valorSaque(),
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
