package com.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "contas_digitais_pessoas_juridicas", uniqueConstraints = {
		@UniqueConstraint(name = "unica_agencia_conta_tabela_cdpj", columnNames = { "agencia", "conta" }) })
public class ContaDigitalPessoaJuridica extends ContaDigitalCliente {

	@Id
	private String cnpj;

	@Column(name = "nome_social", nullable = false, length = 100)
	private String nomeSocial;

	public ContaDigitalPessoaJuridica(String agencia, String conta, String senha, String telefone, String email,
			Long idEndereco, LocalDateTime dataHoraCadastro, LocalDateTime dataHoraAlteracao, String cnpj,
			String nomeSocial) {
		super(agencia, conta, senha, telefone, email, idEndereco, dataHoraCadastro, dataHoraAlteracao);
		this.cnpj = cnpj;
		this.nomeSocial = nomeSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNomeSocial() {
		return nomeSocial;
	}

	public void setNomeSocial(String nomeSocial) {
		this.nomeSocial = nomeSocial;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cnpj);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaDigitalPessoaJuridica other = (ContaDigitalPessoaJuridica) obj;
		return Objects.equals(cnpj, other.cnpj);
	}

}
