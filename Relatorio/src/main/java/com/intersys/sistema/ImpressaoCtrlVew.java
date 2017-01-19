package com.intersys.sistema;

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

	boolean grupo = false;
	boolean subGrupo = false;
	boolean ambiente = false;
	boolean sem = false;

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
		GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();

		for (GerarImpressaoTO gerarImpressaoTO : gerarImpressaoPO.impressaoLista()) {
			if (gerarImpressaoTO.getTipoEvento().equals("A")) {
				System.out.println(gerarImpressaoTO.getChave());
				System.out.println(gerarImpressaoTO.getTipoEvento());
				selecaoimpressao(gerarImpressaoTO.getOrdemImpressao());
				agrupamento(gerarImpressaoTO.getAgrupamento());
				try {
					gerarRelatorio.imprimirRelatorio(ordemOmpressao, gerarImpressaoTO.getChave(),
							gerarImpressaoTO.getId(), grupo, subGrupo, ambiente, gerarImpressaoTO.getTipoEvento());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (gerarImpressaoTO.getTipoEvento().equals("I")) {
				System.out.println(gerarImpressaoTO.getTipoEvento());
				System.out.println(gerarImpressaoTO.getChave());
				selecaoimpressao(gerarImpressaoTO.getOrdemImpressao());
				agrupamento(gerarImpressaoTO.getAgrupamento());
				try {
					gerarRelatorio.imprimirRelatorio(ordemOmpressao, gerarImpressaoTO.getChave(),
							gerarImpressaoTO.getId(), grupo, subGrupo, ambiente, gerarImpressaoTO.getTipoEvento());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (gerarImpressaoTO.getTipoEvento().equals("V")) {
				selecaoimpressao(gerarImpressaoTO.getOrdemImpressao());
				agrupamento(gerarImpressaoTO.getAgrupamento());
				try {
					System.out.println("entrou no V");
					System.out.println(gerarImpressaoTO.getChave());

					gerarRelatorio.imprimirRelatorio(ordemOmpressao, gerarImpressaoTO.getChave(),
							gerarImpressaoTO.getId(), grupo, subGrupo, ambiente, gerarImpressaoTO.getTipoEvento());

				} catch (Exception e) {
					e.printStackTrace();
				}
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
//						switch (y) {
//						case 20:
//							System.out.println("parando thread  time " + y);
//							imprimirAuto.stop();
//						}
						i = 0;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
