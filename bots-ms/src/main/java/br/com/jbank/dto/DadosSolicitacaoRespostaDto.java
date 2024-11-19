package br.com.jbank.dto;

import java.util.List;

public record DadosSolicitacaoRespostaDto(String model, List<MensagensConversaDto> messages) {

}
