package br.com.jbank.service;

import java.util.Map;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.MessageType;

public class MensagemChatSpringAI extends AssistantMessage {
	
	private String content;
	private MessageType messageType;
	
	public MensagemChatSpringAI(br.com.jbank.model.Message message) {
		super(message.getContent());
		this.messageType = MessageType.fromValue(message.getRole());
	}

	@Override
	public String getContent() {
		return this.content;
	}

	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}

	@Override
	public MessageType getMessageType() {
		return messageType;
	}

}
