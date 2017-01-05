package com.intersys.relatorio.fabricaconexao;

public class ProdutoTO {

	private long chave;
	private long codigo;
	private int item;
	private int quantudade;
	private String unidade;
	private String nomeProduto;
	private double precoUnitario;
	private double valorTotal;
	private double totalGeralBruto;
	private double totalGeralLiquido;
	private double totalGeralDesconto;
	private String obs;
	private String sessao;
	private String marca;
	private String grupo;
	private String subgrupo;
	private String orderBy;

	public long getChave() {
		return chave;
	}

	public void setChave(long chave) {
		this.chave = chave;
	}

	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public int getQuantudade() {
		return quantudade;
	}

	public void setQuantudade(int quantudade) {
		this.quantudade = quantudade;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public double getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public double getTotalGeralBruto() {
		return totalGeralBruto;
	}

	public void setTotalGeralBruto(double totalGeralBruto) {
		this.totalGeralBruto = totalGeralBruto;
	}

	public double getTotalGeralLiquido() {
		return totalGeralLiquido;
	}

	public void setTotalGeralLiquido(double totalGeralLiquido) {
		this.totalGeralLiquido = totalGeralLiquido;
	}

	public double getTotalGeralDesconto() {
		return totalGeralDesconto;
	}

	public void setTotalGeralDesconto(double totalGeralDesconto) {
		this.totalGeralDesconto = totalGeralDesconto;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getSessao() {
		return sessao;
	}

	public void setSessao(String sessao) {
		this.sessao = sessao;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getSubgrupo() {
		return subgrupo;
	}

	public void setSubgrupo(String subgrupo) {
		this.subgrupo = subgrupo;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

}
