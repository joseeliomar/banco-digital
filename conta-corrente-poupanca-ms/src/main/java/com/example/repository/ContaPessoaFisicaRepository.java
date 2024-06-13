package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.enumeration.TipoConta;
import com.example.model.ContaPessoaFisica;

public interface ContaPessoaFisicaRepository extends JpaRepository<ContaPessoaFisica, Long> {

	Optional<ContaPessoaFisica> findByCpfAndTipoConta(String cpf, TipoConta tipoConta);

}
