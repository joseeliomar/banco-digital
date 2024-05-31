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

import com.example.dto.ContaDigitalPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaJuridicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaJuridicaInsercaoDto;
import com.example.dto.EnderecoDto;
import com.example.exception.ValidacaoException;
import com.example.feignclient.EnderecoFeignClient;
import com.example.model.ContaDigitalPessoaJuridica;
import com.example.repository.ContaDigitalPessoaJuridicaRepository;

@ExtendWith(SpringExtension.class)
class ContaDigitalPessoaJuridicaServiceTest extends ContaDigitalServiceTest {
	
	@TestConfiguration
	static class BookingServiceTestConfiguration {

        @Bean
        ContaDigitalPessoaJuridicaService contaDigitalPessoaJuridicaService() {
			return new ContaDigitalPessoaJuridicaService();
		}
	}

	@Autowired
	private ContaDigitalPessoaJuridicaService service;
	
	@MockBean
	private ContaDigitalPessoaJuridicaRepository repository;
	
	@MockBean
	private EnderecoFeignClient enderecoFeignClient;
	
	private ContaDigitalPessoaJuridicaInsercaoDto contaDigitalPessoaJuridicaInsercaoDto1;
	
	private ContaDigitalPessoaJuridicaAlteracaoDto contaDigitalPessoaJuridicaAlteracaoDto1;
	
	private ContaDigitalPessoaJuridica contaDigitalPessoaJuridica1;
	
	private ContaDigitalPessoaJuridica contaDigitalPessoaJuridica2;
	
	private EnderecoDto enderecoDto;
	
	private Long codigoEnderecoExistente;
	
	@BeforeEach
	void setup() {
		// Given
		codigoEnderecoExistente = 1L;
		
		enderecoDto = new EnderecoDto(codigoEnderecoExistente);
		given(enderecoFeignClient.buscaEndereco(codigoEnderecoExistente)).willReturn(enderecoDto);
		
		String cnpj1 = "12345678990001";
		String cnpj2 = "12345678990002";
		contaDigitalPessoaJuridicaInsercaoDto1 = new ContaDigitalPessoaJuridicaInsercaoDto("0000000011", "1234567890", "12345678", "19980001234", "fulano@email.com", cnpj1, "Fábrica Tal");
				
		contaDigitalPessoaJuridica1 = new ContaDigitalPessoaJuridica(
				contaDigitalPessoaJuridicaInsercaoDto1.getAgencia(), 
				contaDigitalPessoaJuridicaInsercaoDto1.getConta(),
				contaDigitalPessoaJuridicaInsercaoDto1.getSenha(), 
				contaDigitalPessoaJuridicaInsercaoDto1.getTelefone(),
				contaDigitalPessoaJuridicaInsercaoDto1.getEmail(), 
				null, 
				null, 
				null,
				contaDigitalPessoaJuridicaInsercaoDto1.getCnpj(),
				contaDigitalPessoaJuridicaInsercaoDto1.getRazaoSocial());
		
		contaDigitalPessoaJuridicaAlteracaoDto1 = new ContaDigitalPessoaJuridicaAlteracaoDto("0000000011", "1234567890", "12345678",
				"19980001234", "fulano@email.com", codigoEnderecoExistente, cnpj2, "Fábrica Tal");
		
		contaDigitalPessoaJuridica2 = new ContaDigitalPessoaJuridica(
				contaDigitalPessoaJuridicaAlteracaoDto1.getAgencia(),
				contaDigitalPessoaJuridicaAlteracaoDto1.getConta(), 
				contaDigitalPessoaJuridicaAlteracaoDto1.getSenha(),
				contaDigitalPessoaJuridicaAlteracaoDto1.getTelefone(),
				contaDigitalPessoaJuridicaAlteracaoDto1.getEmail(),
				contaDigitalPessoaJuridicaAlteracaoDto1.getIdEndereco(), 
				null, 
				null,
				contaDigitalPessoaJuridicaAlteracaoDto1.getCnpj(),
				contaDigitalPessoaJuridicaAlteracaoDto1.getRazaoSocial());
		
		
		given(repository.findById(contaDigitalPessoaJuridicaInsercaoDto1.getCnpj())).willReturn(Optional.of(contaDigitalPessoaJuridica1));
		given(repository.findById(contaDigitalPessoaJuridicaAlteracaoDto1.getCnpj())).willReturn(Optional.of(contaDigitalPessoaJuridica2));
	}

