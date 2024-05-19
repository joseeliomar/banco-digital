package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.ContaDigitalPessoaFisica;

@Repository
public interface ContaDigitalPessoaFisicaRepository extends JpaRepository<ContaDigitalPessoaFisica, String> {

	Optional<ContaDigitalPessoaFisica> findByAgenciaAndConta(String agencia, String conta);

}
