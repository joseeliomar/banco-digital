package com.example.model;

import java.time.LocalDateTime;

import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;

import jakarta.persistence.Entity;

@Entity
public class ItemExtratoContaPessoaFisica extends ItemExtratoConta {

	private String cpf;

	public ItemExtratoContaPessoaFisica() {
	}

	public ItemExtratoContaPessoaFisica(TipoConta tipoContaDonaExtrato, Operacao operacaoEfetuada,
			String descricaoOperacao, LocalDateTime dataHoraCadastro, Banco bancoDestino, String agenciaDestino,
			String contaDestino, double valor, String cpf) {
		super(tipoContaDonaExtrato, operacaoEfetuada, descricaoOperacao, dataHoraCadastro, bancoDestino, agenciaDestino,
				contaDestino, valor);
		this.cpf = cpf;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

}
