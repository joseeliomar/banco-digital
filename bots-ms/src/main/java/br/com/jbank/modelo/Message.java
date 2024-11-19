package br.com.jbank.modelo;

import java.time.Instant;

public class Message {
	private String role;
	private String content;
	private Instant dateTimeOfSubmission;
	
	public Message() {
	}

	public Message(String role, String content, Instant dateTimeOfSubmission) {
		this.role = role;
		this.content = content;
		this.dateTimeOfSubmission = dateTimeOfSubmission;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Instant getDateTimeOfSubmission() {
		return dateTimeOfSubmission;
	}

	public void setDateTimeOfSubmission(Instant dateTimeOfSubmission) {
		this.dateTimeOfSubmission = dateTimeOfSubmission;
	}

}
