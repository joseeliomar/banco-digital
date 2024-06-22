package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.DadosParaDepositoContaPessoaFisicaDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.feignclient.ContaPessoaFisicaFeignClient;
import com.example.feignclient.ItemExtratoContaPessoaFisicaFeignClient;

@Service
public class DepositoService {
	
	private static final Banco ESSE_MESMO_BANCO = Banco.JBANK;

	@Autowired
	private ItemExtratoContaPessoaFisicaFeignClient itemExtratoContaPessoaFisicaFeignClient;

	@Autowired
	private ContaPessoaFisicaFeignClient contaPessoaFisicaFeignClient;

	public void efetuaDepositoContaCorrentePessoaFisica(DadosParaDepositoContaPessoaFisicaDto dadosParaDepositoDto) {
		String cpfCliente = dadosParaDepositoDto.cpfCliente();
		double valorDeposito = dadosParaDepositoDto.valorDeposito();
		TipoConta contaCorrente = TipoConta.CORRENTE;
		
		ResponseEntity<?> respostaBuscaContaPessoaFisica = contaPessoaFisicaFeignClient
				.buscaContaPessoaFisica(cpfCliente, contaCorrente);
		ContaPessoaFisicaBuscaDto1 contaCorrentePessoaFisica = (ContaPessoaFisicaBuscaDto1) respostaBuscaContaPessoaFisica
				.getBody();

		double novoSaldoContaCorrente = contaCorrentePessoaFisica.getSaldo() + valorDeposito;
		ContaPessoaFisicaAlteracaoDto contaCorrentePessoaFisicaParaAlteracao = new ContaPessoaFisicaAlteracaoDto(
				contaCorrentePessoaFisica.getId(),
				novoSaldoContaCorrente);
		
		contaPessoaFisicaFeignClient.alteraContaPessoaFisica(contaCorrentePessoaFisicaParaAlteracao);
		
		ItemExtratoContaPessoaFisicaInsercaoDto novoItemExtratoContaPessoaFisicaInsercaoDto = new ItemExtratoContaPessoaFisicaInsercaoDto(
				contaCorrente,
				Operacao.DEPOSITO,
				Operacao.DEPOSITO.getDescricao(),
				ESSE_MESMO_BANCO,
				dadosParaDepositoDto.agenciaDestino(),
				dadosParaDepositoDto.contaDestino(),
				valorDeposito,
				cpfCliente);
		itemExtratoContaPessoaFisicaFeignClient.insereItemExtratoContaPessoaFisica(novoItemExtratoContaPessoaFisicaInsercaoDto);
	}

}
