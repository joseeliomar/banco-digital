package com.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

import com.example.dto.ContaDigitalPessoaFisicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaFisicaInsercaoDto;
import com.example.dto.EnderecoDto;
import com.example.exception.ValidacaoException;
import com.example.feignclient.EnderecoFeignClient;
import com.example.model.ContaDigitalPessoaFisica;
import com.example.repository.ContaDigitalPessoaFisicaRepository;

@ExtendWith(SpringExtension.class)
class ContaDigitalPessoaFisicaServiceTest extends ContaDigitalServiceTest {

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
	
	private ContaDigitalPessoaFisicaInsercaoDto contaDigitalPessoaFisicaInsercaoDto1;
	
	private ContaDigitalPessoaFisicaAlteracaoDto contaDigitalPessoaFisicaAlteracaoDto1;
	
	private ContaDigitalPessoaFisica contaDigitalPessoaFisica1;
	
	private ContaDigitalPessoaFisica contaDigitalPessoaFisica2;
	
	private EnderecoDto enderecoDto;
	
	private Long codigoEnderecoExistente;
	
	@BeforeEach
	void setup() {
		// Given
		codigoEnderecoExistente = 1L;
		
		enderecoDto = new EnderecoDto(codigoEnderecoExistente);
		given(enderecoFeignClient.buscaEndereco(codigoEnderecoExistente)).willReturn(enderecoDto);
		
		String cpf1 = "12345678901";
		String cpf2 = "12345678902";
		contaDigitalPessoaFisicaInsercaoDto1 = new ContaDigitalPessoaFisicaInsercaoDto("1234567890", "0000000011", "12345678",
				"19980001234", "fulano@email.com", cpf1, "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
		
		contaDigitalPessoaFisica1 = new ContaDigitalPessoaFisica("1234567890", "0000000011", "12345678",
				"19980001234", "fulano@email.com", null, null, null, cpf1, "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
		
		contaDigitalPessoaFisicaAlteracaoDto1 = new ContaDigitalPessoaFisicaAlteracaoDto("1234567890", "0000000011", "12345678",
				"19980001234", "fulano@email.com", codigoEnderecoExistente, cpf2, "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
		
		contaDigitalPessoaFisica2 = new ContaDigitalPessoaFisica("1234567890", "0000000011", "12345678",
				"19980001234", "fulano@email.com", codigoEnderecoExistente, null, null, cpf2, "Fulano de Tal",
				LocalDate.of(2001, 1, 1), "Fulana de Tal");
		
		
		given(repository.findById(cpf1)).willReturn(Optional.of(contaDigitalPessoaFisica1));
		given(repository.findById(cpf2)).willReturn(Optional.of(contaDigitalPessoaFisica2));
	}

	@DisplayName("Cria uma conta digital com sucesso quando nenhuma exceção for lançada e for retornado um objeto não nulo do tipo persistido")
	@Test
	void testCriaContaDigital_ComSucesso_NenhumaExcecaoLancadaRetornadoObjetoNaoNullo() {
		// Given
		given(repository.save(any(ContaDigitalPessoaFisica.class))).willReturn(contaDigitalPessoaFisica1);
		
		// When & Then
		ContaDigitalPessoaFisica actual = assertDoesNotThrow(
				() -> service.insereContaDigitalPessoaFisica(contaDigitalPessoaFisicaInsercaoDto1),
				() -> "Não deve ser lançada nehuma exceção.");

		assertNotNull(actual, () -> "O objeto retornado do tipo " + ContaDigitalPessoaFisica.class.getSimpleName()
				+ " não pode ser nulo.");
	}

	@DisplayName("Quando tenta criar conta digital com a agência não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaNula_DeveSerLancadaExcecao() {
		// Given
		String agenciaNula = null;
		contaDigitalPessoaFisicaInsercaoDto1.setAgencia(agenciaNula);
		String mensagemEsperada = "Agência não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quando tenta criar conta digital com a agência não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String agenciaEmBranco = "       ";
		contaDigitalPessoaFisicaInsercaoDto1.setAgencia(agenciaEmBranco);
		String mensagemEsperada = "Agência não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a agência com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaCom9Caracteres = "123456789";
		contaDigitalPessoaFisicaInsercaoDto1.setAgencia(agenciaCom9Caracteres);
		String mensagemEsperada = "Agência com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a agência com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaCom11Caracteres = "12345678901";
		contaDigitalPessoaFisicaInsercaoDto1.setAgencia(agenciaCom11Caracteres);
		String mensagemEsperada = "Agência com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a conta não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaNula_DeveSerLancadaExcecao() {
		// Given
		String contaNula = null;
		contaDigitalPessoaFisicaInsercaoDto1.setConta(contaNula);
		String mensagemEsperada = "Conta não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quando tenta criar conta digital com a conta não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String contaEmBranco = "       ";
		contaDigitalPessoaFisicaInsercaoDto1.setConta(contaEmBranco);
		String mensagemEsperada = "Conta não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a conta com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom9Caracteres = "123456789";
		contaDigitalPessoaFisicaInsercaoDto1.setConta(contaCom9Caracteres);
		String mensagemEsperada = "Conta com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a conta com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComContaComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom11Caracteres = "12345678901";
		contaDigitalPessoaFisicaInsercaoDto1.setConta(contaCom11Caracteres);
		String mensagemEsperada = "Conta com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a senha não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaNula_DeveSerLancadaExcecao() {
		// Given
		String senhaNula = null;
		contaDigitalPessoaFisicaInsercaoDto1.setSenha(senhaNula);
		String mensagemEsperada = "Senha não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a senha não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String senhaEmBranco = "       ";
		contaDigitalPessoaFisicaInsercaoDto1.setSenha(senhaEmBranco);
		String mensagemEsperada = "Senha não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a senha com menos de 8 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaComMenosOitoCaracteres_DeveSerLancadaExcecao() {
		// Given
		String senhaCom7Caracteres = "123456@";
		contaDigitalPessoaFisicaInsercaoDto1.setSenha(senhaCom7Caracteres);
		String mensagemEsperada = "Senha com menos de 8 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();
		
		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a senha com mais de 16 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComSenhaComMaisDezesseisCaracteres_DeveSerLancadaExcecao() {
		// Given
		String senhaCom17Caracteres = "1234567890123456@";
		contaDigitalPessoaFisicaInsercaoDto1.setSenha(senhaCom17Caracteres);
		String mensagemEsperada = "Senha com mais de 16 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o telefone não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComTelefoneNulo_DeveSerLancadaExcecao() {
		// Given
		String telefoneNulo = null;
		contaDigitalPessoaFisicaInsercaoDto1.setTelefone(telefoneNulo);
		String mensagemEsperada = "Telefone não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o telefone não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComTelefoneEmBranco_DeveSerLancadaExcecao() {
		// Given
		String telefoneEmBranco = "       ";
		contaDigitalPessoaFisicaInsercaoDto1.setTelefone(telefoneEmBranco);
		String mensagemEsperada = "Telefone não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o telefone com mais de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComTelefoneComMaisOnzeCaracteres_DeveSerLancadaExcecao() {
		// Given
		String telefoneCom12Caracteres = "129456789012";
		contaDigitalPessoaFisicaInsercaoDto1.setTelefone(telefoneCom12Caracteres);
		String mensagemEsperada = "Telefone com mais de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o e-mail não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEmailNulo_DeveSerLancadaExcecao() {
		// Given
		String emailNulo = null;
		contaDigitalPessoaFisicaInsercaoDto1.setEmail(emailNulo);
		String mensagemEsperada = "E-mail não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o e-mail não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEmailEmBranco_DeveSerLancadaExcecao() {
		// Given
		String emailEmBranco = "       ";
		contaDigitalPessoaFisicaInsercaoDto1.setEmail(emailEmBranco);
		String mensagemEsperada = "E-mail não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o e-mail sem o símbolo @ (arroba) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEmailSemArroba_DeveSerLancadaExcecao() {
		// Given
		String emailEmBranco = "email123gmail.com";
		contaDigitalPessoaFisicaInsercaoDto1.setEmail(emailEmBranco);
		String mensagemEsperada = "E-mail informado sem o símbolo @ (arroba).";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o e-mail com mais de 50 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComEmailComMaisCinquentaCaracteres_DeveSerLancadaExcecao() {
		// Given
		String emailCom51Caracteres = "enderecoemail.grande.grande.grande.grande@gmail.com";
		contaDigitalPessoaFisicaInsercaoDto1.setEmail(emailCom51Caracteres);
		String mensagemEsperada = "E-mail com mais de 50 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CPF não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCpfNulo_DeveSerLancadaExcecao() {
		// Given
		String cpfNulo = null;
		contaDigitalPessoaFisicaInsercaoDto1.setCpf(cpfNulo);
		String mensagemEsperada = "CPF não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CPF não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCpfEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cpfEmBranco = "       ";
		contaDigitalPessoaFisicaInsercaoDto1.setCpf(cpfEmBranco);
		String mensagemEsperada = "CPF não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CPF com menos de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCpfComMenos11Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cpfCom10Caracteres = "1234567890";
		contaDigitalPessoaFisicaInsercaoDto1.setCpf(cpfCom10Caracteres);
		String mensagemEsperada = "CPF com menos de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CPF com mais de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCpfComMaisOnzeCaracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom12Caracteres = "123456789012";
		contaDigitalPessoaFisicaInsercaoDto1.setCpf(contaCom12Caracteres);
		String mensagemEsperada = "CPF com mais de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o nome completo não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoNulo_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoNulo = null;
		contaDigitalPessoaFisicaInsercaoDto1.setNomeCompleto(nomeCompletoNulo);
		String mensagemEsperada = "Nome completo não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o nome completo não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoEmBranco_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoEmBranco = "       ";
		contaDigitalPessoaFisicaInsercaoDto1.setNomeCompleto(nomeCompletoEmBranco);
		String mensagemEsperada = "Nome completo não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o nome completo com mais de 100 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoComMaisCemCaracteres_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoCom101Caracteres = "Fulano de Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal";
		contaDigitalPessoaFisicaInsercaoDto1.setNomeCompleto(nomeCompletoCom101Caracteres);
		String mensagemEsperada = "Nome completo com mais de 100 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a data de nascimento não informada deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComDataNascimentoNulo_DeveSerLancadaExcecao() {
		// Given
		LocalDate dataNascimentoNulo = null;
		contaDigitalPessoaFisicaInsercaoDto1.setDataNascimento(dataNascimentoNulo);
		String mensagemEsperada = "Data nascimento não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o nome completo da mãe não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoMaeNulo_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeNulo = null;
		contaDigitalPessoaFisicaInsercaoDto1.setNomeCompletoMae(nomeCompletoMaeNulo);
		String mensagemEsperada = "Nome completo da mãe não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o nome completo da mãe não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoMaeEmBranco_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeEmBranco = "       ";
		contaDigitalPessoaFisicaInsercaoDto1.setNomeCompletoMae(nomeCompletoMaeEmBranco);
		String mensagemEsperada = "Nome completo da mãe não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o nome completo da mãe com mais de 100 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComNomeCompletoMaeComMaisCemCaracteres_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeCom101Caracteres = "Fulana de Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal";
		contaDigitalPessoaFisicaInsercaoDto1.setNomeCompletoMae(nomeCompletoMaeCom101Caracteres);
		String mensagemEsperada = "Nome completo da mãe com mais de 100 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	// ----------------------------------------------------------- Alteração --------------------------------------------------------------------
	
	@DisplayName("Altera uma conta digital com sucesso quando nenhuma exceção for lançada e for retornado um objeto não nullo do tipo persistido")
	@Test
	void testAlteraContaDigital_ComSucesso_NenhumaExcecaoLancadaRetornadoObjetoNaoNulo() {
		// Given
		given(repository.save(any(ContaDigitalPessoaFisica.class))).willReturn(contaDigitalPessoaFisica2);
		
		// When & Then
		ContaDigitalPessoaFisica actual = assertDoesNotThrow(
				() -> service.alteraContaDigitalPessoaFisica(contaDigitalPessoaFisicaAlteracaoDto1),
				() -> "Não deve ser lançada nehuma exceção.");

		assertNotNull(actual, () -> "O objeto retornado do tipo " + ContaDigitalPessoaFisica.class.getSimpleName()
				+ " não pode ser nulo.");
	}

	@DisplayName("Quando tenta alterar conta digital com a agência não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComAgenciaNula_DeveSerLancadaExcecao() {
		// Given
		String agenciaNula = null;
		contaDigitalPessoaFisicaAlteracaoDto1.setAgencia(agenciaNula);
		String mensagemEsperada = "Agência não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quando tenta alterar conta digital com a agência não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComAgenciaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String agenciaEmBranco = "       ";
		contaDigitalPessoaFisicaAlteracaoDto1.setAgencia(agenciaEmBranco);
		String mensagemEsperada = "Agência não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a agência com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComAgenciaComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaCom9Caracteres = "123456789";
		contaDigitalPessoaFisicaAlteracaoDto1.setAgencia(agenciaCom9Caracteres);
		String mensagemEsperada = "Agência com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a agência com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComAgenciaComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String agenciaCom11Caracteres = "12345678901";
		contaDigitalPessoaFisicaAlteracaoDto1.setAgencia(agenciaCom11Caracteres);
		String mensagemEsperada = "Agência com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a conta não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComContaNula_DeveSerLancadaExcecao() {
		// Given
		String contaNula = null;
		contaDigitalPessoaFisicaAlteracaoDto1.setConta(contaNula);
		String mensagemEsperada = "Conta não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quando tenta alterar conta digital com a conta não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComContaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String contaEmBranco = "       ";
		contaDigitalPessoaFisicaAlteracaoDto1.setConta(contaEmBranco);
		String mensagemEsperada = "Conta não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a conta com menos de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComContaComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom9Caracteres = "123456789";
		contaDigitalPessoaFisicaAlteracaoDto1.setConta(contaCom9Caracteres);
		String mensagemEsperada = "Conta com menos de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a conta com mais de 10 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComContaComMaisDezCaracteres_DeveSerLancadaExcecao() {
		// Given
		String contaCom11Caracteres = "12345678901";
		contaDigitalPessoaFisicaAlteracaoDto1.setConta(contaCom11Caracteres);
		String mensagemEsperada = "Conta com mais de 10 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a senha não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComSenhaNula_DeveSerLancadaExcecao() {
		// Given
		String senhaNula = null;
		contaDigitalPessoaFisicaAlteracaoDto1.setSenha(senhaNula);
		String mensagemEsperada = "Senha não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a senha não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComSenhaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String senhaEmBranco = "       ";
		contaDigitalPessoaFisicaAlteracaoDto1.setSenha(senhaEmBranco);
		String mensagemEsperada = "Senha não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a senha com menos de 8 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComSenhaComMenosOitoCaracteres_DeveSerLancadaExcecao() {
		// Given
		String senhaCom7Caracteres = "123456@";
		contaDigitalPessoaFisicaAlteracaoDto1.setSenha(senhaCom7Caracteres);
		String mensagemEsperada = "Senha com menos de 8 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();
		
		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a senha com mais de 16 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComSenhaComMaisDezesseisCaracteres_DeveSerLancadaExcecao() {
		// Given
		String senhaCom17Caracteres = "1234567890123456@";
		contaDigitalPessoaFisicaAlteracaoDto1.setSenha(senhaCom17Caracteres);
		String mensagemEsperada = "Senha com mais de 16 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o telefone não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComTelefoneNulo_DeveSerLancadaExcecao() {
		// Given
		String telefoneNulo = null;
		contaDigitalPessoaFisicaAlteracaoDto1.setTelefone(telefoneNulo);
		String mensagemEsperada = "Telefone não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o telefone não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComTelefoneEmBranco_DeveSerLancadaExcecao() {
		// Given
		String telefoneEmBranco = "       ";
		contaDigitalPessoaFisicaAlteracaoDto1.setTelefone(telefoneEmBranco);
		String mensagemEsperada = "Telefone não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o telefone com mais de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComTelefoneComMaisOnzeCaracteres_DeveSerLancadaExcecao() {
		// Given
		String telefoneCom12Caracteres = "129456789012";
		contaDigitalPessoaFisicaAlteracaoDto1.setTelefone(telefoneCom12Caracteres);
		String mensagemEsperada = "Telefone com mais de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o e-mail não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComEmailNulo_DeveSerLancadaExcecao() {
		// Given
		String emailNulo = null;
		contaDigitalPessoaFisicaAlteracaoDto1.setEmail(emailNulo);
		String mensagemEsperada = "E-mail não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o e-mail não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComEmailEmBranco_DeveSerLancadaExcecao() {
		// Given
		String emailEmBranco = "       ";
		contaDigitalPessoaFisicaAlteracaoDto1.setEmail(emailEmBranco);
		String mensagemEsperada = "E-mail não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o e-mail sem o símbolo @ (arroba) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComEmailSemArroba_DeveSerLancadaExcecao() {
		// Given
		String emailEmBranco = "email123gmail.com";
		contaDigitalPessoaFisicaAlteracaoDto1.setEmail(emailEmBranco);
		String mensagemEsperada = "E-mail informado sem o símbolo @ (arroba).";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o e-mail com mais de 50 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComEmailComMaisCinquentaCaracteres_DeveSerLancadaExcecao() {
		// Given
		String emailCom51Caracteres = "enderecoemail.grande.grande.grande.grande@gmail.com";
		contaDigitalPessoaFisicaAlteracaoDto1.setEmail(emailCom51Caracteres);
		String mensagemEsperada = "E-mail com mais de 50 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CPF não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCpfNulo_DeveSerLancadaExcecao() {
		// Given
		String cpfNulo = null;
		contaDigitalPessoaFisicaAlteracaoDto1.setCpf(cpfNulo);
		String mensagemEsperada = "CPF não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CPF não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCpfEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cpfEmBranco = "       ";
		contaDigitalPessoaFisicaAlteracaoDto1.setCpf(cpfEmBranco);
		String mensagemEsperada = "CPF não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CPF com menos de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCpfComMenos11Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cpfCom10Caracteres = "1234567890";
		contaDigitalPessoaFisicaAlteracaoDto1.setCpf(cpfCom10Caracteres);
		String mensagemEsperada = "CPF com menos de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CPF com mais de 11 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCpfComMaisOnzeCaracteres_DeveSerLancadaExcecao() {
		// Given
		String cpfCom12Caracteres = "123456789012";
		contaDigitalPessoaFisicaAlteracaoDto1.setCpf(cpfCom12Caracteres);
		String mensagemEsperada = "CPF com mais de 11 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital sem que haja uma conta cadastrada com o CPF informado deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_SemQueHajaContaCadastradaComCpfInformado_DeveSerLancadaExcecao() {
		// Given
		String cpfSemContaDigitalAssociadoAEle = "89815127312";
		contaDigitalPessoaFisicaAlteracaoDto1.setCpf(cpfSemContaDigitalAssociadoAEle);
		String mensagemEsperada = "Não foi encontrada uma conta com o CPF informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o nome completo não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoNulo_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoNulo = null;
		contaDigitalPessoaFisicaAlteracaoDto1.setNomeCompleto(nomeCompletoNulo);
		String mensagemEsperada = "Nome completo não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o nome completo não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoEmBranco_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoEmBranco = "       ";
		contaDigitalPessoaFisicaAlteracaoDto1.setNomeCompleto(nomeCompletoEmBranco);
		String mensagemEsperada = "Nome completo não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o nome completo com mais de 100 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoComMaisCemCaracteres_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoCom101Caracteres = "Fulano de Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal";
		contaDigitalPessoaFisicaAlteracaoDto1.setNomeCompleto(nomeCompletoCom101Caracteres);
		String mensagemEsperada = "Nome completo com mais de 100 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a data de nascimento não informada deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComDataNascimentoNulo_DeveSerLancadaExcecao() {
		// Given
		LocalDate dataNascimentoNulo = null;
		contaDigitalPessoaFisicaAlteracaoDto1.setDataNascimento(dataNascimentoNulo);
		String mensagemEsperada = "Data nascimento não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o nome completo da mãe não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoMaeNulo_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeNulo = null;
		contaDigitalPessoaFisicaAlteracaoDto1.setNomeCompletoMae(nomeCompletoMaeNulo);
		String mensagemEsperada = "Nome completo da mãe não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o nome completo da mãe não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoMaeEmBranco_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeEmBranco = "       ";
		contaDigitalPessoaFisicaAlteracaoDto1.setNomeCompletoMae(nomeCompletoMaeEmBranco);
		String mensagemEsperada = "Nome completo da mãe não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o nome completo da mãe com mais de 100 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComNomeCompletoMaeComMaisCemCaracteres_DeveSerLancadaExcecao() {
		// Given
		String nomeCompletoMaeCom101Caracteres = "Fulana de Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal";
		contaDigitalPessoaFisicaAlteracaoDto1.setNomeCompletoMae(nomeCompletoMaeCom101Caracteres);
		String mensagemEsperada = "Nome completo da mãe com mais de 100 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	// ---------------------------------------------------------- Busca ----------------------------------------------------------
	
	@DisplayName("Quando tenta buscar conta digital pelo o CPF informado deve ser retornada da busca uma conta digital com o mesmo CPF.")
	@Test
	void testBuscaContaDigital_PeloCpfInformado_DeveSerRetornadaContaDigitalComMesmoCpf() {
		// Given
		String cpf = contaDigitalPessoaFisica1.getCpf();
		
		// When
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = service.buscaContaDigitalPeloCpf(cpf);
		
		// Then
		assertTrue(contaDigitalPessoaFisicaOptional.isPresent(),
				() -> "Não foi encontrada uma conta com o CPF informado.");
		assertEquals(cpf, contaDigitalPessoaFisicaOptional.get().getCpf(),
				() -> "O CPF informado na busca é diferente do CPF da conta digital retornada da busca.");
	}
	
	@DisplayName("Quando tenta buscar conta digital pelo CPF e nenhuma conta digital é encontrada não deve ser lançada uma exceção")
	@Test
	void testBuscaContaDigital_PeloCpfNenhumaContaDigitalEncontrada_NaoDeveSerLancadaUmaExcecao() {
		// Given
		String cpf = null;
		
		// When & Then
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = assertDoesNotThrow(
				() -> service.buscaContaDigitalPeloCpf(cpf), () -> "Não pode ser lançada nehuma exceção aqui,"
						+ " pois se for lançada pode causar algum problema onde não se esperava que fosse"
						+ " lançada alguma excessão.");
		
		assertTrue(contaDigitalPessoaFisicaOptional.isEmpty(),
				() -> "Se esperava que não fosse encontrada nenhuma conta, "
						+ "pois a pesquisada foi feita usando um CPF nulo, "
						+ "porém uma conta digital foi retornada da pesquisa, o que não deveria ocorrer.");
	}
	
	@DisplayName("Quando tenta buscar conta digital pela agência e conta "
			+ "deve ser retornada da busca uma conta digital com a mesma agência e conta.")
	@Test
	void testBuscaContaDigital_PelaAgenciaConta_DeveSerRetornadaContaDigitalComMesmaAgenciaConta() {
		// Given
		String agencia = contaDigitalPessoaFisica1.getAgencia();
		String conta = contaDigitalPessoaFisica1.getConta();
		given(repository.findByAgenciaAndConta(agencia, conta)).willReturn(Optional.of(contaDigitalPessoaFisica1));

		// When
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = service
				.buscaContaDigitalPelaAgenciaConta(agencia, conta);

		// Then
		assertTrue(contaDigitalPessoaFisicaOptional.isPresent(),
				() -> "Não foi encontrada uma conta digital com a agência e a conta que foram informadas.");

		ContaDigitalPessoaFisica contaDigitalPessoaFisica = contaDigitalPessoaFisicaOptional.get();

		assertEquals(agencia, contaDigitalPessoaFisica.getAgencia(),
				() -> "A agência informada na busca é diferente da agência da conta digital retornada da busca.");
		assertEquals(conta, contaDigitalPessoaFisica.getConta(),
				() -> "A conta informada na busca é diferente da conta da conta digital retornada da busca.");
	}
	
	@DisplayName("Quando tenta buscar conta digital pela agência e conta e nenhuma conta digital "
			+ "é encontrada não deve ser lançada uma exceção")
	@Test
	void testBuscaContaDigital_PelaAgenciaContaNenhumaContaDigitalEncontrada_NaoDeveSerLancadaUmaExcecao() {
		// Given
		String agencia = null;
		String conta = null;
		
		// When & Then
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = assertDoesNotThrow(
				() -> service.buscaContaDigitalPelaAgenciaConta(agencia, conta),
				() -> "Não pode ser lançada nehuma exceção aqui,"
						+ " pois se for lançada pode causar algum problema onde não se esperava que"
						+ " fosse lançada alguma excessão.");
		
		assertTrue(contaDigitalPessoaFisicaOptional.isEmpty(),
				() -> "Se esperava que não fosse encontrada nenhuma conta, "
						+ "pois a pesquisada foi feita usando uma agência nula e uma conta nula, "
						+ "porém uma conta digital foi retornada da pesquisa, o que não deveria ocorrer.");
	}
	
	// ---------------------------------------------------------- Remoção ----------------------------------------------------------
	
	@DisplayName("Na remoção da conta digital pelo CPF deve ser executado o metodo delete do repository")
	@Test
	void testRemocaoContaDigital_PeloCpf_DeveSerExecutadoMetodoDeleteDoRepository() {
		String cpf = contaDigitalPessoaFisica1.getCpf();
		willDoNothing().given(repository).delete(contaDigitalPessoaFisica1);
		
		service.removeContaDigitalPessoaFisica(cpf);
		
		verify(repository, times(1)).delete(contaDigitalPessoaFisica1);
	}
	
	@DisplayName("Quando tenta remover conta digital com o CPF não informado (string nula) deve ser lançada uma exceção")
	@Test
	void testRemoveContaDigital_ComCpfNulo_DeveSerLancadaExcecao() {
		String cpf = null;
		willDoNothing().given(repository).delete(contaDigitalPessoaFisica1);
		String mensagemEsperada = "Não foi encontrada uma conta com o CPF informado.";
		
		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoContaDigital(cpf);

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando busca conta digital para pessoa física com sucesso "
			+ "deve ser retornado um objeto com os dados da conta digital")
	@Test
	void testBuscaContaDigitalPessoaFisica_ComSucesso_DeveSerRetornadoObjetoDadosContaDigital() {
		// Given
		given(repository.findById(anyString())).willReturn(Optional.of(contaDigitalPessoaFisica1));

		// When
		ContaDigitalPessoaFisicaDTO1Busca actual = assertDoesNotThrow(
				() -> service.buscaContaDigitalPeloCpfComRespostaSemSenha(contaDigitalPessoaFisica1.getCpf()),
				() -> "Não deve ser lançada nehuma exceção.");

		// Then
		assertNotNull(actual, () -> "O objeto retornado não deve ser nulo.");
	}
	
	@DisplayName("Quando busca conta digital para pessoa física sem sucesso "
			+ "não deve ser retornado um objeto com os dados da conta digital")
	@Test
	void testBuscaContaDigitalPessoaFisica_SemSucesso_DeveSerRetornadoObjetoDadosContaDigital() {
		// Given
		given(repository.findById(anyString())).willReturn(Optional.ofNullable(null));

		// When
		ContaDigitalPessoaFisicaDTO1Busca actual = assertDoesNotThrow(
				() -> service.buscaContaDigitalPeloCpfComRespostaSemSenha(""),
				() -> "Não deve ser lançada nehuma exceção.");

		// Then
		assertNull(actual, () -> "O objeto retornado deve ser nulo.");
	}

	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital() {
		return assertThrows(ValidacaoException.class,
				() -> service.insereContaDigitalPessoaFisica(contaDigitalPessoaFisicaInsercaoDto1),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital() {
		return assertThrows(ValidacaoException.class,
				() -> service.alteraContaDigitalPessoaFisica(contaDigitalPessoaFisicaAlteracaoDto1),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoContaDigital(String cpf) {
		return assertThrows(ValidacaoException.class,
				() -> service.removeContaDigitalPessoaFisica(cpf),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
}
