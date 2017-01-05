package com.intersys.relatorio.fabricaconexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoFactory {
	private static String orderBy = "";

	public static List<ProdutoTO> listaProduto() {
		ProdutoTO produtoTO = new ProdutoTO();
		List<ProdutoTO> listaSelect = new ArrayList<>();
		String sql = "select pdcodgru,pdcodram,p1obs2 as obs ,p1chave , pdund ,p2qtd,p1chave,p2item, "
				+ "pdnome,p2codpro,(p2preco*p2qtd)as total,p1totdes,p1totalb,p1totall ,P2PRECO, pdsecao "
				+ "from cadp01,cadp02,cadpro where p1chave= 345640 and p1chave=p2chave and p2codpro=pdcodpro "
				+ orderBy;

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						produtoTO = transferenciaResultSet(resultSet);
						listaSelect.add(produtoTO);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaSelect;
	}

	private static ProdutoTO transferenciaResultSet(ResultSet resultSet) {
		ProdutoTO produtoTO = new ProdutoTO();
		try {
			produtoTO.setChave(resultSet.getLong("p1chave"));
			produtoTO.setCodigo(resultSet.getLong("p2codpro"));
			produtoTO.setItem(resultSet.getInt("p2item"));
			produtoTO.setNomeProduto(resultSet.getString("pdnome"));
			produtoTO.setObs(resultSet.getString("obs"));
			produtoTO.setPrecoUnitario(resultSet.getDouble("p2preco"));
			produtoTO.setQuantudade(resultSet.getInt("p2qtd"));
			produtoTO.setTotalGeralBruto(resultSet.getDouble("p1totalb"));
			produtoTO.setTotalGeralDesconto(resultSet.getDouble("p1totdes"));
			produtoTO.setTotalGeralLiquido(resultSet.getDouble("p1totall"));
			produtoTO.setUnidade(resultSet.getString("pdund"));
			produtoTO.setValorTotal(resultSet.getDouble("total"));
			produtoTO.setSessao(resultSet.getString("pdsecao"));
			produtoTO.setSubgrupo(resultSet.getString("pdcodram"));
			produtoTO.setGrupo(resultSet.getString("pdcodgru"));;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return produtoTO;

	}

	public static String getOrderBy() {
		return orderBy;
	}

	public static void setOrderBy(String orderBy) {
		ProdutoFactory.orderBy = orderBy;
	}
}
