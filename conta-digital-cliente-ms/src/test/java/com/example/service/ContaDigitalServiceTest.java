package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.exception.ValidacaoException;

public abstract class ContaDigitalServiceTest {
	
	protected static final String EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA = "A exceção do tipo esperado não foi lançada.";

	protected void confirmaSeExcecaoLancadaContemMensagemEsperada(String mensagemEsperada, ValidacaoException exception) {
		assertEquals(mensagemEsperada, exception.getMessage(),
				() -> "A mensagem presente na exceção lançada está incorreta.");
	}
}
