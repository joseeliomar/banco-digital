package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.model.ContaDigitalPessoaFisica;
import com.example.repository.ContaDigitalPessoaFisicaRepository;
import com.example.service.exceptions.ValidacaoException;

//@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
//@SpringBootTest
class ContaDigitalPessoaFisicaServiceTest {

	@TestConfiguration
	static class BookingServiceTestConfiguration {

        @Bean
        ContaDigitalPessoaFisicaService contaDigitalPessoaFisicaService() {
			return new ContaDigitalPessoaFisicaService();
		}
	}

	@Autowired
	ContaDigitalPessoaFisicaService service;
	
	@MockBean
	ContaDigitalPessoaFisicaRepository repository;
	
	ContaDigitalPessoaFisica contaDigitalPessoaFisica1;
	
	@BeforeEach
	void setup() {
		// Given
		contaDigitalPessoaFisica1 = new ContaDigitalPessoaFisica("1234567890", "0000000011", "12345678",
				"19980001234", "fulano@email.com", 1L, LocalDateTime.now(), null, "1234567890", "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "BR", "Fulana de Tal");
	}

	@DisplayName("Cria uma conta digital com sucesso quando nenhuma exceção for lançada e for retornado um objeto não nullo do tipo persistido")
	@Test
	void testCriaContaDigital_WithSucesso_NenhumaExcecaoLancadaRetornadoObjetoNaoNullo() {
		// Given
		given(repository.save(any(ContaDigitalPessoaFisica.class))).willReturn(contaDigitalPessoaFisica1);
		
		// When & Then
		ContaDigitalPessoaFisica actual = Assertions.assertDoesNotThrow(
				() -> service.criaContaDigitalPessoaFisica(contaDigitalPessoaFisica1),
				() -> "Não deve ser lançada nehuma exceção.");

		Assertions.assertNotNull(actual, () -> "O objeto retornado do tipo "
				+ ContaDigitalPessoaFisica.class.getSimpleName() + " não pode ser nulo.");
	}

	@DisplayName("Quanto tenta criar conta digital com agência não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaNula_DeveSerLancadaExcecao() {
		// Given
		String agenciaNula = null;
		contaDigitalPessoaFisica1.setAgencia(agenciaNula);
		String mensagemEsperada = "Agência não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quanto tenta criar conta digital com agência não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String agenciaEmBranco = "       ";
		contaDigitalPessoaFisica1.setAgencia(agenciaEmBranco);
		String mensagemEsperada = "Agência não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com agência com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaCom9Caracteres = "123456789";
		contaDigitalPessoaFisica1.setAgencia(agenciaCom9Caracteres);
		String mensagemEsperada = "Agência com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com agência com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaCom11Caracteres = "12345678901";
		contaDigitalPessoaFisica1.setAgencia(agenciaCom11Caracteres);
		String mensagemEsperada = "Agência com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com conta não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaNula_DeveSerLancadaExcecao() {
		// Given
		String contaNula = null;
		contaDigitalPessoaFisica1.setConta(contaNula);
		String mensagemEsperada = "Conta não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quanto tenta criar conta digital com conta não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String contaEmBranco = "       ";
		contaDigitalPessoaFisica1.setConta(contaEmBranco);
		String mensagemEsperada = "Conta não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com conta com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom9Caracteres = "123456789";
		contaDigitalPessoaFisica1.setConta(contaCom9Caracteres);
		String mensagemEsperada = "Conta com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com conta com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom11Caracteres = "12345678901";
		contaDigitalPessoaFisica1.setConta(contaCom11Caracteres);
		String mensagemEsperada = "Conta com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com senha não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaNula_DeveSerLancadaExcecao() {
		// Given
		String senhaNula = null;
		contaDigitalPessoaFisica1.setSenha(senhaNula);
		String mensagemEsperada = "Senha não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com senha não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String senhaEmBranco = "       ";
		contaDigitalPessoaFisica1.setSenha(senhaEmBranco);
		String mensagemEsperada = "Senha não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com senha com menos de 8 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaComMenosOitoCaracteres_DeveSerLancadaExcecao() {
		// Given
		String senhaCom7Caracteres = "123456@";
		contaDigitalPessoaFisica1.setSenha(senhaCom7Caracteres);
		String mensagemEsperada = "Senha com menos de 8 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();
		
		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com senha com mais de 16 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaComMaisDezesseisCaracteres_DeveSerLancadaExcecao() {
		// Given
		String senhaCom17Caracteres = "1234567890123456@";
		contaDigitalPessoaFisica1.setSenha(senhaCom17Caracteres);
		String mensagemEsperada = "Senha com mais de 16 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com telefone não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComTelefoneNulo_DeveSerLancadaExcecao() {
		// Given
		String telefoneNulo = null;
		contaDigitalPessoaFisica1.setTelefone(telefoneNulo);
		String mensagemEsperada = "Telefone não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com telefone não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComTelefoneEmBranco_DeveSerLancadaExcecao() {
		// Given
		String telefoneEmBranco = "       ";
		contaDigitalPessoaFisica1.setTelefone(telefoneEmBranco);
		String mensagemEsperada = "Telefone não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com telefone com mais de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComTelefoneComMaisOnzeCaracteres_DeveSerLancadaExcecao() {
		// Given
		String telefoneCom12Caracteres = "129456789012";
		contaDigitalPessoaFisica1.setTelefone(telefoneCom12Caracteres);
		String mensagemEsperada = "Telefone com mais de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com e-mail não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEmailNulo_DeveSerLancadaExcecao() {
		// Given
		String emailNulo = null;
		contaDigitalPessoaFisica1.setEmail(emailNulo);
		String mensagemEsperada = "E-mail não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com e-mail não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEmailEmBranco_DeveSerLancadaExcecao() {
		// Given
		String emailEmBranco = "       ";
		contaDigitalPessoaFisica1.setEmail(emailEmBranco);
		String mensagemEsperada = "E-mail não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com e-mail sem o símbolo @ (arroba) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEmailSemArroba_DeveSerLancadaExcecao() {
		// Given
		String emailEmBranco = "email123gmail.com";
		contaDigitalPessoaFisica1.setEmail(emailEmBranco);
		String mensagemEsperada = "E-mail informado sem o símbolo @ (arroba).";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital sem o código do endereço deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_SemCodigoEndereco_DeveSerLancadaExcecao() {
		// Given
		Long codigoEnderecoNulo = null;
		contaDigitalPessoaFisica1.setIdEndereco(codigoEnderecoNulo);
		String mensagemEsperada = "O código do endereço não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com endereço não localizado deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEnderecoNaoLocalizado_DeveSerLancadaExcecao() {
		// Given
		Long codigoEnderecoNulo = 1L;
		contaDigitalPessoaFisica1.setIdEndereco(codigoEnderecoNulo);
		String mensagemEsperada = "O código do endereço não foi informado.";
//		given();
		
		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperado();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	private void confirmaSeExcecaoLancadaContemMensagemEsperada(String mensagemEsperada, ValidacaoException exception) {
		Assertions.assertEquals(mensagemEsperada, exception.getMessage(),
				() -> "A mensagem presente na exceção lançada está incorreta.");
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperado() {
		return Assertions.assertThrows(ValidacaoException.class,
				() -> service.criaContaDigitalPessoaFisica(contaDigitalPessoaFisica1),
				() -> "A exceção do tipo esperado não foi lançada.");
	}
	
	
}
