package com.example.model;

import java.time.LocalDateTime;

import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;

import jakarta.persistence.Entity;

@Entity
public class ItemExtratoContaPessoaJuridica extends ItemExtratoConta {

	private String cnpj;

	public ItemExtratoContaPessoaJuridica() {
	}

	public ItemExtratoContaPessoaJuridica(TipoConta tipoContaDonaExtrato, Operacao operacaoEfetuada,
			String descricaoOperacao, LocalDateTime dataHoraCadastro, Banco bancoDestino, String agenciaDestino,
			String contaDestino, double valor, String cnpj) {
		super(tipoContaDonaExtrato, operacaoEfetuada, descricaoOperacao, dataHoraCadastro, bancoDestino, agenciaDestino,
				contaDestino, valor);
		this.cnpj = cnpj;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

}
