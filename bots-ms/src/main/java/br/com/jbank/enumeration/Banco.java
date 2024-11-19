package br.com.jbank.enumeration;

public enum Banco {
	JBANK("JBANK", "000"),
	BANCO_BRASIL("BANCO DO BRASIL SA", "001"),
	BRADESCO("BANCO BRADESCO S.A.", "237"),
	CAIXA("CAIXA ECONOMICA FEDERAL", "104"),
	ITAU("ITAU UNIBANCO S.A.", "341"),
	SANTANDER("BANCO SANTANDER (BRASIL) S.A.", "033"),
	NUBANK("NU PAGAMENTOS S.A. - INSTITUIÇÃO DE PAGAMENTO", "260"),
	INTER("BANCO INTER SA", "077");
	
	private String razaoSocial;
	private String compe;
	
	private Banco(String razaoSocial, String compe) {
		this.razaoSocial = razaoSocial;
		this.compe = compe;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public String getCompe() {
		return compe;
	}
}
