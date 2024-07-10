package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.integrationtests.testcontainers.ConfiguracaoAmbienteTestesParaUsoContainers;
import com.example.model.ContaDigitalPessoaFisica;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ContaDigitalPessoaFisicaRepositoryTest extends ConfiguracaoAmbienteTestesParaUsoContainers {
	
	@Autowired
	ContaDigitalPessoaFisicaRepository contaDigitalPessoaFisicaRepository;

	@DisplayName("Deve obter uma conta digital para pessoa física com sucesso do banco de dados")
	@Test
	void testDeveObterContaDigitalPessoaFisicaComSucesso() {
		// Given
		String agencia = "1234567890";
		String conta = "0000000001";

		ContaDigitalPessoaFisica contaDigitalPessoaFisica = new ContaDigitalPessoaFisica(agencia, conta, "12345678",
				"19980001234", "fulano@email.com", 1L, LocalDateTime.now(), null, "12345678901", "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal", 2);

		contaDigitalPessoaFisicaRepository.save(contaDigitalPessoaFisica);

		// When
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = contaDigitalPessoaFisicaRepository
				.findByAgenciaAndConta(agencia, conta);

		// Then
		assertTrue(contaDigitalPessoaFisicaOptional.isPresent());
	}

	@DisplayName("Não deve obter uma conta digital para pessoa física quando não existe uma conta digital com agência e conta informados")
	@Test
	void testNaoDeveObterUmaContaDigitalPessoaFisicaQuandoNaoExisteContaDigitalComAgenciaContaInformados() {
		// Given
		String agencia = "1234567890";
		String conta = "0000000011";

		// When
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = contaDigitalPessoaFisicaRepository
				.findByAgenciaAndConta(agencia, conta);

		// Then
		assertTrue(contaDigitalPessoaFisicaOptional.isEmpty());
	}
}
