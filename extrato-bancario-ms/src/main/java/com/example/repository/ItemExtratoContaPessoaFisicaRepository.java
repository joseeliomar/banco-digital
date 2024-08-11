package com.example.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.enumeration.TipoConta;
import com.example.model.ItemExtratoContaPessoaFisica;

public interface ItemExtratoContaPessoaFisicaRepository extends JpaRepository<ItemExtratoContaPessoaFisica, Long> {

	@Query("SELECT i FROM ItemExtratoContaPessoaFisica i WHERE i.cpfCliente = :cpfCliente and i.tipoContaDonaExtrato = :tipoConta "
			+ "and DATE(i.dataHoraCadastro) >= DATE(:dataInicialPeriodo) and DATE(i.dataHoraCadastro) <= DATE(:dataFinalPeriodo) "
			+ "order by i.dataHoraCadastro asc")
	List<ItemExtratoContaPessoaFisica> buscaItensExtrato(
			String cpfCliente, TipoConta tipoConta, LocalDateTime dataInicialPeriodo, LocalDateTime dataFinalPeriodo);

}
