package com.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.example.dto.EnderecoAlteracaoDto;
import com.example.dto.EnderecoBuscaDto1;
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
	
	private EnderecoInsercaoDto enderecoInsercaoDto1;
	private Endereco endereco1;
	private EnderecoAlteracaoDto enderecoAlteracaoDto1;
	private Endereco endereco2;
	
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
		
		endereco1.setId(1L);
		
		enderecoAlteracaoDto1 = new EnderecoAlteracaoDto(1L, "Rua tal", 200, "Bairro tal", "Rio de Janeiro", UnidadeFederativa.RJ, "14444444");
		
		endereco2 = new Endereco(
				enderecoAlteracaoDto1.getRua(),
				enderecoAlteracaoDto1.getNumero(),
				enderecoAlteracaoDto1.getBairro(),
				enderecoAlteracaoDto1.getMunicipio(),
				enderecoAlteracaoDto1.getUnidadeFederativa(),
				enderecoAlteracaoDto1.getCep(),
				null,
				null);
		
		endereco2.setId(enderecoAlteracaoDto1.getId());
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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta criar conta digital com o CEP com menos de 8 dígitos deve ser lançada uma exceção.")
	@Test
	void testCriaEndereco_ComCepComMenos8Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cepCom7Caracteres = "1234567";
		enderecoInsercaoDto1.setCep(cepCom7Caracteres);
		String mensagemEsperada = "CEP com menos de 8 dígitos.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

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
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando altera um endereço com sucesso, nenhuma exceção pode ser "
			+ "lançada e deve ser retornado um objeto não nulo do tipo persistido")
	@Test
	void testAlteraEndereco_ComSucesso_NenhumaExcecaoDeveSerLancadaDeveSerRetornadoObjetoNaoNullo() {
		// Given
		given(enderecoRepository.findById(endereco2.getId())).willReturn(Optional.of(endereco2));
		given(enderecoRepository.save(any(Endereco.class))).willReturn(endereco2);
		
		// When & Then
		Endereco actual = assertDoesNotThrow(
				() -> enderecoService.alteraEndereco(enderecoAlteracaoDto1),
				() -> "Não deve ser lançada nehuma exceção.");

		verify(enderecoRepository).save(any(Endereco.class));
		assertNotNull(actual, () -> "O objeto retornado do tipo " + Endereco.class.getSimpleName()
				+ " não pode ser nulo.");
	}
	
	@DisplayName("Quando tenta alterar endereço com a rua não informada (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComRuaNula_DeveSerLancadaExcecao() {
		// Given
		String ruaNula = null;
		enderecoAlteracaoDto1.setRua(ruaNula);
		String mensagemEsperada = "Rua não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar endereço com a rua não informada (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComRuaEmBranco_DeveSerLancadaExcecao() {
		// Given
		String ruaEmBranco = "       ";
		enderecoAlteracaoDto1.setRua(ruaEmBranco);
		String mensagemEsperada = "Rua não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar endereço com a rua com mais de 80 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComRuaComMais80Caracteres_DeveSerLancadaExcecao() {
		// Given
		String ruaCom83Caracteres = "Rua tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal tal";
		enderecoAlteracaoDto1.setRua(ruaCom83Caracteres);
		String mensagemEsperada = "Rua com mais de 80 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar endereço com o número não informado deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComNumeroNulo_DeveSerLancadaExcecao() {
		// Given
		Integer numeroNulo = null;
		enderecoAlteracaoDto1.setNumero(numeroNulo);
		String mensagemEsperada = "Número não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar endereço com o número com mais de 4 dígitos deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComNumeroComMais4Digitos_DeveSerLancadaExcecao() {
		// Given
		Integer numeroCom5Digitos = 12345;
		enderecoAlteracaoDto1.setNumero(numeroCom5Digitos);
		String mensagemEsperada = "Número com mais de 4 dígitos.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar endereço com o bairro não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComBairroNulo_DeveSerLancadaExcecao() {
		// Given
		String bairroNulo = null;
		enderecoAlteracaoDto1.setBairro(bairroNulo);
		String mensagemEsperada = "Bairro não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar endereço com o bairro não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComBairroEmBranco_DeveSerLancadaExcecao() {
		// Given
		String bairroEmBranco = "       ";
		enderecoAlteracaoDto1.setBairro(bairroEmBranco);
		String mensagemEsperada = "Bairro não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar endereço com o bairro com mais de 40 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComBairroComMais80Caracteres_DeveSerLancadaExcecao() {
		// Given
		String bairroCom42Caracteres = "Bairro tal tal tal tal tal tal tal tal tal";
		enderecoAlteracaoDto1.setBairro(bairroCom42Caracteres);
		String mensagemEsperada = "Bairro com mais de 40 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar endereço com o município não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComMunicipioNulo_DeveSerLancadaExcecao() {
		// Given
		String municipioNulo = null;
		enderecoAlteracaoDto1.setMunicipio(municipioNulo);
		String mensagemEsperada = "Município não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar endereço com o município não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComMunicipioEmBranco_DeveSerLancadaExcecao() {
		// Given
		String municipioEmBranco = "       ";
		enderecoAlteracaoDto1.setMunicipio(municipioEmBranco);
		String mensagemEsperada = "Município não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar endereço com o município com mais de 40 caracteres deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComMunicipioComMais80Caracteres_DeveSerLancadaExcecao() {
		// Given
		String municipioCom41Caracteres = "Município tal tal tal tal tal tal tal tal";
		enderecoAlteracaoDto1.setMunicipio(municipioCom41Caracteres);
		String mensagemEsperada = "Município com mais de 40 caracteres.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar endereço com a unidade federativa não informada deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComUnidadeFederativaNula_DeveSerLancadaExcecao() {
		// Given
		UnidadeFederativa unidadeFederativaNula = null;
		enderecoAlteracaoDto1.setUnidadeFederativa(unidadeFederativaNula);
		String mensagemEsperada = "Unidade federativa não informada.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CEP não informado (string nula) deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComCepNulo_DeveSerLancadaExcecao() {
		// Given
		String cepNulo = null;
		enderecoAlteracaoDto1.setCep(cepNulo);
		String mensagemEsperada = "CEP não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}

	@DisplayName("Quando tenta alterar conta digital com o CEP não informado (string em branco) deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComCepEmBranco_DeveSerLancadaExcecao() {
		// Given
		String cepEmBranco = "       ";
		enderecoAlteracaoDto1.setCep(cepEmBranco);
		String mensagemEsperada = "CEP não informado.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CEP com menos de 8 dígitos deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComCepComMenos10Caracteres_DeveSerLancadaExcecao() {
		// Given
		String cepCom7Caracteres = "1234567";
		enderecoAlteracaoDto1.setCep(cepCom7Caracteres);
		String mensagemEsperada = "CEP com menos de 8 dígitos.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando tenta alterar conta digital com o CEP com mais de 8 dígitos deve ser lançada uma exceção.")
	@Test
	void testAlteraEndereco_ComCepComMais8Digitos_DeveSerLancadaExcecao() {
		// Given
		String cepCom9Digitos = "123456789";
		enderecoAlteracaoDto1.setCep(cepCom9Digitos);
		String mensagemEsperada = "CEP com mais de 8 dígitos.";

		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco();

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Na remoção do endereço pelo ID deve ser executado o método delete do repository")
	@Test
	void testRemocaoEndereco_PeloId_DeveSerExecutadoMetodoDeleteDoRepository() {
		Long id = endereco1.getId();
		given(enderecoRepository.findById(id)).willReturn(Optional.of(endereco1));
		willDoNothing().given(enderecoRepository).delete(endereco1);
		
		enderecoService.removeEndereco(id);
		
		verify(enderecoRepository).delete(endereco1);
	}
	
	@DisplayName("Quando tenta remover endereço com o ID não informado deve ser lançada uma exceção")
	@Test
	void testRemoveEndereco_ComIdNulo_DeveSerLancadaExcecao() {
		Long id = null;
		given(enderecoRepository.findById(any())).willReturn(Optional.ofNullable(null));
		String mensagemEsperada = "Não foi encontrado um endereço com o código informado.";
		
		// When & Then
		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoEndereco(id);

		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
	}
	
	@DisplayName("Quando busca endereço com sucesso "
			+ "deve ser retornado um objeto com os dados do endereço")
	@Test
	void testBuscaEndereco_ComSucesso_DeveSerRetornadoObjetoDadosEndereco() {
		// Given
		given(enderecoRepository.findById(any(Long.class))).willReturn(Optional.of(endereco1));

		// When
		EnderecoBuscaDto1 actual = assertDoesNotThrow(
				() -> enderecoService.buscaEnderecoCompleto(endereco1.getId()),
				() -> "Não deve ser lançada nehuma exceção.");

		// Then
		assertNotNull(actual, () -> "O objeto retornado não deve ser nulo.");
	}
	
	@DisplayName("Quando busca endereço sem sucesso "
			+ "não deve ser retornado um objeto com os dados do endereço")
	@Test
	void testBuscaEndereco_SemSucesso_DeveSerRetornadoObjetoDadosEndereco() {
		// Given
		given(enderecoRepository.findById(any())).willReturn(Optional.ofNullable(null));

		// When
		EnderecoBuscaDto1 actual = assertDoesNotThrow(
				() -> enderecoService.buscaEnderecoCompleto(null),
				() -> "Não deve ser lançada nehuma exceção.");

		// Then
		assertNull(actual, () -> "O objeto retornado deve ser nulo.");
	}
	
	@DisplayName("Quando busca endereços com sucesso o objeto retornado não deve ser nulo")
	@Test
	void testBuscaEnderecos_ComSucesso_ObjetoRetornadoNaoDeveSerNulo() {
		// Given
		Page<Endereco> enderecos = new PageImpl<>(Arrays.asList(endereco1));
		Pageable pageable = PageRequest.of(0, 2, Direction.ASC, "rua", "numero", "bairro", "municipio",
				"unidadeFederativa", "cep");
		given(enderecoRepository.findAll(pageable)).willReturn(enderecos);

		// When
		Page<Endereco> actual = assertDoesNotThrow(
				() -> enderecoService.buscaEnderecos(pageable),
				() -> "Não deve ser lançada nehuma exceção.");

		// Then
		assertNotNull(actual, () -> "O objeto retornado não deve ser nulo.");
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoEndereco() {
		return assertThrows(ValidacaoException.class,
				() -> enderecoService.insereEndereco(enderecoInsercaoDto1),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoAlteracaoEndereco() {
		return assertThrows(ValidacaoException.class,
				() -> enderecoService.alteraEndereco(enderecoAlteracaoDto1),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private ValidacaoException confirmaSeSeraLancadaExcecaoTipoEsperadoRemocaoEndereco(Long idEndereco) {
		return assertThrows(ValidacaoException.class,
				() -> enderecoService.removeEndereco(idEndereco),
				() -> EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA);
	}
	
	private void confirmaSeExcecaoLancadaContemMensagemEsperada(String mensagemEsperada, ValidacaoException exception) {
		assertEquals(mensagemEsperada, exception.getMessage(),
				() -> "A mensagem presente na exceção lançada está incorreta.");
	}
}
