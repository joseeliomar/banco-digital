package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class ServiceNotificaPessoaFisica {

	@Value("${twilio.my-phone-number}")
	private String myTwilioPhoneNumber;

	@Value("${twilio.account-sid}")
	private String accountSid;
	
	@Value("${twilio.auth-token}")
	private String authToken;

	/**
	 * Notifica o cliente sobre a nova movimentação bancária que aconteceu em sua conta.
	 * 
	 * @param telefoneMaisMensagem número de tefone e a mensagem sepados por um ";" (ponto e vírgula).
	 */
	@KafkaListener(topics = "movimentacao-conta-pessoa-fisica-notificacao-topico", groupId = "notifica-group")
	public void notificaClienteMovimentacaoBancaria(String telefoneMaisMensagem) {
		String[] dadosSeparados = telefoneMaisMensagem.split(";");
		String telefone = dadosSeparados[0];
		String mensagem = dadosSeparados[1];

		Twilio.init(accountSid, authToken);

		Message.creator(new PhoneNumber("+55" + telefone), new PhoneNumber(myTwilioPhoneNumber), mensagem).create();
	}
}
