package com.example.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.model.ItemExtratoContaPessoaJuridica;

public class ItemExtratoContaPessoaJuridicaInseridoDto implements Serializable {
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
	private String cnpjCliente;

	public ItemExtratoContaPessoaJuridicaInseridoDto() {
	}

	public ItemExtratoContaPessoaJuridicaInseridoDto(ItemExtratoContaPessoaJuridica itemExtratoContaPessoaJuridica) {
		this.id = itemExtratoContaPessoaJuridica.getId();
		this.tipoContaDonaExtrato = itemExtratoContaPessoaJuridica.getTipoContaDonaExtrato();
		this.operacaoEfetuada = itemExtratoContaPessoaJuridica.getOperacaoEfetuada();
		this.descricaoOperacao = itemExtratoContaPessoaJuridica.getDescricaoOperacao();
		this.dataHoraCadastro = itemExtratoContaPessoaJuridica.getDataHoraCadastro();
		this.bancoDestino = itemExtratoContaPessoaJuridica.getBancoDestino();
		this.agenciaDestino = itemExtratoContaPessoaJuridica.getAgenciaDestino();
		this.contaDestino = itemExtratoContaPessoaJuridica.getContaDestino();
		this.valor = itemExtratoContaPessoaJuridica.getValor();
		this.cnpjCliente = itemExtratoContaPessoaJuridica.getCnpjCliente();
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

	public String getCnpjCliente() {
		return cnpjCliente;
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
		ItemExtratoContaPessoaJuridicaInseridoDto other = (ItemExtratoContaPessoaJuridicaInseridoDto) obj;
		return Objects.equals(id, other.id);
	}

}
