package com.example.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.enumeration.TipoConta;
import com.example.model.ItemExtratoContaPessoaFisica;

public interface ItemExtratoContaPessoaFisicaRepository extends JpaRepository<ItemExtratoContaPessoaFisica, Long> {

	@Query("SELECT i FROM ItemExtratoContaPessoaFisica i "
			+ "WHERE i.cpfCliente = :cpfCliente "
			+ "AND i.tipoContaDonaExtrato = :tipoConta "
			+ "AND DATE(i.dataHoraCadastro) >= :dataInicialPeriodo "
			+ "AND DATE(i.dataHoraCadastro) <= :dataFinalPeriodo "
			+ "ORDER BY i.dataHoraCadastro ASC")
	List<ItemExtratoContaPessoaFisica> buscaItensExtrato(
			String cpfCliente, TipoConta tipoConta, LocalDate dataInicialPeriodo, LocalDate dataFinalPeriodo);

}
