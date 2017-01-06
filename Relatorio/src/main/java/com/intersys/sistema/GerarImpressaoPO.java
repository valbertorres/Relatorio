package com.intersys.sistema;

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

	private static GerarImpressaoTO transferenciaResultset(ResultSet resultSet) throws SQLException {
		GerarImpressaoTO gerarImpressaoTO = new GerarImpressaoTO();

		gerarImpressaoTO.setChave(resultSet.getLong("p1r_chave"));
		gerarImpressaoTO.setTipoEvento(resultSet.getString("p1r_tipo_evento"));
		gerarImpressaoTO.setImpressoraPadrão(resultSet.getString("p1r_impressora_padrao"));

		return gerarImpressaoTO;
	}
	
	public static void main(String[] args) {
		for(GerarImpressaoTO gerarImpressaoTO : GerarImpressaoPO.getinstancia().impressaoLista()){
			System.out.println(gerarImpressaoTO.getChave());
		}
	}

}
