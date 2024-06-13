package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.enumeration.TipoConta;
import com.example.model.ContaPessoaJuridica;

public interface ContaPessoaJuridicaRepository extends JpaRepository<ContaPessoaJuridica, Long> {

	Optional<ContaPessoaJuridica> findByCnpjAndTipoConta(String cnpj, TipoConta tipoConta);

}
