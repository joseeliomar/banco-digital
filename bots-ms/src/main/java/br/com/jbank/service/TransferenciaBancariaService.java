package br.com.jbank.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.jbank.dto.DadosParaTransferenciaBancaria;
import br.com.jbank.dto.DadosParaTransferenciaEntreContasClientesDiferentesDesseBancoDto;
import br.com.jbank.dto.DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto;
import br.com.jbank.dto.DadosParaTransferenciaEntreContasMesmoClienteDto;
import br.com.jbank.enumeration.MensagemPadronizada;
import br.com.jbank.enumeration.NomeArquivoPrompt;
import br.com.jbank.enumeration.TipoTransferencia;
import br.com.jbank.model.ConversationUserWithBot;
import br.com.jbank.openfeign.feignclient.TransferenciaFeignClient;

@Service
public class TransferenciaBancariaService extends SolicitacaoService {
	
	@Autowired
	private TransferenciaFeignClient transferenciaFeignClient;
	
	/**
	 * Executa a transferência bancária desejada.
	 * 
	 * @param conversaAtualComBot
	 * @return resposta sobre a transferência, que pode ser tanto uma mensagem
	 *         informando que a transferência ocorreu com sucesso quanto alguma
	 *         outra mensagem que envolve esse processo de transferência.
	 */
	public String executaTransferenciaBancaria(ConversationUserWithBot conversaAtualComBot) {
		DadosExtraidosTransferenciaBancaria dadosExtraidos = obtemDadosTransferencia(
				conversaAtualComBot);
		
		if (dadosExtraidos == null || dadosExtraidos.dadosParaTransferenciaBancaria() == null) {
			return "Ocorreu algum problema ao tentarmos obter os dados necessário para a transferência. "
					+ MensagemPadronizada.MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO;
		}
		
		DadosParaTransferenciaBancaria dadosParaTransferenciaBancaria = dadosExtraidos
				.dadosParaTransferenciaBancaria();

		HttpStatusCode statusCode = null;
		TipoTransferencia operacao = dadosExtraidos.tipoTransferencia();
		if (operacao.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_DIFERENTES_DESSE_BANCO)) {
			statusCode = efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(dadosParaTransferenciaBancaria);
		} else if (operacao.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_MESMO_CLIENTE)) {
			statusCode = efetuaTransferenciaEntreContasMesmoCliente(dadosParaTransferenciaBancaria);
		} else if (operacao
				.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_INSTITUICOES_FINANCEIRAS_DIFERENTES)) {
			statusCode = efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
					dadosParaTransferenciaBancaria);
		} else {
			return "Desculpe, ocorreu algum problema e não conseguimos extrair a modalidade de transferência. "
					+ MensagemPadronizada.MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO;
		}

		if (statusCode != null && statusCode.is2xxSuccessful()) {
			return "A transferência ocorreu com sucesso!";
		} else {
			return "Ocorreu algum problema durante a transferência. Por favor, solicite "
					+ "novamente mais tarde que o último pedido de transferência seja processsado novamente. "
					+ MensagemPadronizada.MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO;
		}
	}
	
	/**
	 * Efetua transferência entre contas de clientes diferentes desse banco.
	 * 
	 * @param dadosParaTransferencia
	 * @return o código de status HTTP que foi retornado ao tentar efetuar a
	 *         transferência via API.
	 */
	private HttpStatusCode efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(
			DadosParaTransferenciaBancaria dadosParaTransferencia) {
		return this.transferenciaFeignClient
				.efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(
						(DadosParaTransferenciaEntreContasClientesDiferentesDesseBancoDto) dadosParaTransferencia)
				.getStatusCode();
	}
	
	/**
	 * Efetua transferência entre contas do mesmo cliente, ou seja, da conta
	 * corrente para a conta poupança, mas também da conta poupança para a conta
	 * corrente.
	 * 
	 * @param dadosParaTransferencia
	 * @return o código de status HTTP que foi retornado ao tentar efetuar a
	 *         transferência via API.
	 */
	private HttpStatusCode efetuaTransferenciaEntreContasMesmoCliente(
			DadosParaTransferenciaBancaria dadosParaTransferencia) {
		return this.transferenciaFeignClient.efetuaTransferenciaEntreContasMesmoCliente(
				(DadosParaTransferenciaEntreContasMesmoClienteDto) dadosParaTransferencia).getStatusCode();
	}
	
	/**
	 * Efetua transferência entre contas de cliente de instituições financeiras
	 * diferentes.
	 * 
	 * @param dadosParaTransferencia
	 * @return o código de status HTTP que foi retornado ao tentar efetuar a
	 *         transferência via API.
	 */
	private HttpStatusCode efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
			DadosParaTransferenciaBancaria dadosParaTransferencia) {
		return this.transferenciaFeignClient.efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
				(DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto) dadosParaTransferencia)
				.getStatusCode();
	}
	
	/**
	 * Obtem os dados necessários para a transferência bancária desejada.
	 * 
	 * @param conversaUsuarioComBot
	 * @return
	 */
	private DadosExtraidosTransferenciaBancaria obtemDadosTransferencia(ConversationUserWithBot conversaUsuarioComBot) {
		String promptParaIA = carregadorPrompts
				.getPrompt(NomeArquivoPrompt.PROMPT_PARA_EXTRACAO_DADOS_NECESSARIOS_TRANSFERENCIA_BANCARIA.getNome());
		

		try {
			String json = obtemJsonDadosExtraidosPeloModelo(conversaUsuarioComBot, promptParaIA);
			Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});

			String tipoTransferencia = (String) map.get("tipoTransferencia");

			DadosParaTransferenciaBancaria dadosTransferenciaBancaria = null;
			if (tipoTransferencia.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_MESMO_CLIENTE.name())) {
				dadosTransferenciaBancaria = objectMapper.convertValue(map,
						DadosParaTransferenciaEntreContasMesmoClienteDto.class);
				
				return new DadosExtraidosTransferenciaBancaria(
						TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_MESMO_CLIENTE, dadosTransferenciaBancaria);
			} else if (tipoTransferencia
					.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_DIFERENTES_DESSE_BANCO.name())) {
				dadosTransferenciaBancaria = objectMapper.convertValue(map,
						DadosParaTransferenciaEntreContasClientesDiferentesDesseBancoDto.class);
				
				return new DadosExtraidosTransferenciaBancaria(
						TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_DIFERENTES_DESSE_BANCO, dadosTransferenciaBancaria);
			} else if (tipoTransferencia
					.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_INSTITUICOES_FINANCEIRAS_DIFERENTES.name())) {
				dadosTransferenciaBancaria = objectMapper.convertValue(map,
						DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto.class);
				
				return new DadosExtraidosTransferenciaBancaria(
						TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_INSTITUICOES_FINANCEIRAS_DIFERENTES, dadosTransferenciaBancaria);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
