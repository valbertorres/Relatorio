package com.intersys.sistema;

import javax.swing.JOptionPane;

import org.springframework.util.SystemPropertyUtils;

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
		GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();
		GerarRelatorio gerarRelatorio = new GerarRelatorio();
		GerarRelatorio2 gerarRelatorio2 = new GerarRelatorio2();

		for (GerarImpressaoTO gerarImpressaoTO : gerarImpressaoPO.impressaoLista()) {
			selecaoimpressao(gerarImpressaoTO.getOrdemImpressao());
			agrupamento(gerarImpressaoTO.getAgrupamento());
			gerarRelatorio.setChave(gerarImpressaoTO.getChave());
			System.out.println(gerarImpressaoTO.getChave());
			gerarRelatorio.setAmbiente(ambiente);
			gerarRelatorio.setGrupo(grupo);
			gerarRelatorio.setOrderBy(ordemOmpressao);
			gerarRelatorio.setSubgrupo(subGrupo);
			gerarRelatorio.setSem(sem);
			gerarRelatorio.setId(gerarImpressaoTO.getId());
			System.out.println(gerarImpressaoTO.getId());
			gerarRelatorio.setEvento(gerarImpressaoTO.getTipoEvento());
			gerarRelatorio2.setGerarRelatorio(gerarRelatorio);

			try {
				if (gerarImpressaoTO.getQtdVias() < 1) {
					if (gerarImpressaoTO.getModeloRelatorio() == 0) {
						gerarRelatorio.gerarRelatorio();
					}
					if (gerarImpressaoTO.getModeloRelatorio() == 1) {
						gerarRelatorio2.setImprimir_via(false);
						gerarRelatorio2.gerarRelatorio2();
					}
				}

				if (gerarImpressaoTO.getQtdVias() == 2) {
					if (gerarImpressaoTO.getModeloRelatorio() == 0) {
						gerarRelatorio.gerarRelatorio();
					}
					if (gerarImpressaoTO.getModeloRelatorio() == 1) {
						gerarRelatorio2.setImprimir_via(true);
						gerarRelatorio2.gerarRelatorio2();
					}
				}

				if (gerarImpressaoTO.getQtdVias() > 2) {
					for (int i = 0; i < gerarImpressaoTO.getQtdVias(); i++) {
						if (gerarImpressaoTO.getModeloRelatorio() == 0) {
							gerarRelatorio.setContador(i);
							gerarRelatorio.gerarRelatorio();
						}
						if (gerarImpressaoTO.getModeloRelatorio() == 1) {
							gerarRelatorio.setContador(i);
							gerarRelatorio2.setImprimir_via(true);
							gerarRelatorio2.gerarRelatorio2();
						}
					}
				}
				gerarRelatorio.salvarPdf();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static int t = 0;

	public class Tempo extends Thread {
		int i = 0;

		public void run() {
			GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();
			while (true) {

				try {
					Thread.sleep(1000);
					i++;
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

						i = 0;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
