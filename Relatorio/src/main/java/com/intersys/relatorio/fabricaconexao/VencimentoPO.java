package com.intersys.relatorio.fabricaconexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VencimentoPO {

	public static List<VenciementoTO> vencimento() {
		VenciementoTO venciementoTO = new VenciementoTO();
		List<VenciementoTO> listaVencimento = new ArrayList<>();
		String sql = "select pfpnomfor as formardepagamento, pfpdata, pfpvalor from cadpfp where pfpchave=?";

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setLong(1, ClientePO.getChave());
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						venciementoTO = transferenciaResultSet(resultSet);
						listaVencimento.add(venciementoTO);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaVencimento;
	}

	private static VenciementoTO transferenciaResultSet(ResultSet resultSet) throws SQLException {
		VenciementoTO venciementoTO = new VenciementoTO();

		venciementoTO.setData(resultSet.getDate("pfpdata"));
		venciementoTO.setFormaPagamento(resultSet.getString("formardepagamento"));
		venciementoTO.setValor(resultSet.getDouble("pfpvalor"));

		return venciementoTO;
	}
}
