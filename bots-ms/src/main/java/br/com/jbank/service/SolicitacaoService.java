package br.com.jbank.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jbank.exception.NoResponseException;
import br.com.jbank.model.ConversationUserWithBot;
import br.com.jbank.model.Message;
import br.com.jbank.openai.enumeration.Role;
import br.com.jbank.prompt.CarregadorPrompts;

public abstract class SolicitacaoService {
	
	@Autowired
	protected CarregadorPrompts carregadorPrompts;

	@Autowired
	@Qualifier("selectedIAService")
	protected IAService iAService;
	
	protected ObjectMapper objectMapper;
	
	public SolicitacaoService() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	/**
	 * Obtem JSON com os dados extraidos pelo modelo de IA.
	 * 
	 * @param conversaUsuarioComBot
	 * @param promptComInstrucoesExtracaoDados Prompt com as instruções para a extração dos dados.
	 * @return JSON com os dados extraidos.
	 * @throws Exception 
	 */
	protected String obtemJsonDadosExtraidosPeloModelo(ConversationUserWithBot conversaUsuarioComBot, String promptComInstrucoesExtracaoDados) throws NoResponseException {
		List<Message> historicoMensagens = conversaUsuarioComBot.copiaHistorico();
		historicoMensagens.add(new Message(Role.SYSTEM.getTexto(), promptComInstrucoesExtracaoDados, Instant.now()));
		return obtemRespostaModelo(historicoMensagens).replaceAll("```json", "").replaceAll("```", "");
	}
	
	/**
	 * Obtem a resposta do modelo de IA.
	 * 
	 * @param mensagensConversa
	 * @return a resposta do modelo.
	 * @throws NoResponseException
	 */
	protected String obtemRespostaModelo(List<Message> mensagensConversa) throws NoResponseException {
		return iAService.processarMensagens(mensagensConversa)
				.orElseThrow(() -> new NoResponseException("O modelo não conseguiu gerar uma resposta."));
	}
}
