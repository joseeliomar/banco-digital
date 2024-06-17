package com.example.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.model.ItemExtratoContaPessoaFisica;

public class ItemExtratoContaPessoaFisicaInseridoDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private TipoConta tipoContaDonaExtrato;
	private Operacao operacaoEfetuada;
	private String descricaoOperacao;
	private LocalDateTime dataHoraCadastro;
	private Banco bancoDestino;
	private String agenciaDestino;
	private String contaDestino;
	private double valor;
	private String cpfCliente;
	
	public ItemExtratoContaPessoaFisicaInseridoDto() {
	}
	
	public ItemExtratoContaPessoaFisicaInseridoDto(ItemExtratoContaPessoaFisica itemExtratoContaPessoaFisica) {
		this.id = itemExtratoContaPessoaFisica.getId();
		this.tipoContaDonaExtrato = itemExtratoContaPessoaFisica.getTipoContaDonaExtrato();
		this.operacaoEfetuada = itemExtratoContaPessoaFisica.getOperacaoEfetuada();
		this.descricaoOperacao = itemExtratoContaPessoaFisica.getDescricaoOperacao();
		this.dataHoraCadastro = itemExtratoContaPessoaFisica.getDataHoraCadastro();
		this.bancoDestino = itemExtratoContaPessoaFisica.getBancoDestino();
		this.agenciaDestino = itemExtratoContaPessoaFisica.getAgenciaDestino();
		this.contaDestino = itemExtratoContaPessoaFisica.getContaDestino();
		this.valor = itemExtratoContaPessoaFisica.getValor();
		this.cpfCliente = itemExtratoContaPessoaFisica.getCpfCliente();
	}

	public Long getId() {
		return id;
	}

	public TipoConta getTipoContaDonaExtrato() {
		return tipoContaDonaExtrato;
	}

	public Operacao getOperacaoEfetuada() {
		return operacaoEfetuada;
	}

	public String getDescricaoOperacao() {
		return descricaoOperacao;
	}

	public LocalDateTime getDataHoraCadastro() {
		return dataHoraCadastro;
	}

	public Banco getBancoDestino() {
		return bancoDestino;
	}

	public String getAgenciaDestino() {
		return agenciaDestino;
	}

	public String getContaDestino() {
		return contaDestino;
	}

	public double getValor() {
		return valor;
	}

	public String getCpfCliente() {
		return cpfCliente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemExtratoContaPessoaFisicaInseridoDto other = (ItemExtratoContaPessoaFisicaInseridoDto) obj;
		return Objects.equals(id, other.id);
	}

	
}
