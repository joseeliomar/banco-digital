package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.model.ItemExtratoContaPessoaFisica;
import com.example.repository.ItemExtratoContaPessoaFisicaRepository;

public class ItemExtratoContaPessoaFisicaService extends ItemExtratoContaService{
	
	@Autowired
	private ItemExtratoContaPessoaFisicaRepository itemExtratoContaPessoaFisicaRepository;

	public ItemExtratoContaPessoaFisica insereItemExtratoContaPessoaFisica(
			ItemExtratoContaPessoaFisicaInsercaoDto itemExtratoContaPessoaFisicaInsercaoDto) {
		TipoConta tipoContaDonaExtrato = itemExtratoContaPessoaFisicaInsercaoDto.getTipoContaDonaExtrato();
		Operacao operacaoEfetuada = itemExtratoContaPessoaFisicaInsercaoDto.getOperacaoEfetuada();
		String descricaoOperacao = itemExtratoContaPessoaFisicaInsercaoDto.getDescricaoOperacao();
		Banco bancoDestino = itemExtratoContaPessoaFisicaInsercaoDto.getBancoDestino();
		String agenciaDestino = itemExtratoContaPessoaFisicaInsercaoDto.getAgenciaDestino();
		String contaDestino = itemExtratoContaPessoaFisicaInsercaoDto.getContaDestino();
		double valor = itemExtratoContaPessoaFisicaInsercaoDto.getValor();
		String cpfCliente = itemExtratoContaPessoaFisicaInsercaoDto.getCpfCliente();
		
		super.validaTipoConta(tipoContaDonaExtrato);
		super.validaOperacaoEfetuada(operacaoEfetuada);
		super.validaDescricaoOperacao(descricaoOperacao);
		
		ItemExtratoContaPessoaFisica novoItemExtratoContaPessoaFisica = new ItemExtratoContaPessoaFisica(
				tipoContaDonaExtrato, operacaoEfetuada, descricaoOperacao, null, bancoDestino, agenciaDestino,
				contaDestino, valor, cpfCliente);
		return itemExtratoContaPessoaFisicaRepository.save(novoItemExtratoContaPessoaFisica);
	}

}
