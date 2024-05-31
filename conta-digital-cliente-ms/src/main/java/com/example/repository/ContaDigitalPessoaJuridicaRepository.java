package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.ContaDigitalPessoaJuridica;

@Repository
public interface ContaDigitalPessoaJuridicaRepository extends JpaRepository<ContaDigitalPessoaJuridica, String>{

	Optional<ContaDigitalPessoaJuridica> findByAgenciaAndConta(String agencia, String conta);

}