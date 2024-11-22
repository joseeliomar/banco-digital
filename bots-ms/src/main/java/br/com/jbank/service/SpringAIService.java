package br.com.jbank.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringAIService implements IAService {

	@Autowired
	private ChatClient chatClient;

	@Override
	public String processarMensagens(List<br.com.jbank.model.Message> mensagensConversa) {
		List<Message> mensagens = mensagensConversa.stream().map(m -> (Message) new MensagemChatSpringAI(m)).toList();
		String respostaModelo = chatClient.prompt() // colocar aqui o prompt
				.messages(mensagens)
				.call()
				.content();

		return respostaModelo;
	}
}
