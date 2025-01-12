package br.com.jbank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbank.openai.enumeration.Role;

@Service
public class SpringAIService implements IAService {

	@Autowired
	private ChatClient chatClient;

	@Override
	public Optional<String> processarMensagens(List<br.com.jbank.model.Message> mensagensConversa) {
		List<Message> mensagens = mensagensConversa.stream().map(m -> converteMensagens(m)).toList();

		String respostaModelo = chatClient.prompt()
				.messages(mensagens).call().content();

		return Optional.of(respostaModelo);
	}

	/**
	 * Converte as mensagens para os tipos esperados pelo SprinAI
	 * @param m
	 * @return
	 */
	private Message converteMensagens(br.com.jbank.model.Message mensagem) {
		String conteudo = mensagem.getContent();
		String role = mensagem.getRole();
		if (role.equals(Role.ASSISTANT.getTexto())) {
			return (Message) new AssistantMessage(conteudo);
		} else if (role.equals(Role.SYSTEM.getTexto())) {
			return (Message) new SystemMessage(conteudo);
		}
		return (Message) new UserMessage(conteudo);
	}
}