package br.com.jbank.service;

import java.time.Instant;
import java.util.List;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.jbank.enumeration.NomeArquivoPrompt;
import br.com.jbank.enumeration.StatusOkProcessamentoOperacao;
import br.com.jbank.exception.NoResponseException;
import br.com.jbank.model.ConversationUserWithBot;
import br.com.jbank.model.Message;
import br.com.jbank.openai.enumeration.Role;
import br.com.jbank.prompt.CarregadorPrompts;
import br.com.jbank.service.strategy.solicitaprocessamento.OperacaoSolicitadaStrategy;

@Service
public class AssistenteTelegramService {

	@Autowired
	@Qualifier("selectedIAService")
	private IAService iAService;

	@Autowired
	private ConversationUserWithBotService conversationService;

	@Autowired
	private CarregadorPrompts carregadorPrompts;

	@Autowired
	private OperacaoSolicitadaFactory operacaoSolicitadaFactory;

	/**
	 * Processa a mensagem recebida.
	 * 
	 * @param exchange
	 */
	public void processaMensagemRecebida(Exchange exchange) {
		String novaMensagemUsuario = extraiMensagemEnviadaPeloUsuario(exchange);
		if (novaMensagemUsuario != null && !novaMensagemUsuario.isEmpty()) {
			String telegramChatId = extraiIDChatTelegram(exchange);

			ConversationUserWithBot conversaAtualComBot = buscaConversa(telegramChatId);
			adicionaNovaMensagemNaConversa(conversaAtualComBot, Role.USER.getTexto(), novaMensagemUsuario);

			String respostaParaUsuario = null;
			try {
				String respostaModelo = obtemRespostaModelo(conversaAtualComBot.getMessages());
				respostaParaUsuario = processaSolitacaoFeitaPeloUsuario(conversaAtualComBot, respostaModelo);
			} catch (Exception e) {
				respostaParaUsuario = "Desculpe, ocorreu um erro ao processar sua solicitação. Tente novamente.";
				adicionaNovaMensagemNaConversa(conversaAtualComBot, Role.SYSTEM.getTexto(), respostaParaUsuario);
				e.printStackTrace();
			}

			conversationService.salvaConversa(conversaAtualComBot);
			configuraRespostaParaUsuarioExchange(exchange, respostaParaUsuario);
		}
	}

	/**
	 * Processa solicitação feita pelo usuário.
	 * 
	 * @param conversaAtualComBot
	 * @param respostaModelo
	 * @return uma resposta para o usuário.
	 */
	private String processaSolitacaoFeitaPeloUsuario(ConversationUserWithBot conversaAtualComBot,
			String respostaModelo) {
		StatusOkProcessamentoOperacao statusOkProcessamentoOperacao = StatusOkProcessamentoOperacao
				.obterItem(respostaModelo);
		if (statusOkProcessamentoOperacao != null) {
			OperacaoSolicitadaStrategy operacaoSolicitada = this.operacaoSolicitadaFactory
					.getInstance(statusOkProcessamentoOperacao);
			
			if (operacaoSolicitada == null) {
				return "Desculpe, ocorreu algum problema e não conseguimos extrair a operação solicitada.";
			}
			
			String respostaParaUsuario = operacaoSolicitada.processar(conversaAtualComBot);
			
			adicionaNovaMensagemNaConversa(conversaAtualComBot, Role.SYSTEM.getTexto(), respostaParaUsuario);
			
			return respostaParaUsuario;
		} else {
			adicionaNovaMensagemNaConversa(conversaAtualComBot, Role.ASSISTANT.getTexto(), respostaModelo);
			return respostaModelo;
		}
	}
	
	/**
	 * Configura resposta para usuário na exchange.
	 * 
	 * @param exchange
	 * @param respostaParaUsuario
	 */
	private void configuraRespostaParaUsuarioExchange(Exchange exchange, String respostaParaUsuario) {
		exchange.getMessage().setBody(respostaParaUsuario);
	}

	/**
	 * Extrai o ID do chat do Telegram.
	 * 
	 * @param exchange
	 * @return
	 */
	private String extraiIDChatTelegram(Exchange exchange) {
		return exchange.getIn().getHeader("CamelTelegramChatId", String.class);
	}

	/**
	 * Extrai a mensagem que foi enviada pelo usuário.
	 * 
	 * @param exchange
	 * @return A mensagem que foi enviada pelo usuário.
	 */
	private String extraiMensagemEnviadaPeloUsuario(Exchange exchange) {
		return exchange.getIn().getBody(String.class);
	}

	/**
	 * Obtem a resposta do modelo de IA.
	 * 
	 * @param mensagensConversa
	 * @return a resposta do modelo.
	 * @throws NoResponseException
	 */
	private String obtemRespostaModelo(List<Message> mensagensConversa) throws NoResponseException {
		return iAService.processarMensagens(mensagensConversa)
				.orElseThrow(() -> new NoResponseException("O modelo não conseguiu gerar uma resposta."));
	}

	/**
	 * Busca a conversa no banco de dados pelo ID do chat. Se a conversa ainda não
	 * existir é criada uma nova conversa e ela é retornada.
	 * 
	 * @param telegramChatId
	 * @return conversa salva no banco ou uma nova caso ainda não exista no banco de
	 *         dados uma conversa com o ID do chat que foi informado.
	 */
	private ConversationUserWithBot buscaConversa(String telegramChatId) {
		ConversationUserWithBot conversationUserWithBot = conversationService
				.buscaConversaPeloIdChatTelegram(telegramChatId).orElse(null);

		if (conversationUserWithBot == null) {
			String promptParaIA = carregadorPrompts.getPrompt(NomeArquivoPrompt.PROMPT_INICIAL.getNome());

			ConversationUserWithBot novaConversa = new ConversationUserWithBot(telegramChatId, Instant.now());
			adicionaNovaMensagemNaConversa(novaConversa, Role.SYSTEM.getTexto(), promptParaIA);
			return novaConversa;
		}

		return conversationUserWithBot;
	}

	/**
	 * Adiciona uma nova mensagem a conversa.
	 * 
	 * @param conversaUsuarioComBot
	 * @param papel
	 * @param novaMensagemUsuario
	 */
	private void adicionaNovaMensagemNaConversa(ConversationUserWithBot conversaUsuarioComBot, String papel,
			String novaMensagemUsuario) {
		conversaUsuarioComBot.getMessages().add(new Message(papel, novaMensagemUsuario, Instant.now()));
	}

}