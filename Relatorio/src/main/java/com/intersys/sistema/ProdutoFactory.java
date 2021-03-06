package com.intersys.sistema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.SystemPropertyUtils;

import com.intersys.relatorio.fabricaconexao.FabricaDeConexao;

public class ProdutoFactory {
	private static ProdutoFactory instancia;

	public static synchronized ProdutoFactory getInstancia() {
		if (instancia == null) {
			instancia = new ProdutoFactory();
		}
		return instancia;
	}

	private static String orderBy = "";
	private static ProdutoTO produtoTO;

	public static List<ProdutoTO> listaProduto() {
		List<ProdutoTO> listaSelect = new ArrayList<>();
		String sql = "select p2ambiente,pdcodgru,pdcodram,p1obs2 as obs ,p1chave , "
				+ "pdund ,p2qtd,p1chave,p2item,pdnome,p2codpro,"
				+ "(p2preco*p2qtd)as total,p1totdes,p1totalb,p1totall ," + "P2PRECO, pdsecao " + " from cadp01,cadp02,"
				+ "cadpro where p1chave=? and p1chave=p2chave " + "and p2codpro=pdcodpro " + orderBy;

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setLong(1, produtoTO.getChave());
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						ProdutoTO produtoTO = new ProdutoTO();
						produtoTO = transferenciaResultSet(resultSet);
						listaSelect.add(produtoTO);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
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
			produtoTO.setGrupo(resultSet.getString("pdcodgru"));
			produtoTO.setAmbiente(resultSet.getString("p2ambiente"));
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

	public static ProdutoTO getProdutoTO() {
		return produtoTO;
	}

	public static void setProdutoTO(ProdutoTO produtoTO) {
		ProdutoFactory.produtoTO = produtoTO;
	}

	public static void main(String[] args) {

		ProdutoTO produtoTO = new ProdutoTO();
		produtoTO.setChave(345642);
		ProdutoFactory.setProdutoTO(produtoTO);
		for(ProdutoTO to : ProdutoFactory.listaProduto()){
			System.out.println(to.getNomeProduto());
		}
		for (ProdutoTO produtoTO2 : ProdutoFactory.listaProduto()) {
			System.out.println(produtoTO2.getNomeProduto());
		}
	}

}
