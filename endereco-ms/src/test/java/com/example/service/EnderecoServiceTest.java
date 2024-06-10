package com.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.dto.EnderecoInsercaoDto;
import com.example.enumeration.UnidadeFederativa;
import com.example.model.Endereco;
import com.example.repository.EnderecoRepository;

@ExtendWith(SpringExtension.class)
class EnderecoServiceTest {
	
	@Autowired
	private EnderecoService enderecoService;
	
	@MockBean
	private EnderecoRepository enderecoRepository;
	
	private Endereco endereco1;
	private EnderecoInsercaoDto enderecoInsercaoDto1;
	
	@BeforeEach
	private void setup() {
		enderecoInsercaoDto1 = new EnderecoInsercaoDto("Rua tal", 100, "Bairro tal", "São Paulo", UnidadeFederativa.SP, "13444444");
		
		endereco1 = new Endereco(
				enderecoInsercaoDto1.rua(),
				enderecoInsercaoDto1.numero(),
				enderecoInsercaoDto1.bairro(),
				enderecoInsercaoDto1.municipio(),
				enderecoInsercaoDto1.unidadeFederativa(),
				enderecoInsercaoDto1.cep(),
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

		assertNotNull(actual, () -> "O objeto retornado do tipo " + Endereco.class.getSimpleName()
				+ " não pode ser nulo.");
	}
	
//	@DisplayName("Quando tenta criar endereço com a rua não informada (string nula) deve ser lançada uma exceção.")
//	@Test
//	void testCriaEndereco_ComRuaNula_DeveSerLancadaExcecao() {
//		// Given
//		String ruaNula = null;
//		enderecoInsercaoDto1.setRua(ruaNula);
//		String mensagemEsperada = "Rua não informada.";
//
//		// When & Then
//		ValidacaoException exception = confirmaSeSeraLancadaExcecaoTipoEsperadoCriacaoContaDigital();
//
//		confirmaSeExcecaoLancadaContemMensagemEsperada(mensagemEsperada, exception);
//	}

}
