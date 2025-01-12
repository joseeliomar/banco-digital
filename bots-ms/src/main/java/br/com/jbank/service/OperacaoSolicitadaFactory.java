package br.com.jbank.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.jbank.enumeration.StatusOkProcessamentoOperacao;
import br.com.jbank.service.strategy.solicitaprocessamento.OperacaoSolicitadaStrategy;
import br.com.jbank.service.strategy.solicitaprocessamento.ProcessaConsultaStrategy;
import br.com.jbank.service.strategy.solicitaprocessamento.ProcessaTransferenciaBancariaStrategy;

@Component
public class OperacaoSolicitadaFactory {
	
	private Map<StatusOkProcessamentoOperacao, OperacaoSolicitadaStrategy> estrategias;

	private OperacaoSolicitadaFactory(ProcessaTransferenciaBancariaStrategy processaTransferenciaBancariaStrategy, 
            ProcessaConsultaStrategy processaConsultaStrategy) {
		 this.estrategias = Map.of(
					StatusOkProcessamentoOperacao.STATUS_PARA_PROCESSAMENTO_TRANSFERENCIA, processaTransferenciaBancariaStrategy,
					StatusOkProcessamentoOperacao.STATUS_PARA_CONSULTA_SALDO, processaConsultaStrategy);
	}
	
	public OperacaoSolicitadaStrategy getInstance(StatusOkProcessamentoOperacao statusOkProcessamentoOperacao) {
		return estrategias.get(statusOkProcessamentoOperacao);
	}
}
