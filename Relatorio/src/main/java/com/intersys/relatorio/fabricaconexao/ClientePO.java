package com.intersys.relatorio.fabricaconexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.jpa.jpql.utility.iterable.CloneListIterable;

public class ClientePO {

	private static ClientePO instancia;

	public static synchronized ClientePO getInstancia() {
		if (instancia == null) {
			instancia = new ClientePO();
		}
		return instancia;
	}

	private static long chave;

	public static List<ClienteTO> Cliente() {
		List<ClienteTO> listacliente = new ArrayList<>();
		ClienteTO clienteTO = new ClienteTO();
		String sql = "select  CASE WHEN p1fretepc=0 THEN 'Por conta do emitente.' "
				+ "when p1fretepc=1 THEN 'Por conta do destinatário.' "
				+ "WHEN p1fretepc=2 THEN 'Por conta de terceiros.'" + "when p1fretepc=9 then 'sem Frete.'END P1FRETEPC,"
				+ "p1orc_prazoent as prazo_de_entrega, p1orc_valpro as valido,"
				+ "cpontor as pontoreferencia ,crazao,cdenom,cenderc,cbairro,ccidadec,cuf,"
				+ "VNOME,cnomven,cfone01,cie,ccep,ccnpj, p1datap as data from cadp01,CADVEN,"
				+ "cadcli,cadp01_orc where p1chave=? " + "and ccodcli=p1codcli  and vcodven=P1CODVEN  "
				+ "and p1chave=p1orc_chave  ";

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
				statement.setLong(1, chave);
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						clienteTO = transferenciadeResultSet(resultSet);
						listacliente.add(clienteTO);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listacliente;
	}

	private static ClienteTO transferenciadeResultSet(ResultSet resultSet) throws SQLException {
		ClienteTO clienteTO = new ClienteTO();

		clienteTO.setBairro(resultSet.getString("cbairro"));
		clienteTO.setCep(resultSet.getString("ccep"));
		clienteTO.setCiadade(resultSet.getString("ccidadec"));
		clienteTO.setCnpj(resultSet.getString("ccnpj"));
		clienteTO.setData(resultSet.getString("data"));
		clienteTO.setEndereco(resultSet.getString("cenderc"));
		clienteTO.setNomeCliente(resultSet.getString("crazao"));
		clienteTO.setNomeFantasia(resultSet.getString("cdenom"));
		clienteTO.setRg(resultSet.getString("cie"));
		clienteTO.setUf(resultSet.getString("cuf"));
		clienteTO.setVendedor(resultSet.getString("VNOME"));
		clienteTO.setPrazo(resultSet.getString("prazo_de_entrega"));
		clienteTO.setValidade(resultSet.getString("valido"));
		clienteTO.setPontoDeReferencia(resultSet.getString("pontoreferencia"));
		clienteTO.setFrete(resultSet.getString("P1FRETEPC"));
		clienteTO.setContato(resultSet.getString("cfone01"));

		return clienteTO;
	}


public static long getChave() {
		return chave;
	}

	public static void setChave(long chave) {
		ClientePO.chave = chave;
	}

public static void main(String[] args) {
	ClienteTO clienteTO = new ClienteTO();
	chave = 345640;
	
	 for(ClienteTO clienteTO2 :Cliente()){
		 System.out.println(clienteTO2.getNomeCliente());
	 }
	
}
}
