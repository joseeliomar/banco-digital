package br.com.jbank.bot.telegram;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.jbank.service.AssistenteTelegramService;

@Component
public class TelegramRoute extends RouteBuilder {
	
	@Autowired
	private AssistenteTelegramService assistenteTelegramService;

	@Override
	public void configure() {
		from("telegram:bots").process(exchange -> {
			assistenteTelegramService.processaMensagemRecebida(exchange);
		}).to("telegram:bots");
	}

}
