package com.example.model;

import java.time.LocalDateTime;

import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ItemExtratoContaPessoaJuridica extends ItemExtratoConta {

	@Column(name = "cnpj_cliente", length = 14, nullable = false)
	private String cnpjCliente;

	public ItemExtratoContaPessoaJuridica() {
	}

	public ItemExtratoContaPessoaJuridica(TipoConta tipoContaDonaExtrato, Operacao operacaoEfetuada,
			String descricaoOperacao, LocalDateTime dataHoraCadastro, Banco bancoDestino, String agenciaDestino,
			String contaDestino, double valor, String cnpjCliente) {
		super(tipoContaDonaExtrato, operacaoEfetuada, descricaoOperacao, dataHoraCadastro, bancoDestino, agenciaDestino,
				contaDestino, valor);
		this.cnpjCliente = cnpjCliente;
	}

	public String getCnpjCliente() {
		return cnpjCliente;
	}

	public void setCnpjCliente(String cnpjCliente) {
		this.cnpjCliente = cnpjCliente;
	}

}
