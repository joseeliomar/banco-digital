package br.com.jbank.dto;

public class TransferenciaEntreContasMesmoClienteDto extends DadosParaTransferenciaBancaria {

	private static final long serialVersionUID = 1L;
	
	private String cpfCliente;
	private double valorTransferencia;
	private String tipoContaOrigemDinheiro;

	public String getCpfCliente() {
		return cpfCliente;
	}

	public double getValorTransferencia() {
		return valorTransferencia;
	}

	public String getTipoContaOrigemDinheiro() {
		return tipoContaOrigemDinheiro;
	}

}
