package com.intersys.relatorio.fabricaconexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.SystemPropertyUtils;

public class ParcelasPO {

	public static List<ParcelasTO> parcelas() {
		ParcelasTO parcelasTO = new ParcelasTO();
		List<ParcelasTO> listaparcelas = new ArrayList<>();
		String sql = "select pfpprazo from cadpfp where pfpchave=?";

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setLong(1, ClientePO.getChave());
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						parcelasTO = transferenciaResultSet(resultSet);
						listaparcelas.add(parcelasTO);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaparcelas;
	}

	private static ParcelasTO transferenciaResultSet(ResultSet resultSet) throws SQLException {
		ParcelasTO parcelasTO = new ParcelasTO();

		parcelasTO.setParcelas(resultSet.getInt("pfpprazo"));

		return parcelasTO;
	}

}
