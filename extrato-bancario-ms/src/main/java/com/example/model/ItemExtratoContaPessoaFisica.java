package com.example.model;

import java.time.LocalDateTime;

import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ItemExtratoContaPessoaFisica extends ItemExtratoConta {

	@Column(name = "cpf_cliente", length = 11, nullable = false)
	private String cpfCliente;

	public ItemExtratoContaPessoaFisica() {
	}

	public ItemExtratoContaPessoaFisica(TipoConta tipoContaDonaExtrato, Operacao operacaoEfetuada,
			String descricaoOperacao, LocalDateTime dataHoraCadastro, Banco bancoDestino, String agenciaDestino,
			String contaDestino, double valor, String cpfCliente) {
		super(tipoContaDonaExtrato, operacaoEfetuada, descricaoOperacao, dataHoraCadastro, bancoDestino, agenciaDestino,
				contaDestino, valor);
		this.cpfCliente = cpfCliente;
	}

	public String getCpfCliente() {
		return cpfCliente;
	}

	public void setCpfCliente(String cpfCliente) {
		this.cpfCliente = cpfCliente;
	}

}
