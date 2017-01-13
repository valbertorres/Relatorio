package com.intersys.sistema;

import java.util.ArrayList;
import java.util.List;

public class TesteAbripdf {

	public static void main(String[] args) {
		// File file = new
		// File("C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/relatorio2.pdf");
		// try {
		// Desktop.getDesktop().open(file);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		String tipoI = "";
		String tipoB = "B";
		String tipoV = "V";

		// for (GerarImpressaoTO gerarImpressaoTO : listaImpressao) {
		// tipoI = gerarImpressaoTO.getTipoEvento();
		// System.out.println(tipoI);
		// if (tipoI == tipoV) {
		//
		// System.out.println("entrou");
		// try {
		// String ordemOmpressao = "";
		// gerarRelatorio.imprimirRelatorio(ordemOmpressao,
		// gerarImpressaoTO.getChave(), false, false, false);
		// gerarImpressaoPO.inserirPdf(gerarImpressaoTO.getChave());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }else{
		// System.out.println("não entrou");
		// }
		// }

		List<String> lista = new ArrayList<>();
		lista.add("B");
		lista.add("A");
		GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();
		for (GerarImpressaoTO gerarImpressaoTO : gerarImpressaoPO.impressaoLista()) {
			System.out.println(gerarImpressaoTO.getTipoEvento());
			if (gerarImpressaoTO.getTipoEvento().equals("A")) {
				System.out.println("entrou");
			} else {
				System.out.println("não entrou");
			}
		}
	}
}
