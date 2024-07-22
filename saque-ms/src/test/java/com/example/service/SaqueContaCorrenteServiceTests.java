package com.example.service;

import org.mockito.Mock;

import com.example.openfeign.feignclient.ContaCorrentePoupancaMsFeignClient;
import com.example.openfeign.feignclient.ContaDigitalClienteMsFeignClient;
import com.example.openfeign.feignclient.ExtratoBancarioMsFeignClient;

public class SaqueContaCorrenteServiceTests {
	
	protected static final String NAO_DEVE_SER_LANCADA_NEHUMA_EXCECAO = "Não deve ser lançada nehuma exceção.";
	
	protected static final String EXCECAO_DO_TIPO_ESPERADO_NAO_FOI_LANCADA = "A exceção do tipo esperado não foi lançada.";
	
	@Mock
	protected ContaCorrentePoupancaMsFeignClient contaCorrentePoupancaMsFeignClient;
	
	@Mock
	protected ExtratoBancarioMsFeignClient extratoBancarioMsFeignClient;

	@Mock
	protected ContaDigitalClienteMsFeignClient contaDigitalClienteMsFeignClient;
}
