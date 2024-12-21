package br.com.jbank.service;

import java.util.List;
import java.util.Optional;

import br.com.jbank.model.Message;

public interface IAService {

	public Optional<String> processarMensagens(List<Message> mensagensConversa);
}
