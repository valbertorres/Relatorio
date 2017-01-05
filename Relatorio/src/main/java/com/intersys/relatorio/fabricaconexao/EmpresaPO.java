package com.intersys.relatorio.fabricaconexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpresaPO {

	public static EmpresaTO empresa() {
		EmpresaTO empresaTO = new EmpresaTO();
		String sql = "select razao,cgc,endereco,fone,inscricao,bairro,cep,cidade,e_mail,uf from cademp where codemp = 1 ";

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						empresaTO = transfereciaResultSet(resultSet);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empresaTO;
	}

	private static EmpresaTO transfereciaResultSet(ResultSet resultSet) throws SQLException {
		EmpresaTO empresaTO = new EmpresaTO();

		empresaTO.setCep(resultSet.getString("cep"));
		empresaTO.setCidade(resultSet.getString("cidade"));
		empresaTO.setCnpj(resultSet.getString("cgc"));
		empresaTO.setEmail(resultSet.getString("e_mail"));
		empresaTO.setEndereco(resultSet.getString("endereco"));
		empresaTO.setFone(resultSet.getString("fone"));
		empresaTO.setIe(resultSet.getString("inscricao"));
		empresaTO.setRazao(resultSet.getString("razao"));
		empresaTO.setSetor(resultSet.getString("bairro"));
		empresaTO.setUf(resultSet.getString("uf"));

		return empresaTO;
	}

}
