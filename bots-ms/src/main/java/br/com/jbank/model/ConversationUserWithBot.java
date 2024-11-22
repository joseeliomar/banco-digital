package br.com.jbank.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "conversations_users_bots")
public class ConversationUserWithBot {

	@Id
	private String id;

	private String telegramChatId;
	private Instant dateTimeOfCreation;
	private List<Message> messages;
	
	
	public ConversationUserWithBot() {
		this.messages = new ArrayList<>();
	}

	public ConversationUserWithBot(String telegramChatId, Instant dateTimeOfCreation) {
		this.telegramChatId = telegramChatId;
		this.dateTimeOfCreation = dateTimeOfCreation;
		this.messages = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTelegramChatId() {
		return telegramChatId;
	}

	public Instant getDateTimeOfCreation() {
		return dateTimeOfCreation;
	}

	public void setDateTimeOfCreation(Instant dateTimeOfCreation) {
		this.dateTimeOfCreation = dateTimeOfCreation;
	}

	public void setTelegramChatId(String telegramChatId) {
		this.telegramChatId = telegramChatId;
	}

	public List<Message> getMessages() {
		return messages;
	}

}
