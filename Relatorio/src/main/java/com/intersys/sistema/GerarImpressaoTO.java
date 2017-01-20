package com.intersys.sistema;

import java.io.File;

public class GerarImpressaoTO {

	private long chave;
	private String tipoEvento;
	private String impressoraPadrão;
	private File file;
	private int id;
	private int agrupamento;
	private int ordemImpressao;
	private int qtdVias;
	private int modeloRelatorio;

	public int getAgrupamento() {
		return agrupamento;
	}

	public void setAgrupamento(int agrupamento) {
		this.agrupamento = agrupamento;
	}

	public int getOrdemImpressao() {
		return ordemImpressao;
	}

	public void setOrdemImpressao(int ordemImpressao) {
		this.ordemImpressao = ordemImpressao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getQtdVias() {
		return qtdVias;
	}

	public void setQtdVias(int qtdVias) {
		this.qtdVias = qtdVias;
	}

	public int getModeloRelatorio() {
		return modeloRelatorio;
	}

	public void setModeloRelatorio(int modeloRelatorio) {
		this.modeloRelatorio = modeloRelatorio;
	}

}
