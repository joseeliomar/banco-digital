package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.EnderecoInsercaoDto;
import com.example.enumeration.UnidadeFederativa;
import com.example.exception.ValidacaoException;
import com.example.model.Endereco;
import com.example.repository.EnderecoRepository;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {
	
	private static final String EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA = "A exceção do tipo esperado não foi lançada.";
	
	@InjectMocks
	private EnderecoService enderecoService;
	
	@Mock
	private EnderecoRepository enderecoRepository;
	
	private Endereco endereco1;
	private EnderecoInsercaoDto enderecoInsercaoDto1;
	
	@BeforeEach
	private void setup() {
		enderecoInsercaoDto1 = new EnderecoInsercaoDto("Rua tal", 100, "Bairro tal", "São Paulo", UnidadeFederativa.SP, "13444444");
		
		endereco1 = new Endereco(
				enderecoInsercaoDto1.getRua(),
				enderecoInsercaoDto1.getNumero(),
				enderecoInsercaoDto1.getBairro(),
				enderecoInsercaoDto1.getMunicipio(),
				enderecoInsercaoDto1.getUnidadeFederativa(),
				enderecoInsercaoDto1.getCep(),
				null,
				null);
	}

	@DisplayName("Quando cria um endereço com sucesso, nenhuma exceção pode ser "
			+ "lançada e deve ser retornado um objeto não nulo do tipo persistido")
	@Test
	void testCriaEndereco_ComSucesso_NenhumaExcecaoDeveSerLancadaDeveSerRetornadoObjetoNaoNullo() {
		// Given
		given(enderecoRepository.save(any(Endereco.class))).willReturn(endereco1);
		
		// When & Then
		Endereco actual = assertDoesNotThrow(
				() -> enderecoService.insereEndereco(enderecoInsercaoDto1),
				() -> "Não deve ser lançada nehuma exceção.");

		verify(enderecoRepository).save(any(Endereco.class));
		assertNotNull(actual, () -> "O objeto retornado do tipo " + Endereco.class.getSimpleName()
				+ " não pode ser nulo.");
	}
	
	@DisplayName("Quando tenta criar endereço com a rua não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComRuaNula_DeveSerLancadaExcecao() {
		// Given
		String ruaNula = null;
		enderecoInsercaoDto1.setRua(ruaNula);
		String mensagemEsperada = "Rua não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar endereço com a rua não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComRuaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String ruaEmBranco = "       ";
		enderecoInsercaoDto1.setRua(ruaEmBranco);
		String mensagemEsperada = "Rua não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar endereço com a rua com mais de 80 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComRuaComMais80Caracteres_DeveSerLancadaExcecao() {
		// Given
		String ruaCom83Caracteres = "Rua tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal";
		enderecoInsercaoDto1.setRua(ruaCom83Caracteres);
		String mensagemEsperada = "Rua com mais de 80 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar endereço com o número não informado deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComNumeroNulo_DeveSerLancadaExcecao() {
		// Given
		Integer numeroNulo = null;
		enderecoInsercaoDto1.setNumero(numeroNulo);
		String mensagemEsperada = "Número não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar endereço com o número com mais de 4 dígitos deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComNumeroComMais4Digitos_DeveSerLancadaExcecao() {
		// Given
		Integer numeroCom5Digitos = 12345;
		enderecoInsercaoDto1.setNumero(numeroCom5Digitos);
		String mensagemEsperada = "Número com mais de 4 dígitos.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar endereço com o bairro não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComBairroNulo_DeveSerLancadaExcecao() {
		// Given
		String bairroNulo = null;
		enderecoInsercaoDto1.setBairro(bairroNulo);
		String mensagemEsperada = "Bairro não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar endereço com o bairro não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComBairroEmBranco_DeveSerLancadaExcecao() {
		// Given
		String bairroEmBranco = "       ";
		enderecoInsercaoDto1.setBairro(bairroEmBranco);
		String mensagemEsperada = "Bairro não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar endereço com o bairro com mais de 40 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComBairroComMais80Caracteres_DeveSerLancadaExcecao() {
		// Given
		String bairroCom42Caracteres = "Bairro tal tal tal tal tal tal tal tal tal";
		enderecoInsercaoDto1.setBairro(bairroCom42Caracteres);
		String mensagemEsperada = "Bairro com mais de 40 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar endereço com o município não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComMunicipioNulo_DeveSerLancadaExcecao() {
		// Given
		String municipioNulo = null;
		enderecoInsercaoDto1.setMunicipio(municipioNulo);
		String mensagemEsperada = "Município não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar endereço com o município não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComMunicipioEmBranco_DeveSerLancadaExcecao() {
		// Given
		String municipioEmBranco = "       ";
		enderecoInsercaoDto1.setMunicipio(municipioEmBranco);
		String mensagemEsperada = "Município não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar endereço com o município com mais de 40 caracteres deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComMunicipioComMais80Caracteres_DeveSerLancadaExcecao() {
		// Given
		String municipioCom41Caracteres = "Município tal tal tal tal tal tal tal tal";
		enderecoInsercaoDto1.setMunicipio(municipioCom41Caracteres);
		String mensagemEsperada = "Município com mais de 40 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar endereço com a unidade federativa não informada deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComUnidadeFederativaNula_DeveSerLancadaExcecao() {
		// Given
		UnidadeFederativa unidadeFederativaNula = null;
		enderecoInsercaoDto1.setUnidadeFederativa(unidadeFederativaNula);
		String mensagemEsperada = "Unidade federativa não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CEP não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComCepNulo_DeveSerLancadaExcecao() {
		// Given
		String cepNulo = null;
		enderecoInsercaoDto1.setCep(cepNulo);
		String mensagemEsperada = "CEP não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quando tenta criar conta digital com o CEP não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComCepEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cepEmBranco = "       ";
		enderecoInsercaoDto1.setCep(cepEmBranco);
		String mensagemEsperada = "CEP não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CEP com menos de 8 dígitos deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComCepComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cepCom7Caracteres = "1234567";
		enderecoInsercaoDto1.setCep(cepCom7Caracteres);
		String mensagemEsperada = "CEP com menos de 8 dígitos.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CEP com mais de 8 dígitos deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComCepComMais8Digitos_DeveSerLancadaExcecao() {
		// Given
		String cepCom9Digitos = "123456789";
		enderecoInsercaoDto1.setCep(cepCom9Digitos);
		String mensagemEsperada = "CEP com mais de 8 dígitos.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital() {
		return assertThrows(ValidacaoException.class,
				() -> enderecoService.insereEndereco(enderecoInsercaoDto1),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private void confirmaSeExcecaoLancadaContemMensagemEsperada(String mensagemEsperada, ValidacaoException exception) {
		assertEquals(mensagemEsperada, exception.getMessage(),
				() -> "A mensagem presente na exceção lançada está incorreta.");
	}
}
