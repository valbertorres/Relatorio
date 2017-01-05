package com.intersys.relatorio.fabricaconexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChavePO {

	public static List<ChaveTO>chave() {
		List<ChaveTO>listaChaveTipo = new ArrayList<>();
		ChaveTO chaveTO = new ChaveTO();
		String sql = "select CASE WHEN P1TIPO=0 THEN 'Orcamento' else TPNOME end as tipo, p1chave "
				+ "from cadp01,cadtipped where p1chave=? and p1tipo=tpcodigo";

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setLong(1, ClientePO.getChave());
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						chaveTO = transferenciaResultSet(resultSet);
						listaChaveTipo.add(chaveTO);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaChaveTipo;
	}

	private static ChaveTO transferenciaResultSet(ResultSet resultSet) throws SQLException {
		ChaveTO chaveTO = new ChaveTO();

		chaveTO.setChave(resultSet.getLong("p1chave"));
		chaveTO.setTipoVenda(resultSet.getString("tipo"));

		return chaveTO;
	}

}
