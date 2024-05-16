package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.model.ContaDigitalPessoaJuridica;
import com.example.repository.ContaDigitalPessoaJuridicaRepository;
import com.example.service.exceptions.ValidacaoException;

@ExtendWith(SpringExtension.class)
class ContaDigitalPessoaJuridicaServiceTest {
	
	@TestConfiguration
	static class BookingServiceTestConfiguration {

        @Bean
        ContaDigitalPessoaJuridicaService contaDigitalPessoaFisicaService() {
			return new ContaDigitalPessoaJuridicaService();
		}
	}

	@Autowired
	private ContaDigitalPessoaJuridicaService service;
	
	@MockBean
	private ContaDigitalPessoaJuridicaRepository repository;
	
	private ContaDigitalPessoaJuridica contaDigitalPessoaJuridica1;
	
	@BeforeEach
	void setup() {
		// Given
		Long codigoEnderecoExistente = 1L;
		
		contaDigitalPessoaJuridica1 = new ContaDigitalPessoaJuridica("1234567890", "0000000011", "12345678",
				"19980001234", "fulano@email.com", codigoEnderecoExistente, null, null, "12345678901", "Fábrica Tal");
	}

	@DisplayName("Quanto tenta criar conta digital com a razão social não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComRazaoSocialNula_DeveSerLancadaExcecao() {
		// Given
		String razaoSocialNula = null;
		contaDigitalPessoaJuridica1.setRazaoSocial(razaoSocialNula);
		String mensagemEsperada = "Razão social não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	private void confirmaSeExcecaoLancadaContemMensagemEsperada(String mensagemEsperada, ValidacaoException exception) {
		assertEquals(mensagemEsperada, exception.getMessage(),
				() -> "A mensagem presente na exceção lançada está incorreta.");
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperado() {
		return assertThrows(ValidacaoException.class,
				() -> service.criaContaDigitalPessoaJuridica(contaDigitalPessoaJuridica1),
				() -> "A exceção do tipo esperado não foi lançada.");
	}
}
