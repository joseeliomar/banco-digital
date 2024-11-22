package br.com.jbank.service;

import br.com.jbank.dto.DadosParaTransferenciaBancaria;
import br.com.jbank.enumeration.TipoTransferencia;

public record DadosExtraidosTransferenciaBancaria(TipoTransferencia tipoTransferencia,
		DadosParaTransferenciaBancaria dadosParaTransferenciaBancaria) {

}
