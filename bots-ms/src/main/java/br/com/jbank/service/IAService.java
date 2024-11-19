package br.com.jbank.service;

import java.util.List;

import br.com.jbank.modelo.Message;

public interface IAService {

	public String processarMensagem(List<Message> mensagensConversa);
}
