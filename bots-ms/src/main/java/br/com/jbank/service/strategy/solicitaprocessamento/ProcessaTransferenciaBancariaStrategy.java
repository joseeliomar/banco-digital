package br.com.jbank.service.strategy.solicitaprocessamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.jbank.model.ConversationUserWithBot;
import br.com.jbank.service.TransferenciaBancariaService;

@Component
public class ProcessaTransferenciaBancariaStrategy implements OperacaoSolicitadaStrategy {
	
	@Autowired
	private TransferenciaBancariaService transferenciaBancariaService;

	@Override
	public String processar(ConversationUserWithBot conversaAtualComBot) {
		return transferenciaBancariaService.executaTransferenciaBancaria(conversaAtualComBot);
	}

}
