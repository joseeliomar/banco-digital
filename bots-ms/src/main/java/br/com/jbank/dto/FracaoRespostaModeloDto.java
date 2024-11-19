package br.com.jbank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FracaoRespostaModeloDto(String model, MensagensRespostaModeloDto message) {

}
