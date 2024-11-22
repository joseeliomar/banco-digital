package br.com.jbank.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbank.model.ConversationUserWithBot;
import br.com.jbank.repository.ConversationUserWithBotRepository;

@Service
public class ConversationUserWithBotService {

	@Autowired
	private ConversationUserWithBotRepository conversationRepository;

	public ConversationUserWithBot salvaConversa(ConversationUserWithBot conversationUserWithBot) {
		return conversationRepository.save(conversationUserWithBot);
	}

	public Optional<ConversationUserWithBot> buscaConversaPeloIdChatTelegram(String idChatTelegram) {
		return conversationRepository.findByTelegramChatId(idChatTelegram);
	}
}
