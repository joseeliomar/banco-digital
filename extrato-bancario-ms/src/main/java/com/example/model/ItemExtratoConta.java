package com.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
public abstract class ItemExtratoConta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tipo_conta_dona_extrato", nullable = false, length = 8)
	@Enumerated(EnumType.STRING)
	private TipoConta tipoContaDonaExtrato;

	@Column(name = "operacao_efetuada", nullable = false, length = 60)
	@Enumerated(EnumType.STRING)
	private Operacao operacaoEfetuada;

	@Column(name = "descricao_operacao", nullable = false, length = 300)
	private String descricaoOperacao;

	@Column(name = "data_hora_cadastro", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dataHoraCadastro;

	@Column(name = "banco_destino", nullable = false, length = 2)
	@Enumerated(EnumType.STRING)
	private Banco bancoDestino;

	@Column(name = "agencia_destino", nullable = false)
	private String agenciaDestino;

	@Column(name = "conta_destino", nullable = false)
	private String contaDestino;

	@Column(nullable = false)
	private double valor;
	
	public ItemExtratoConta() {
	}

	public ItemExtratoConta(TipoConta tipoContaDonaExtrato, Operacao operacaoEfetuada, String descricaoOperacao,
			LocalDateTime dataHoraCadastro, Banco bancoDestino, String agenciaDestino, String contaDestino,
			double valor) {
		this.tipoContaDonaExtrato = tipoContaDonaExtrato;
		this.operacaoEfetuada = operacaoEfetuada;
		this.descricaoOperacao = descricaoOperacao;
		this.dataHoraCadastro = dataHoraCadastro;
		this.bancoDestino = bancoDestino;
		this.agenciaDestino = agenciaDestino;
		this.contaDestino = contaDestino;
		this.valor = valor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDateTime getDataHoraCadastro() {
		return dataHoraCadastro;
	}

	public void setDataHoraCadastro(LocalDateTime dataHoraCadastro) {
		this.dataHoraCadastro = dataHoraCadastro;
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
		ItemExtratoConta other = (ItemExtratoConta) obj;
		return Objects.equals(id, other.id);
	}

}
