package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
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
		super.validaBancoDestino(bancoDestino);
		super.validaAgenciaDestino(agenciaDestino);
		super.validaContaDestino(contaDestino);
		validaCpfCliente(cpfCliente);
		
		ItemExtratoContaPessoaFisica novoItemExtratoContaPessoaFisica = new ItemExtratoContaPessoaFisica(
				tipoContaDonaExtrato, operacaoEfetuada, descricaoOperacao, null, bancoDestino, agenciaDestino,
				contaDestino, valor, cpfCliente);
		return itemExtratoContaPessoaFisicaRepository.save(novoItemExtratoContaPessoaFisica);
	}
	
	private void validaCpfCliente(String cpfCliente) {
		if (cpfCliente == null || cpfCliente.isBlank()) {
			throw new ValidacaoException("O CPF do cliente não foi informado.", HttpStatus.BAD_REQUEST);
		}
		if (cpfCliente.length() < 11) {
			throw new ValidacaoException("O CPF do cliente está com menos de 11 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (cpfCliente.length() > 11) {
			throw new ValidacaoException("O CPF do cliente está com mais de 11 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	public void removeItemExtratoContaPessoaFisica(Long id) {
		Optional<ItemExtratoContaPessoaFisica> itemExtratoContaPessoaFisicaOptional = itemExtratoContaPessoaFisicaRepository.findById(id);
		
		ItemExtratoContaPessoaFisica contaDigitalPessoaFisicaSalvaBancoDados = itemExtratoContaPessoaFisicaOptional
				.orElseThrow(() -> new ValidacaoException(
						"Não foi encontrado um item de extrato de conta de pessoa física com o código informado.",
						HttpStatus.BAD_REQUEST));
		
		itemExtratoContaPessoaFisicaRepository.delete(contaDigitalPessoaFisicaSalvaBancoDados);
	}
}
