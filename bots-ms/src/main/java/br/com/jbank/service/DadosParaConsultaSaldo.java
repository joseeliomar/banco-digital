package br.com.jbank.service;

import br.com.jbank.enumeration.TipoConta;

public record DadosParaConsultaSaldo(String cpfCliente, TipoConta tipoConta) {

}