	@DisplayName("Cria uma conta digital com sucesso quando nenhuma exceção for lançada e for retornado um objeto não nulo do tipo persistido")
	@Test
	void testCriaContaDigital_ComSucesso_NenhumaExcecaoLancadaRetornadoObjetoNaoNullo() {
		// Given
		given(repository.save(any(ContaDigitalPessoaJuridica.class))).willReturn(contaDigitalPessoaJuridica1);
		
		// When & Then
		ContaDigitalPessoaJuridica actual = assertDoesNotThrow(
				() -> service.insereContaDigitalPessoaJuridica(contaDigitalPessoaJuridicaInsercaoDto1),
				() -> "Não deve ser lançada nehuma exceção.");

		assertNotNull(actual, () -> "O objeto retornado do tipo " + ContaDigitalPessoaJuridica.class.getSimpleName()
				+ " não pode ser nulo.");
	}

	@DisplayName("Quando tenta criar conta digital com a agência não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComAgenciaNula_DeveSerLancadaExcecao() {
		// Given
		String agenciaNula = null;
		contaDigitalPessoaJuridicaInsercaoDto1.setAgencia(agenciaNula);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setAgencia(agenciaEmBranco);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setAgencia(agenciaCom9Caracteres);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setAgencia(agenciaCom11Caracteres);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setConta(contaNula);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setConta(contaEmBranco);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setConta(contaCom9Caracteres);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setConta(contaCom11Caracteres);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setSenha(senhaNula);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setSenha(senhaEmBranco);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setSenha(senhaCom7Caracteres);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setSenha(senhaCom17Caracteres);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setTelefone(telefoneNulo);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setTelefone(telefoneEmBranco);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setTelefone(telefoneCom12Caracteres);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setEmail(emailNulo);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setEmail(emailEmBranco);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setEmail(emailEmBranco);
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
		contaDigitalPessoaJuridicaInsercaoDto1.setEmail(emailCom51Caracteres);
		String mensagemEsperada = "E-mail com mais de 50 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CNPJ não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCnpjNulo_DeveSerLancadaExcecao() {
		// Given
		String cnpjNulo = null;
		contaDigitalPessoaJuridicaInsercaoDto1.setCnpj(cnpjNulo);
		String mensagemEsperada = "CNPJ não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CNPJ não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCnpjEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cnpjEmBranco = "       ";
		contaDigitalPessoaJuridicaInsercaoDto1.setCnpj(cnpjEmBranco);
		String mensagemEsperada = "CNPJ não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CNPJ com menos de 14 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCnpjComMenos14Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cnpjCom13Caracteres = "1234567899000";
		contaDigitalPessoaJuridicaInsercaoDto1.setCnpj(cnpjCom13Caracteres);
		String mensagemEsperada = "CNPJ com menos de 14 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CNPJ com mais de 14 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComCnpjComMais14Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cnpjCom15Caracteres = "123456789900001";
		contaDigitalPessoaJuridicaInsercaoDto1.setCnpj(cnpjCom15Caracteres);
		String mensagemEsperada = "CNPJ com mais de 14 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a razão social não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComRazaoSocialNula_DeveSerLancadaExcecao() {
		// Given
		String razaoSocialNula = null;
		contaDigitalPessoaJuridicaInsercaoDto1.setRazaoSocial(razaoSocialNula);
		String mensagemEsperada = "Razão social não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a razão social não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComRazaoSocialEmBranco_DeveSerLancadaExcecao() {
		// Given
		String razaoSocialEmBranco = "       ";
		contaDigitalPessoaJuridicaInsercaoDto1.setRazaoSocial(razaoSocialEmBranco);
		String mensagemEsperada = "Razão social não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com a razão social com mais de 144 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaContaDigital_ComRazaoSocialComMais144Caracteres_DeveSerLancadaExcecao() {
		// Given
		String razaoSocialCom145Caracteres = "Fábrica Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal T";
		contaDigitalPessoaJuridicaInsercaoDto1.setRazaoSocial(razaoSocialCom145Caracteres);
		String mensagemEsperada = "Razão social com mais de 144 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	// ----------------------------------------------------------- Alteração --------------------------------------------------------------------
	
	@DisplayName("Altera uma conta digital com sucesso quando nenhuma exceção for lançada e for retornado um objeto não nulo do tipo persistido")
	@Test
	void testAlteraContaDigital_ComSucesso_NenhumaExcecaoLancadaRetornadoObjetoNaoNulo() {
		// Given
		given(repository.save(any(ContaDigitalPessoaJuridica.class))).willReturn(contaDigitalPessoaJuridica2);
		
		// When & Then
		ContaDigitalPessoaJuridica actual = assertDoesNotThrow(
				() -> service.alteraContaDigitalPessoaJuridica(contaDigitalPessoaJuridicaAlteracaoDto1),
				() -> "Não deve ser lançada nehuma exceção.");

		assertNotNull(actual, () -> "O objeto retornado do tipo " + ContaDigitalPessoaJuridica.class.getSimpleName()
				+ " não pode ser nulo.");
	}

	@DisplayName("Quando tenta alterar conta digital com a agência não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComAgenciaNula_DeveSerLancadaExcecao() {
		// Given
		String agenciaNula = null;
		contaDigitalPessoaJuridicaAlteracaoDto1.setAgencia(agenciaNula);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setAgencia(agenciaEmBranco);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setAgencia(agenciaCom9Caracteres);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setAgencia(agenciaCom11Caracteres);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setConta(contaNula);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setConta(contaEmBranco);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setConta(contaCom9Caracteres);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setConta(contaCom11Caracteres);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setSenha(senhaNula);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setSenha(senhaEmBranco);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setSenha(senhaCom7Caracteres);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setSenha(senhaCom17Caracteres);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setTelefone(telefoneNulo);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setTelefone(telefoneEmBranco);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setTelefone(telefoneCom12Caracteres);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setEmail(emailNulo);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setEmail(emailEmBranco);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setEmail(emailEmBranco);
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
		contaDigitalPessoaJuridicaAlteracaoDto1.setEmail(emailCom51Caracteres);
		String mensagemEsperada = "E-mail com mais de 50 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CNPJ não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCnpjNulo_DeveSerLancadaExcecao() {
		// Given
		String cnpjNulo = null;
		contaDigitalPessoaJuridicaAlteracaoDto1.setCnpj(cnpjNulo);
		String mensagemEsperada = "CNPJ não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CNPJ não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCnpjEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cnpjEmBranco = "       ";
		contaDigitalPessoaJuridicaAlteracaoDto1.setCnpj(cnpjEmBranco);
		String mensagemEsperada = "CNPJ não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CNPJ com menos de 14 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCnpjComMenos14Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cnpjCom13Caracteres = "1234567899000";
		contaDigitalPessoaJuridicaAlteracaoDto1.setCnpj(cnpjCom13Caracteres);
		String mensagemEsperada = "CNPJ com menos de 14 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CNPJ com mais de 14 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComCnpjComMais14Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cnpjCom15Caracteres = "123456789900001";
		contaDigitalPessoaJuridicaAlteracaoDto1.setCnpj(cnpjCom15Caracteres);
		String mensagemEsperada = "CNPJ com mais de 14 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital sem que haja uma conta cadastrada com o CNPJ informado deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_SemQueHajaContaCadastradaComCnpjInformado_DeveSerLancadaExcecao() {
		// Given
		String cnpjSemContaDigitalAssociadoAEle = "12345678999999";
		contaDigitalPessoaJuridicaAlteracaoDto1.setCnpj(cnpjSemContaDigitalAssociadoAEle);
		String mensagemEsperada = "Não foi encontrada uma conta com o CNPJ informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a razão social não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComRazaoSocialNula_DeveSerLancadaExcecao() {
		// Given
		String razaoSocialNula = null;
		contaDigitalPessoaJuridicaAlteracaoDto1.setRazaoSocial(razaoSocialNula);
		String mensagemEsperada = "Razão social não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a razão social não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComRazaoSocialEmBranco_DeveSerLancadaExcecao() {
		// Given
		String razaoSocialEmBranco = "       ";
		contaDigitalPessoaJuridicaAlteracaoDto1.setRazaoSocial(razaoSocialEmBranco);
		String mensagemEsperada = "Razão social não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com a razão social com mais de 144 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraContaDigital_ComRazaoSocialComMais144Caracteres_DeveSerLancadaExcecao() {
		// Given
		String razaoSocialCom145Caracteres = "Fábrica Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal Tal T";
		contaDigitalPessoaJuridicaAlteracaoDto1.setRazaoSocial(razaoSocialCom145Caracteres);
		String mensagemEsperada = "Razão social com mais de 144 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	// ---------------------------------------------------------- Busca ----------------------------------------------------------
	
	@DisplayName("Quando tenta buscar conta digital pelo o CNPJ informado deve ser retornada da busca uma conta digital com o mesmo CNPJ.")
	@Test
	void testBuscaContaDigital_PeloCnpjInformado_DeveSerRetornadaContaDigitalComMesmoCnpj() {
		// Given
		String cnpj = contaDigitalPessoaJuridica1.getCnpj();
		
		// When
		Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional = service.buscaContaDigitalPeloCnpj(cnpj);
		
		// Then
		assertTrue(contaDigitalPessoaJuridicaOptional.isPresent(),
				() -> "Não foi encontrada uma conta com o CNPJ informado.");
		assertEquals(cnpj, contaDigitalPessoaJuridicaOptional.get().getCnpj(),
				() -> "O CNPJ informado na busca é diferente do CNPJ da conta digital retornada da busca.");
	}
	
	@DisplayName("Quando tenta buscar conta digital pelo CNPJ e nenhuma conta digital é encontrada não deve ser lançada uma exceção")
	@Test
	void testBuscaContaDigital_PeloCnpjNenhumaContaDigitalEncontrada_NaoDeveSerLancadaUmaExcecao() {
		// Given
		String cnpj = null;
		
		// When & Then
		Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional = assertDoesNotThrow(
				() -> service.buscaContaDigitalPeloCnpj(cnpj), () -> "Não pode ser lançada nehuma exceção aqui,"
						+ " pois se for lançada pode causar algum problema onde não se esperava que fosse"
						+ " lançada alguma excessão.");
		
		assertTrue(contaDigitalPessoaJuridicaOptional.isEmpty(),
				() -> "Se esperava que não fosse encontrada nenhuma conta, "
						+ "pois a pesquisada foi feita usando um CNPJ nulo, "
						+ "porém uma conta digital foi retornada da pesquisa, o que não deveria ocorrer.");
	}
	
	@DisplayName("Quando tenta buscar conta digital pela agência e conta "
			+ "deve ser retornada da busca uma conta digital com a mesma agência e conta.")
	@Test
	void testBuscaContaDigital_PelaAgenciaConta_DeveSerRetornadaContaDigitalComMesmaAgenciaConta() {
		// Given
		String agencia = contaDigitalPessoaJuridica1.getAgencia();
		String conta = contaDigitalPessoaJuridica1.getConta();
		given(repository.findByAgenciaAndConta(agencia, conta)).willReturn(Optional.of(contaDigitalPessoaJuridica1));

		// When
		Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional = service
				.buscaContaDigitalPelaAgenciaConta(agencia, conta);

		// Then
		assertTrue(contaDigitalPessoaJuridicaOptional.isPresent(),
				() -> "Não foi encontrada uma conta digital com a agência e a conta que foram informadas.");

		ContaDigitalPessoaJuridica contaDigitalPessoaJuridica = contaDigitalPessoaJuridicaOptional.get();

		assertEquals(agencia, contaDigitalPessoaJuridica.getAgencia(),
				() -> "A agência informada na busca é diferente da agência da conta digital retornada da busca.");
		assertEquals(conta, contaDigitalPessoaJuridica.getConta(),
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
		Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional = assertDoesNotThrow(
				() -> service.buscaContaDigitalPelaAgenciaConta(agencia, conta),
				() -> "Não pode ser lançada nehuma exceção aqui,"
						+ " pois se for lançada pode causar algum problema onde não se esperava que"
						+ " fosse lançada alguma excessão.");
		
		assertTrue(contaDigitalPessoaJuridicaOptional.isEmpty(),
				() -> "Se esperava que não fosse encontrada nenhuma conta, "
						+ "pois a pesquisada foi feita usando uma agência nula e uma conta nula, "
						+ "porém uma conta digital foi retornada da pesquisa, o que não deveria ocorrer.");
	}
	
	// ---------------------------------------------------------- Remoção ----------------------------------------------------------
	
	@DisplayName("Na remoção da conta digital pelo CNPJ deve ser executado o metodo delete do repository")
	@Test
	void testRemocaoContaDigital_PeloCnpj_DeveSerExecutadoMetodoDeleteDoRepository() {
		String cnpj = contaDigitalPessoaJuridica1.getCnpj();
		willDoNothing().given(repository).delete(contaDigitalPessoaJuridica1);
		
		service.removeContaDigitalPessoaJuridica(cnpj);
		
		verify(repository, times(1)).delete(contaDigitalPessoaJuridica1);
	}
	
	@DisplayName("Quando tenta remover conta digital com o CNPJ não informado (string nula) deve ser lançada uma exceção")
	@Test
	void testRemoveContaDigital_ComCnpjNulo_DeveSerLancadaExcecao() {
		String cnpj = null;
		willDoNothing().given(repository).delete(contaDigitalPessoaJuridica1);
		String mensagemEsperada = "Não foi encontrada uma conta com o CNPJ informado.";
		
		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoContaDigital(cnpj);

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando busca conta digital para pessoa física com sucesso "
			+ "deve ser retornado um objeto com os dados da conta digital")
	@Test
	void testBuscaContaDigitalPessoaJuridica_ComSucesso_DeveSerRetornadoObjetoDadosContaDigital() {
		// Given
		given(repository.findById(anyString())).willReturn(Optional.of(contaDigitalPessoaJuridica1));

		// When
		ContaDigitalPessoaJuridicaDTO1Busca actual = assertDoesNotThrow(
				() -> service.buscaContaDigitalPeloCnpjComRespostaSemSenha(contaDigitalPessoaJuridica1.getCnpj()),
				() -> "Não deve ser lançada nehuma exceção.");

		// Then
		assertNotNull(actual, () -> "O objeto retornado não deve ser nulo.");
	}
	
	@DisplayName("Quando busca conta digital para pessoa física sem sucesso "
			+ "não deve ser retornado um objeto com os dados da conta digital")
	@Test
	void testBuscaContaDigitalPessoaJuridica_SemSucesso_DeveSerRetornadoObjetoDadosContaDigital() {
		// Given
		given(repository.findById(anyString())).willReturn(Optional.ofNullable(null));

		// When
		ContaDigitalPessoaJuridicaDTO1Busca actual = assertDoesNotThrow(
				() -> service.buscaContaDigitalPeloCnpjComRespostaSemSenha(""),
				() -> "Não deve ser lançada nehuma exceção.");

		// Then
		assertNull(actual, () -> "O objeto retornado deve ser nulo.");
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital() {
		return assertThrows(ValidacaoException.class,
				() -> service.insereContaDigitalPessoaJuridica(contaDigitalPessoaJuridicaInsercaoDto1),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoContaDigital() {
		return assertThrows(ValidacaoException.class,
				() -> service.alteraContaDigitalPessoaJuridica(contaDigitalPessoaJuridicaAlteracaoDto1),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoContaDigital(String cnpj) {
		return assertThrows(ValidacaoException.class,
				() -> service.removeContaDigitalPessoaJuridica(cnpj),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
}
