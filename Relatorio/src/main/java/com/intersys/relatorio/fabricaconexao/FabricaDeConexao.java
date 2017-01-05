package com.intersys.relatorio.fabricaconexao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class FabricaDeConexao {

	private static String usuario = "";
	private static String senha = "dezenove";
	private static String url = "jdbc:oracle:thin:@ip:1521:servico";
	private static String driver = "oracle.jdbc.OracleDriver";
	private static FabricaDeConexao instancia;

	public static synchronized Properties getProperties() throws IOException {
		Properties properties = new Properties();
		FileInputStream file;
		try {
			file = new FileInputStream("C:/sge/SGE.INI");
			properties.load(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return properties;
	}

	public static synchronized FabricaDeConexao getInstancia() {
		if (instancia == null) {
			instancia = new FabricaDeConexao();
		}
		return instancia;
	}

	public Connection getConnxao() throws Exception {
		Properties properties = getProperties();
		String IP = properties.getProperty("IP");
		String servico = properties.getProperty("servico_original");
		url = url.replace("ip", IP);
		url = url.replace("servico", servico);
		usuario = properties.getProperty("USUARIO");
		
		Class.forName(driver);
		Connection connection = DriverManager.getConnection(url, usuario, senha);
		return connection;
	}
	
}
