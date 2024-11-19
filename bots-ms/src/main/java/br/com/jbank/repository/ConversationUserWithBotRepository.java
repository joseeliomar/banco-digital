package br.com.jbank.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.jbank.modelo.ConversationUserWithBot;

public interface ConversationUserWithBotRepository extends MongoRepository<ConversationUserWithBot, String> {
    Optional<ConversationUserWithBot> findByTelegramChatId(String telegramChatId);
}

