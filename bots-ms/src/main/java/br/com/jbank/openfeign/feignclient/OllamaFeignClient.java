package br.com.jbank.openfeign.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.jbank.dto.DadosSolicitacaoRespostaDto;
import br.com.jbank.dto.FracaoRespostaModeloDto;

@FeignClient(name = "http://localhost:11434")
public interface OllamaFeignClient {

	@PostMapping("/api/chat")
	public ResponseEntity<List<FracaoRespostaModeloDto>> solicitaRespostaAPIChat(
			@RequestBody DadosSolicitacaoRespostaDto contaPessoaFisicaInsercaoDto);
}