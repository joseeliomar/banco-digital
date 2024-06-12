package com.example.enumeration;

public enum UnidadeFederativa {

	AC("AC", "ACRE"), 
	AL("AL", "ALAGOAS"), 
	AP("AP", "AMAPÁ"), 
	AM("AN", "AMAZONAS"), 
	BA("BA", "BAHIA"), 
	CE("CE", "CEARÁ"), 
	DF("DF", "DISTRITO FEDERAL"), 
	ES("ES", "ESPÍRITO SANTO"), 
	GO("GO", "GOIÁS"), 
	MA("MA", "MARANHÃO"), 
	MT("MT", "MATO-GROSSO"), 
	MS("MS", "MATO-GROSSO DO SUL"), 
	MG("MG", "MINAS GERAIS"), 
	PA("PA", "PARÁ"), 
	PB("PB", "PARAÍBA"), 
	PR("PR", "PARANÁ"), 
	PE("PE", "PERNAMBUCO"), 
	PI("PI", "PIAUÍ"), 
	RJ("RJ", "RIO DE JANEIRO"), 
	RN("RN", "RIO GRANDE DO NORTE"), 
	RS("RS", "RIO GRANDE DO SUL"), 
	RO("RO", "RONDÔNIA"), 
	RR("RR", "RORAIMA"), 
	SC("SC", "SANTA CATARINA"), 
	SP("SP", "SÃO PAULO"), 
	SE("SE", "SERGIPE"), 
	TO("TO", "TOCANTINS");
	
	private String sigla;
	private String nome;
	
	private UnidadeFederativa(String sigla, String nome) {
		this.sigla = sigla;
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNome() {
		return nome;
	}
}
