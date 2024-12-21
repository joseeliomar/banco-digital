package br.com.jbank.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.jbank.dto.DadosSolicitacaoRespostaDto;
import br.com.jbank.dto.FracaoRespostaModeloDto;
import br.com.jbank.dto.MensagensConversaDto;
import br.com.jbank.model.Message;
import br.com.jbank.openfeign.feignclient.OllamaFeignClient;

@Service
public class OllamaService implements IAService {

	@Autowired
	private OllamaFeignClient ollamaFeignClient;
	
	@Value("${ollama.model}")
	private String ollamaModel;

	@Override
	public Optional<String> processarMensagens(List<Message> mensagensConversa) {
		DadosSolicitacaoRespostaDto dadosSolicitacaoRespostaDto = new DadosSolicitacaoRespostaDto(this.ollamaModel,
				mensagensConversa.stream().map(m -> new MensagensConversaDto(m.getRole(), m.getContent())).toList());

		ResponseEntity<List<FracaoRespostaModeloDto>> respostaRequisicaoHttp = this.ollamaFeignClient
				.solicitaRespostaAPIChat(dadosSolicitacaoRespostaDto);

		String respostaModelo = respostaRequisicaoHttp.getBody().stream()
                .map(fracao -> fracao.message().content())
                .collect(Collectors.joining(""));;

		return Optional.of(respostaModelo);
	}
}
