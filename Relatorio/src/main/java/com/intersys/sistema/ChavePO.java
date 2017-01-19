package com.intersys.sistema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.intersys.relatorio.fabricaconexao.FabricaDeConexao;

public class ChavePO {

	private static ChavePO instancia;

	public static synchronized ChavePO getInstancia() {
		if (instancia == null) {
			instancia = new ChavePO();
		}
		return instancia;
	}

	public static List<ChaveTO> chave() {
		List<ChaveTO> listaChaveTipo = new ArrayList<>();
		ChaveTO chaveTO = new ChaveTO();
		String sql = "select CASE WHEN P1TIPO=0 THEN 'Orcamento' else TPNOME end as tipo, p1chave "
				+ "from cadp01,cadtipped where p1chave=? and p1tipo=tpcodigo";

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
