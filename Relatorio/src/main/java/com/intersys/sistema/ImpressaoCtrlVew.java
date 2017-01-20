package com.intersys.sistema;

import javax.swing.JOptionPane;

public class ImpressaoCtrlVew {

	private Tempo imprimirAuto = new Tempo();

	public static void main(String[] args) {
		ImpressaoCtrlVew ctrlVew = new ImpressaoCtrlVew();
		ctrlVew.iniciaThred();
	}

	public void iniciaThred() {
		imprimirAuto.start();
	}

	String ordemOmpressao;

	private boolean grupo = false;
	private boolean subGrupo = false;
	private boolean ambiente = false;
	private boolean sem = false;

	public void selecaoimpressao(int ordemImpressao) {

		if (ordemImpressao == 2) {
			ordemOmpressao = "order by pdcodpro ASC";
		}

		if (ordemImpressao == 1) {
			ordemOmpressao = "order by pdnome ASC";
		}

		if (ordemImpressao == 5) {
			ordemOmpressao = "order by pdcodgru,pdcodpro ASC";
		}

		if (ordemImpressao == 6) {
			ordemOmpressao = "order by pdcodgru,pdnome ASC";
		}

		if (ordemImpressao == 4) {
			ordemOmpressao = "order by pdcodgru,pdcodram,pdcodpro ASC";
		}

		if (ordemImpressao == 3) {
			ordemOmpressao = "order by pdcodgru,pdcodram,pdnome ASC";
		}

		if (ordemImpressao == 0) {
			ordemOmpressao = "order by p2item ASC";
		}

		if (ordemImpressao == 7) {
			ordemOmpressao = "order by pdsecao";
		}

	}

	public void agrupamento(int i) {

		if (i == 2) {
			grupo = false;
			subGrupo = false;
			ambiente = true;
			sem = false;
		}

		if (i == 0) {
			grupo = true;
			subGrupo = false;
			ambiente = false;
			sem = false;
		}

		if (i == 1) {
			grupo = false;
			subGrupo = true;
			ambiente = false;
			sem = false;
		}
		if (i == 3) {
			grupo = false;
			subGrupo = false;
			ambiente = false;
			sem = true;
		}

	}

	public void imprimirAuto() {
		GerarRelatorio gerarRelatorio = new GerarRelatorio();
		GerarRelatorio2 gerarRelatorio2 = new GerarRelatorio2();
		GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();

		for (GerarImpressaoTO gerarImpressaoTO : gerarImpressaoPO.impressaoLista()) {
			gerarRelatorio.setChave(gerarImpressaoTO.getChave());
			gerarRelatorio.setAmbiente(ambiente);
			gerarRelatorio.setGrupo(grupo);
			gerarRelatorio.setOrderBy(ordemOmpressao);
			gerarRelatorio.setSubgrupo(subGrupo);
			gerarRelatorio.setSem(sem);
			gerarRelatorio.setId(gerarImpressaoTO.getId());
			gerarRelatorio.setEvento(gerarImpressaoTO.getTipoEvento());
			selecaoimpressao(gerarImpressaoTO.getOrdemImpressao());
			agrupamento(gerarImpressaoTO.getAgrupamento());
			gerarRelatorio2.setGerarRelatorio(gerarRelatorio);

			try {
				switch (gerarImpressaoTO.getModeloRelatorio()) {
				case 0:
					gerarRelatorio.gerarRelatorio();
					break;
				case 1:
					gerarRelatorio2.gerarRelatorio2();
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Impressão automatica Falhou!");
			}
		}
	}

	static int t = 0;

	public class Tempo extends Thread {
		int i = 0;
		int y = 0;

		public void run() {
			GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();
			while (true) {

				try {
					Thread.sleep(1000);
					i++;
					y++;
				} catch (Exception e) {
				}
				System.out.println(i);

				switch (i) {
				case 2:
					try {
						int lista = gerarImpressaoPO.impressaoLista().size();
						System.out.println(lista);
						if (lista != 0) {
							for (int y = 0; y < lista; y++) {
								imprimirAuto();
							}
						} else {

							i = 0;
						}
						// switch (y) {
						// case 20:
						// System.out.println("parando thread time " + y);
						// imprimirAuto.stop();
						// }
						i = 0;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
