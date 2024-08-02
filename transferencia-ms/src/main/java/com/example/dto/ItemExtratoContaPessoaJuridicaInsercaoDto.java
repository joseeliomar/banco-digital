package com.example.dto;

import java.util.Objects;

import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;

public class ItemExtratoContaPessoaJuridicaInsercaoDto {

	private TipoConta tipoContaDonaExtrato;
	private Operacao operacaoEfetuada;
	private String descricaoOperacao;
	private Banco bancoDestino;
	private String agenciaDestino;
	private String contaDestino;
	private double valor;
	private String cnpjCliente;

	public ItemExtratoContaPessoaJuridicaInsercaoDto(TipoConta tipoContaDonaExtrato, Operacao operacaoEfetuada,
			String descricaoOperacao, Banco bancoDestino, String agenciaDestino, String contaDestino, double valor,
			String cnpjCliente) {
		this.tipoContaDonaExtrato = tipoContaDonaExtrato;
		this.operacaoEfetuada = operacaoEfetuada;
		this.descricaoOperacao = descricaoOperacao;
		this.bancoDestino = bancoDestino;
		this.agenciaDestino = agenciaDestino;
		this.contaDestino = contaDestino;
		this.valor = valor;
		this.cnpjCliente = cnpjCliente;
	}

	public TipoConta getTipoContaDonaExtrato() {
		return tipoContaDonaExtrato;
	}

	public void setTipoContaDonaExtrato(TipoConta tipoContaDonaExtrato) {
		this.tipoContaDonaExtrato = tipoContaDonaExtrato;
	}

	public Operacao getOperacaoEfetuada() {
		return operacaoEfetuada;
	}

	public void setOperacaoEfetuada(Operacao operacaoEfetuada) {
		this.operacaoEfetuada = operacaoEfetuada;
	}

	public String getDescricaoOperacao() {
		return descricaoOperacao;
	}

	public void setDescricaoOperacao(String descricaoOperacao) {
		this.descricaoOperacao = descricaoOperacao;
	}

	public Banco getBancoDestino() {
		return bancoDestino;
	}

	public void setBancoDestino(Banco bancoDestino) {
		this.bancoDestino = bancoDestino;
	}

	public String getAgenciaDestino() {
		return agenciaDestino;
	}

	public void setAgenciaDestino(String agenciaDestino) {
		this.agenciaDestino = agenciaDestino;
	}

	public String getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(String contaDestino) {
		this.contaDestino = contaDestino;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getCnpjCliente() {
		return cnpjCliente;
	}

	public void setCnpjCliente(String cnpjCliente) {
		this.cnpjCliente = cnpjCliente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(agenciaDestino, bancoDestino, cnpjCliente, contaDestino, descricaoOperacao,
				operacaoEfetuada, tipoContaDonaExtrato, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemExtratoContaPessoaJuridicaInsercaoDto other = (ItemExtratoContaPessoaJuridicaInsercaoDto) obj;
		return Objects.equals(agenciaDestino, other.agenciaDestino) && bancoDestino == other.bancoDestino
				&& Objects.equals(cnpjCliente, other.cnpjCliente) && Objects.equals(contaDestino, other.contaDestino)
				&& Objects.equals(descricaoOperacao, other.descricaoOperacao)
				&& operacaoEfetuada == other.operacaoEfetuada && tipoContaDonaExtrato == other.tipoContaDonaExtrato
				&& Double.doubleToLongBits(valor) == Double.doubleToLongBits(other.valor);
	}

}
