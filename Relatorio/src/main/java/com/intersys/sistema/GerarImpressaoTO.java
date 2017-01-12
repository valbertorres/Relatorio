package com.intersys.sistema;

import java.io.File;

public class GerarImpressaoTO {

	private long chave;
	private String tipoEvento;
	private String impressoraPadrão;
	private File file;
	private String caminho;
	private int id;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

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
