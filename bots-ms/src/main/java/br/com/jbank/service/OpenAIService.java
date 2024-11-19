package br.com.jbank.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbank.modelo.Message;

@Service
public class OpenAIService implements IAService {

    @Autowired
	private ChatClient chatClient;

    @Override
    public String processarMensagem(List<Message> mensagensConversa) {
//        String resposta = chatClient.prompt() // colocar aqui o prompt
//		.user(mensagem)
//		.call()
//		.content();

        return null;
    }
}
