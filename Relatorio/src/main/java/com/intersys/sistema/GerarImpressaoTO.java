package com.intersys.sistema;

public class GerarImpressaoTO {

	private long chave;
	private String tipoEvento;
	private String impressoraPadrão;

	public long getChave() {
		return chave;
	}

	public void setChave(long chave) {
		this.chave = chave;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getImpressoraPadrão() {
		return impressoraPadrão;
	}

	public void setImpressoraPadrão(String impressoraPadrão) {
		this.impressoraPadrão = impressoraPadrão;
	}

}
