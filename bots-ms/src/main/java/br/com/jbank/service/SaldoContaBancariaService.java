package br.com.jbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.jbank.dto.ContaPessoaFisicaBuscaDto1;
import br.com.jbank.enumeration.MensagemPadronizada;
import br.com.jbank.enumeration.NomeArquivoPrompt;
import br.com.jbank.model.ConversationUserWithBot;
import br.com.jbank.openfeign.feignclient.ContaCorrentePoupancaMsFeignClient;
import br.com.jbank.utils.Utils;

@Service
public class SaldoContaBancariaService extends SolicitacaoService {
	
	@Autowired
	private ContaCorrentePoupancaMsFeignClient contaCorrentePoupancaMsFeignClient;
	
	/**
	 * Executa a consulta do saldo da conta do cliente.
	 * 
	 * @param conversaAtualComBot
	 * @return resposta sobre a consulta do saldo, que pode ser tanto uma mensagem
	 *         informando o saldo atual quanto alguma outra mensagem que envolve
	 *         esse processo de consulta.
	 */
	public String executaConsultaSaldo(ConversationUserWithBot conversaAtualComBot) {
		DadosParaConsultaSaldo dadosParaConsultaSaldo = obtemDadosConsultaSaldo(conversaAtualComBot);

		if (dadosParaConsultaSaldo == null) {
			return "Ocorreu algum problema ao tentarmos obter os dados necessário para a consultado do saldo. "
					+ MensagemPadronizada.MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO;
		}

		ResponseEntity<ContaPessoaFisicaBuscaDto1> respostaAPI = contaCorrentePoupancaMsFeignClient
				.buscaContaPessoaFisica(dadosParaConsultaSaldo.cpfCliente(), dadosParaConsultaSaldo.tipoConta());

		HttpStatusCode statusCode = respostaAPI.getStatusCode();

		if (statusCode != null && statusCode.is2xxSuccessful()) {
			ContaPessoaFisicaBuscaDto1 contaPessoaFisica = respostaAPI.getBody();
			return "O saldo atual da sua conta " + contaPessoaFisica.getTipoConta().getNome().toLowerCase() + " é de "
					+ Utils.formataValorReais(contaPessoaFisica.getSaldo()) + ".";
		} else {
			return "Ocorreu algum problema durante a consultado do saldo. Por favor, solicite "
					+ "novamente mais tarde que o último pedido de consulta de saldo seja processsado novamente. "
					+ MensagemPadronizada.MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO;
		}
	}
	
	/**
	 * Obtem os dados necessários para a consulta do saldo da conta desejada.
	 * 
	 * @param conversaUsuarioComBot
	 * @return os dados necessários para a consultado do saldo.
	 */
	private DadosParaConsultaSaldo obtemDadosConsultaSaldo(ConversationUserWithBot conversaUsuarioComBot) {
		String promptParaIA = carregadorPrompts
				.getPrompt(NomeArquivoPrompt.PROMPT_PARA_EXTRACAO_DADOS_NECESSARIO_CONSULTA_SALDO.getNome());
		
		DadosParaConsultaSaldo dadosTransferenciaBancaria = null;
		try {
			String json = obtemJsonDadosExtraidosPeloModelo(conversaUsuarioComBot, promptParaIA);
			dadosTransferenciaBancaria = objectMapper.readValue(json, DadosParaConsultaSaldo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dadosTransferenciaBancaria;
	}
	
}
