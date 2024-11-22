package br.com.jbank.service;

import java.util.List;

import br.com.jbank.model.Message;

public interface IAService {

	public String processarMensagens(List<Message> mensagensConversa);
}
