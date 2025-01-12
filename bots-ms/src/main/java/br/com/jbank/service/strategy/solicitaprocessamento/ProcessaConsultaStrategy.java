package br.com.jbank.service.strategy.solicitaprocessamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.jbank.model.ConversationUserWithBot;
import br.com.jbank.service.SaldoContaBancariaService;

@Component
public class ProcessaConsultaStrategy implements OperacaoSolicitadaStrategy {

	@Autowired
	private SaldoContaBancariaService saldoContaBancariaService;
	
	@Override
	public String processar(ConversationUserWithBot conversaAtualComBot) {
		return saldoContaBancariaService.executaConsultaSaldo(conversaAtualComBot);
	}

}
