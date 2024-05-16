package com.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.feignclients.EnderecoFeignClient;
import com.example.feignclients.dto.EnderecoDto;
import com.example.model.ContaDigitalPessoaFisica;
import com.example.repository.ContaDigitalPessoaFisicaRepository;
import com.example.service.exceptions.ValidacaoException;

//@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ContaDigitalPessoaFisicaServiceTest {

	@TestConfiguration
	static class BookingServiceTestConfiguration {

        @Bean
        ContaDigitalPessoaFisicaService contaDigitalPessoaFisicaService() {
			return new ContaDigitalPessoaFisicaService();
		}
	}

	@Autowired
	private ContaDigitalPessoaFisicaService service;
	
	@MockBean
	private ContaDigitalPessoaFisicaRepository repository;
	
	@MockBean
	private EnderecoFeignClient enderecoFeignClient;
	
	private ContaDigitalPessoaFisica contaDigitalPessoaFisica1;
	
	private EnderecoDto enderecoDto;
	
	@BeforeEach
	void setup() {
		// Given
		Long codigoEnderecoExistente = 1L;
		
		enderecoDto = new EnderecoDto(codigoEnderecoExistente);
		given(enderecoFeignClient.buscaEndereco(codigoEnderecoExistente)).willReturn(enderecoDto);
		
		String cpf1 = "12345678901";
		contaDigitalPessoaFisica1 = new ContaDigitalPessoaFisica("1234567890", "0000000011", "12345678",
				"19980001234", "fulano@email.com", codigoEnderecoExistente, null, null, cpf1, "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
		
		given(repository.findById(cpf1)).willReturn(Optional.of(contaDigitalPessoaFisica1));
	}

	@DisplayName("Cria uma conta digital com sucesso quando nenhuma exceção for lançada e for retornado um objeto não nullo do tipo persistido")
	@Test
	void testCriaContaDigital_WithSucesso_NenhumaExcecaoLancadaRetornadoObjetoNaoNullo() {
		// Given
		given(repository.save(any(ContaDigitalPessoaFisica.class))).willReturn(contaDigitalPessoaFisica1);
		
		// When & Then
		ContaDigitalPessoaFisica actual = assertDoesNotThrow(
				() -> service.criaContaDigitalPessoaFisica(contaDigitalPessoaFisica1),
				() -> "Não deve ser lançada nehuma exceção.");

		assertNotNull(actual, () -> "O objeto retornado do tipo " + ContaDigitalPessoaFisica.class.getSimpleName()
				+ " não pode ser nulo.");
	}

	@DisplayName("Quanto tenta criar conta digital com a agência não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaNula_DeveSerLancadaExcecao() {
		// Given
		String agenciaNula = null;
		contaDigitalPessoaFisica1.setAgencia(agenciaNula);
		String mensagemEsperada = "Agência não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quanto tenta criar conta digital com a agência não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String agenciaEmBranco = "       ";
		contaDigitalPessoaFisica1.setAgencia(agenciaEmBranco);
		String mensagemEsperada = "Agência não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com a agência com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaCom9Caracteres = "123456789";
		contaDigitalPessoaFisica1.setAgencia(agenciaCom9Caracteres);
		String mensagemEsperada = "Agência com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com a agência com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaCom11Caracteres = "12345678901";
		contaDigitalPessoaFisica1.setAgencia(agenciaCom11Caracteres);
		String mensagemEsperada = "Agência com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com a conta não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaNula_DeveSerLancadaExcecao() {
		// Given
		String contaNula = null;
		contaDigitalPessoaFisica1.setConta(contaNula);
		String mensagemEsperada = "Conta não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quanto tenta criar conta digital com a conta não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String contaEmBranco = "       ";
		contaDigitalPessoaFisica1.setConta(contaEmBranco);
		String mensagemEsperada = "Conta não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com a conta com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom9Caracteres = "123456789";
		contaDigitalPessoaFisica1.setConta(contaCom9Caracteres);
		String mensagemEsperada = "Conta com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com a conta com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom11Caracteres = "12345678901";
		contaDigitalPessoaFisica1.setConta(contaCom11Caracteres);
		String mensagemEsperada = "Conta com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com a senha não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaNula_DeveSerLancadaExcecao() {
		// Given
		String senhaNula = null;
		contaDigitalPessoaFisica1.setSenha(senhaNula);
		String mensagemEsperada = "Senha não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com a senha não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String senhaEmBranco = "       ";
		contaDigitalPessoaFisica1.setSenha(senhaEmBranco);
		String mensagemEsperada = "Senha não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com a senha com menos de 8 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaComMenosOitoCaracteres_DeveSerLancadaExcecao() {
		// Given
		String senhaCom7Caracteres = "123456@";
		contaDigitalPessoaFisica1.setSenha(senhaCom7Caracteres);
		String mensagemEsperada = "Senha com menos de 8 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();
		
		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com a senha com mais de 16 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaComMaisDezesseisCaracteres_DeveSerLancadaExcecao() {
		// Given
		String senhaCom17Caracteres = "1234567890123456@";
		contaDigitalPessoaFisica1.setSenha(senhaCom17Caracteres);
		String mensagemEsperada = "Senha com mais de 16 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o telefone não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComTelefoneNulo_DeveSerLancadaExcecao() {
		// Given
		String telefoneNulo = null;
		contaDigitalPessoaFisica1.setTelefone(telefoneNulo);
		String mensagemEsperada = "Telefone não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o telefone não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComTelefoneEmBranco_DeveSerLancadaExcecao() {
		// Given
		String telefoneEmBranco = "       ";
		contaDigitalPessoaFisica1.setTelefone(telefoneEmBranco);
		String mensagemEsperada = "Telefone não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o telefone com mais de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComTelefoneComMaisOnzeCaracteres_DeveSerLancadaExcecao() {
		// Given
		String telefoneCom12Caracteres = "129456789012";
		contaDigitalPessoaFisica1.setTelefone(telefoneCom12Caracteres);
		String mensagemEsperada = "Telefone com mais de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o e-mail não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEmailNulo_DeveSerLancadaExcecao() {
		// Given
		String emailNulo = null;
		contaDigitalPessoaFisica1.setEmail(emailNulo);
		String mensagemEsperada = "E-mail não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o e-mail não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEmailEmBranco_DeveSerLancadaExcecao() {
		// Given
		String emailEmBranco = "       ";
		contaDigitalPessoaFisica1.setEmail(emailEmBranco);
		String mensagemEsperada = "E-mail não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o e-mail sem o símbolo @ (arroba) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEmailSemArroba_DeveSerLancadaExcecao() {
		// Given
		String emailEmBranco = "email123gmail.com";
		contaDigitalPessoaFisica1.setEmail(emailEmBranco);
		String mensagemEsperada = "E-mail informado sem o símbolo @ (arroba).";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o e-mail com mais de 50 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEmailComMaisCinquentaCaracteres_DeveSerLancadaExcecao() {
		// Given
		String emailCom51Caracteres = "enderecoemail.grande.grande.grande.grande@gmail.com";
		contaDigitalPessoaFisica1.setEmail(emailCom51Caracteres);
		String mensagemEsperada = "E-mail com mais de 50 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o endereço não localizado deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEnderecoNaoLocalizado_DeveSerLancadaExcecao() {
		// Given
		Long codigoEnderecoNaoExistente = 998955511747451151L;
		contaDigitalPessoaFisica1.setIdEndereco(codigoEnderecoNaoExistente);
		String mensagemEsperada = "O endereço não foi localizado.";
		
		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o CPF não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCpfNulo_DeveSerLancadaExcecao() {
		// Given
		String cpfNulo = null;
		contaDigitalPessoaFisica1.setCpf(cpfNulo);
		String mensagemEsperada = "CPF não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o CPF não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCpfEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cpfEmBranco = "       ";
		contaDigitalPessoaFisica1.setCpf(cpfEmBranco);
		String mensagemEsperada = "CPF não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o CPF com menos de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCpfComMenos11Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cpfCom10Caracteres = "1234567890";
		contaDigitalPessoaFisica1.setCpf(cpfCom10Caracteres);
		String mensagemEsperada = "CPF com menos de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o CPF com mais de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCpfComMaisOnzeCaracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom12Caracteres = "123456789012";
		contaDigitalPessoaFisica1.setCpf(contaCom12Caracteres);
		String mensagemEsperada = "CPF com mais de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o nome completo não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoNulo_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoNulo = null;
		contaDigitalPessoaFisica1.setNomeCompleto(nomeCompletoNulo);
		String mensagemEsperada = "Nome completo não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o nome completo não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoEmBranco_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoEmBranco = "       ";
		contaDigitalPessoaFisica1.setNomeCompleto(nomeCompletoEmBranco);
		String mensagemEsperada = "Nome completo não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o nome completo com mais de 100 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoComMaisCemCaracteres_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoCom101Caracteres = "Fulano de Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal";
		contaDigitalPessoaFisica1.setNomeCompleto(nomeCompletoCom101Caracteres);
		String mensagemEsperada = "Nome completo com mais de 100 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com a data de nascimento não informada deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComDataNascimentoNulo_DeveSerLancadaExcecao() {
		// Given
		LocalDate dataNascimentoNulo = null;
		contaDigitalPessoaFisica1.setDataNascimento(dataNascimentoNulo);
		String mensagemEsperada = "Data nascimento não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o nome completo da mãe não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoMaeNulo_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeNulo = null;
		contaDigitalPessoaFisica1.setNomeCompletoMae(nomeCompletoMaeNulo);
		String mensagemEsperada = "Nome completo da mãe não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o nome completo da mãe não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoMaeEmBranco_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeEmBranco = "       ";
		contaDigitalPessoaFisica1.setNomeCompletoMae(nomeCompletoMaeEmBranco);
		String mensagemEsperada = "Nome completo da mãe não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta criar conta digital com o nome completo da mãe com mais de 100 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoMaeComMaisCemCaracteres_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeCom101Caracteres = "Fulana de Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal";
		contaDigitalPessoaFisica1.setNomeCompletoMae(nomeCompletoMaeCom101Caracteres);
		String mensagemEsperada = "Nome completo da mãe com mais de 100 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	// ----------------------------------------------------------- Alteração --------------------------------------------------------------------
	
	@DisplayName("Altera uma conta digital com sucesso quando nenhuma exceção for lançada e for retornado um objeto não nullo do tipo persistido")
	@Test
	void testAlteraContaDigital_WithSucesso_NenhumaExcecaoLancadaRetornadoObjetoNaoNullo() {
		// Given
		given(repository.save(any(ContaDigitalPessoaFisica.class))).willReturn(contaDigitalPessoaFisica1);
		
		// When & Then
		ContaDigitalPessoaFisica actual = assertDoesNotThrow(
				() -> service.alteraContaDigitalPessoaFisica(contaDigitalPessoaFisica1),
				() -> "Não deve ser lançada nehuma exceção.");

		assertNotNull(actual, () -> "O objeto retornado do tipo " + ContaDigitalPessoaFisica.class.getSimpleName()
				+ " não pode ser nulo.");
	}

	@DisplayName("Quanto tenta alterar conta digital com a agência não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComAgenciaNula_DeveSerLancadaExcecao() {
		// Given
		String agenciaNula = null;
		contaDigitalPessoaFisica1.setAgencia(agenciaNula);
		String mensagemEsperada = "Agência não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quanto tenta alterar conta digital com a agência não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComAgenciaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String agenciaEmBranco = "       ";
		contaDigitalPessoaFisica1.setAgencia(agenciaEmBranco);
		String mensagemEsperada = "Agência não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com a agência com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComAgenciaComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaCom9Caracteres = "123456789";
		contaDigitalPessoaFisica1.setAgencia(agenciaCom9Caracteres);
		String mensagemEsperada = "Agência com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com a agência com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComAgenciaComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaCom11Caracteres = "12345678901";
		contaDigitalPessoaFisica1.setAgencia(agenciaCom11Caracteres);
		String mensagemEsperada = "Agência com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com a conta não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComContaNula_DeveSerLancadaExcecao() {
		// Given
		String contaNula = null;
		contaDigitalPessoaFisica1.setConta(contaNula);
		String mensagemEsperada = "Conta não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quanto tenta alterar conta digital com a conta não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComContaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String contaEmBranco = "       ";
		contaDigitalPessoaFisica1.setConta(contaEmBranco);
		String mensagemEsperada = "Conta não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com a conta com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComContaComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom9Caracteres = "123456789";
		contaDigitalPessoaFisica1.setConta(contaCom9Caracteres);
		String mensagemEsperada = "Conta com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com a conta com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComContaComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom11Caracteres = "12345678901";
		contaDigitalPessoaFisica1.setConta(contaCom11Caracteres);
		String mensagemEsperada = "Conta com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com a senha não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComSenhaNula_DeveSerLancadaExcecao() {
		// Given
		String senhaNula = null;
		contaDigitalPessoaFisica1.setSenha(senhaNula);
		String mensagemEsperada = "Senha não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com a senha não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComSenhaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String senhaEmBranco = "       ";
		contaDigitalPessoaFisica1.setSenha(senhaEmBranco);
		String mensagemEsperada = "Senha não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com a senha com menos de 8 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComSenhaComMenosOitoCaracteres_DeveSerLancadaExcecao() {
		// Given
		String senhaCom7Caracteres = "123456@";
		contaDigitalPessoaFisica1.setSenha(senhaCom7Caracteres);
		String mensagemEsperada = "Senha com menos de 8 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();
		
		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com a senha com mais de 16 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComSenhaComMaisDezesseisCaracteres_DeveSerLancadaExcecao() {
		// Given
		String senhaCom17Caracteres = "1234567890123456@";
		contaDigitalPessoaFisica1.setSenha(senhaCom17Caracteres);
		String mensagemEsperada = "Senha com mais de 16 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o telefone não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComTelefoneNulo_DeveSerLancadaExcecao() {
		// Given
		String telefoneNulo = null;
		contaDigitalPessoaFisica1.setTelefone(telefoneNulo);
		String mensagemEsperada = "Telefone não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o telefone não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComTelefoneEmBranco_DeveSerLancadaExcecao() {
		// Given
		String telefoneEmBranco = "       ";
		contaDigitalPessoaFisica1.setTelefone(telefoneEmBranco);
		String mensagemEsperada = "Telefone não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o telefone com mais de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComTelefoneComMaisOnzeCaracteres_DeveSerLancadaExcecao() {
		// Given
		String telefoneCom12Caracteres = "129456789012";
		contaDigitalPessoaFisica1.setTelefone(telefoneCom12Caracteres);
		String mensagemEsperada = "Telefone com mais de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o e-mail não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComEmailNulo_DeveSerLancadaExcecao() {
		// Given
		String emailNulo = null;
		contaDigitalPessoaFisica1.setEmail(emailNulo);
		String mensagemEsperada = "E-mail não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o e-mail não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComEmailEmBranco_DeveSerLancadaExcecao() {
		// Given
		String emailEmBranco = "       ";
		contaDigitalPessoaFisica1.setEmail(emailEmBranco);
		String mensagemEsperada = "E-mail não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o e-mail sem o símbolo @ (arroba) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComEmailSemArroba_DeveSerLancadaExcecao() {
		// Given
		String emailEmBranco = "email123gmail.com";
		contaDigitalPessoaFisica1.setEmail(emailEmBranco);
		String mensagemEsperada = "E-mail informado sem o símbolo @ (arroba).";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o e-mail com mais de 50 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComEmailComMaisCinquentaCaracteres_DeveSerLancadaExcecao() {
		// Given
		String emailCom51Caracteres = "enderecoemail.grande.grande.grande.grande@gmail.com";
		contaDigitalPessoaFisica1.setEmail(emailCom51Caracteres);
		String mensagemEsperada = "E-mail com mais de 50 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital sem o código do endereço deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_SemCodigoEndereco_DeveSerLancadaExcecao() {
		// Given
		Long codigoEnderecoNulo = null;
		contaDigitalPessoaFisica1.setIdEndereco(codigoEnderecoNulo);
		String mensagemEsperada = "O código do endereço não foi informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o endereço não localizado deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComEnderecoNaoLocalizado_DeveSerLancadaExcecao() {
		// Given
		Long codigoEnderecoNaoExistente = 998955511747451151L;
		contaDigitalPessoaFisica1.setIdEndereco(codigoEnderecoNaoExistente);
		String mensagemEsperada = "O endereço não foi localizado.";
		
		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o CPF não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCpfNulo_DeveSerLancadaExcecao() {
		// Given
		String cpfNulo = null;
		contaDigitalPessoaFisica1.setCpf(cpfNulo);
		String mensagemEsperada = "CPF não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o CPF não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCpfEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cpfEmBranco = "       ";
		contaDigitalPessoaFisica1.setCpf(cpfEmBranco);
		String mensagemEsperada = "CPF não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o CPF com menos de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCpfComMenos11Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cpfCom10Caracteres = "1234567890";
		contaDigitalPessoaFisica1.setCpf(cpfCom10Caracteres);
		String mensagemEsperada = "CPF com menos de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o CPF com mais de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCpfComMaisOnzeCaracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom12Caracteres = "123456789012";
		contaDigitalPessoaFisica1.setCpf(contaCom12Caracteres);
		String mensagemEsperada = "CPF com mais de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o nome completo não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoNulo_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoNulo = null;
		contaDigitalPessoaFisica1.setNomeCompleto(nomeCompletoNulo);
		String mensagemEsperada = "Nome completo não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o nome completo não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoEmBranco_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoEmBranco = "       ";
		contaDigitalPessoaFisica1.setNomeCompleto(nomeCompletoEmBranco);
		String mensagemEsperada = "Nome completo não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o nome completo com mais de 100 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoComMaisCemCaracteres_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoCom101Caracteres = "Fulano de Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal";
		contaDigitalPessoaFisica1.setNomeCompleto(nomeCompletoCom101Caracteres);
		String mensagemEsperada = "Nome completo com mais de 100 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com a data de nascimento não informada deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComDataNascimentoNulo_DeveSerLancadaExcecao() {
		// Given
		LocalDate dataNascimentoNulo = null;
		contaDigitalPessoaFisica1.setDataNascimento(dataNascimentoNulo);
		String mensagemEsperada = "Data nascimento não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o nome completo da mãe não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoMaeNulo_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeNulo = null;
		contaDigitalPessoaFisica1.setNomeCompletoMae(nomeCompletoMaeNulo);
		String mensagemEsperada = "Nome completo da mãe não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o nome completo da mãe não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoMaeEmBranco_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeEmBranco = "       ";
		contaDigitalPessoaFisica1.setNomeCompletoMae(nomeCompletoMaeEmBranco);
		String mensagemEsperada = "Nome completo da mãe não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quanto tenta alterar conta digital com o nome completo da mãe com mais de 100 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoMaeComMaisCemCaracteres_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeCom101Caracteres = "Fulana de Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal";
		contaDigitalPessoaFisica1.setNomeCompletoMae(nomeCompletoMaeCom101Caracteres);
		String mensagemEsperada = "Nome completo da mãe com mais de 100 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	private void confirmaSeExcecaoLancadaContemMensagemEsperada(String mensagemEsperada, ValidacaoException exception) {
		assertEquals(mensagemEsperada, exception.getMessage(),
				() -> "A mensagem presente na exceção lançada está incorreta.");
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital() {
		return assertThrows(ValidacaoException.class,
				() -> service.criaContaDigitalPessoaFisica(contaDigitalPessoaFisica1),
				() -> "A exceção do tipo esperado não foi lançada.");
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital() {
		return assertThrows(ValidacaoException.class,
				() -> service.alteraContaDigitalPessoaFisica(contaDigitalPessoaFisica1),
				() -> "A exceção do tipo esperado não foi lançada.");
	}
}
