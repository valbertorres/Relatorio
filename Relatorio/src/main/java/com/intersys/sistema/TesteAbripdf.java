package com.intersys.sistema;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class TesteAbripdf {

	public static void main(String[] args) {
		File file = new File("C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/relatorio2.pdf");
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
