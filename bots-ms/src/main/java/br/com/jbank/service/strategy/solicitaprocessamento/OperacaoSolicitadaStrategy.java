package br.com.jbank.service.strategy.solicitaprocessamento;

import br.com.jbank.model.ConversationUserWithBot;

public interface OperacaoSolicitadaStrategy {
	
	/**
	 * Processa a operação solitada.
	 * 
	 * @param conversaAtualComBot
	 * @return uma resposta para o usuário sobre a operação executada.
	 */
	String processar(ConversationUserWithBot conversaAtualComBot);
	
}
