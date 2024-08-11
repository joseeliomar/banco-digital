package com.example.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dto.ItemExtratoContaPessoaJuridicaInsercaoDto;
import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.model.ItemExtratoContaPessoaJuridica;
import com.example.repository.ItemExtratoContaPessoaJuridicaRepository;

@Service
public class ExtratoContaPessoaJuridicaService extends ItemExtratoContaService{
	
	@Autowired
	private ItemExtratoContaPessoaJuridicaRepository itemExtratoContaPessoaJuridicaRepository;

	public ItemExtratoContaPessoaJuridica insereItemExtratoContaPessoaJuridica(
			ItemExtratoContaPessoaJuridicaInsercaoDto itemExtratoContaPessoaJuridicaInsercaoDto) {
		TipoConta tipoContaDonaExtrato = itemExtratoContaPessoaJuridicaInsercaoDto.getTipoContaDonaExtrato();
		Operacao operacaoEfetuada = itemExtratoContaPessoaJuridicaInsercaoDto.getOperacaoEfetuada();
		String descricaoOperacao = itemExtratoContaPessoaJuridicaInsercaoDto.getDescricaoOperacao();
		Banco bancoDestino = itemExtratoContaPessoaJuridicaInsercaoDto.getBancoDestino();
		String agenciaDestino = itemExtratoContaPessoaJuridicaInsercaoDto.getAgenciaDestino();
		String contaDestino = itemExtratoContaPessoaJuridicaInsercaoDto.getContaDestino();
		double valor = itemExtratoContaPessoaJuridicaInsercaoDto.getValor();
		String cnpjCliente = itemExtratoContaPessoaJuridicaInsercaoDto.getCnpjCliente();
		
		super.validaTipoConta(tipoContaDonaExtrato);
		super.validaOperacaoEfetuada(operacaoEfetuada);
		super.validaDescricaoOperacao(descricaoOperacao);
		super.validaBancoDestino(bancoDestino);
		super.validaAgenciaDestino(agenciaDestino);
		super.validaContaDestino(contaDestino);
		validaCnpjCliente(cnpjCliente);
		
		LocalDateTime dataHoraCadastro = LocalDateTime.now();
		ItemExtratoContaPessoaJuridica novoItemExtratoContaPessoaJuridica = new ItemExtratoContaPessoaJuridica(
				tipoContaDonaExtrato, operacaoEfetuada, descricaoOperacao, dataHoraCadastro, bancoDestino,
				agenciaDestino, contaDestino, valor, cnpjCliente);
		return itemExtratoContaPessoaJuridicaRepository.save(novoItemExtratoContaPessoaJuridica);
	}
	
	private void validaCnpjCliente(String cnpjCliente) {
		if (cnpjCliente == null || cnpjCliente.isBlank()) {
			throw new ValidacaoException("O CNPJ do cliente não foi informado.", HttpStatus.BAD_REQUEST);
		}
		if (cnpjCliente.length() < 14) {
			throw new ValidacaoException("O CNPJ do cliente está com menos de 14 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (cnpjCliente.length() > 14) {
			throw new ValidacaoException("O CNPJ do cliente está com mais de 14 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	public void removeItemExtratoContaPessoaJuridica(Long id) {
		Optional<ItemExtratoContaPessoaJuridica> itemExtratoContaPessoaJuridicaOptional = itemExtratoContaPessoaJuridicaRepository.findById(id);
		
		ItemExtratoContaPessoaJuridica contaDigitalPessoaJuridicaSalvaBancoDados = itemExtratoContaPessoaJuridicaOptional
				.orElseThrow(() -> new ValidacaoException(
						"Não foi encontrado um item de extrato de conta de pessoa jurídica com o código informado.",
						HttpStatus.BAD_REQUEST));
		
		itemExtratoContaPessoaJuridicaRepository.delete(contaDigitalPessoaJuridicaSalvaBancoDados);
	}
}
