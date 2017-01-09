package com.intersys.sistema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.intersys.relatorio.fabricaconexao.FabricaDeConexao;

public class GerarImpressaoPO {

	private static GerarImpressaoPO instancia;

	public static synchronized GerarImpressaoPO getinstancia() {
		if (instancia == null) {
			instancia = new GerarImpressaoPO();
		}
		return instancia;
	}

	private GerarImpressaoTO gerarImpressaoTO;

	public List<GerarImpressaoTO> impressaoLista() {
		GerarImpressaoTO gerarImpressaoTO = new GerarImpressaoTO();
		List<GerarImpressaoTO> listaImpressao = new ArrayList<>();
		String sql = "SELECT P1R_CHAVE, P1R_CODEMP,P1R_TIPO_EVENTO,P1R_ARQUIVO_PDF,P1R_IMPRESSORA_PADRAO "
				+ "FROM CADP01_REQUISICOES";

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						gerarImpressaoTO = transferenciaResultset(resultSet);
						listaImpressao.add(gerarImpressaoTO);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaImpressao;
	}

	public void atualizar() {
		String sql = "update cadp01_requisicoes set p1r_tipo_evento=null where p1r_chave=?";

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setLong(1, gerarImpressaoTO.getChave());
				statement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void inserirPdf()  {
		String sql = "update CADP01_REQUISICOES set P1R_ARQUIVO_PDF=? where p1r_chave=345642";
		File arquivo = new File(this.getGerarImpressaoTO().getCaminho());
		FileInputStream file;
		byte[] data = Files.readAllBytes(path);
		System.out.println(arquivo.length());
		try {
			file = new FileInputStream(arquivo);
		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setBytes(1, file);;
				statement.executeUpdate();
				System.out.println("statement ok!");
			}
		} catch (FileNotFoundException e1) {
			System.out.println("statement 1!");

			e1.printStackTrace();
		}
		} catch (SQLException e) {
			System.out.println("statement 2!");

			e.printStackTrace();
			System.out.println("statement 3!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static GerarImpressaoTO transferenciaResultset(ResultSet resultSet) throws SQLException {
		GerarImpressaoTO gerarImpressaoTO = new GerarImpressaoTO();

		gerarImpressaoTO.setChave(resultSet.getLong("p1r_chave"));
		gerarImpressaoTO.setTipoEvento(resultSet.getString("p1r_tipo_evento"));
		gerarImpressaoTO.setImpressoraPadrão(resultSet.getString("p1r_impressora_padrao"));

		return gerarImpressaoTO;
	}

	public static void main(String[] args) {
		for (GerarImpressaoTO gerarImpressaoTO : GerarImpressaoPO.getinstancia().impressaoLista()) {
			System.out.println(gerarImpressaoTO.getChave());
		}
	}

	public GerarImpressaoTO getGerarImpressaoTO() {
		return gerarImpressaoTO;
	}

	public void setGerarImpressaoTO(GerarImpressaoTO gerarImpressaoTO) {
		this.gerarImpressaoTO = gerarImpressaoTO;
	}

}