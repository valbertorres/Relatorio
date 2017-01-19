package com.intersys.sistema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.intersys.relatorio.fabricaconexao.FabricaDeConexao;

public class ClientePO {

	private static ClientePO instancia;

	public static synchronized ClientePO getInstancia() {
		if (instancia == null) {
			instancia = new ClientePO();
		}
		return instancia;
	}

	private ClienteTO clienteTO;

	public ClienteTO Cliente() {
		ClienteTO clienteTO = new ClienteTO();
		String sql = "select  CASE WHEN p1fretepc=0 THEN 'Por Conta do Emitente.' "
				+ " when p1fretepc=1 THEN 'Por Conta do Destinatário.' "
				+ " WHEN p1fretepc=2 THEN 'Por Conta de Terceiros.' "
				+ " when p1fretepc=9 then 'Sem Frete.'END P1FRETEPC,"
				+ " p1orc_prazoent as prazo_de_entrega, p1orc_valpro as valido,"
				+ " cpontor as pontoreferencia ,crazao,cdenom,cenderc,cbairro,ccidadec,cuf,"
				+ " VNOME,cnomven,cfone01,cie,ccep,ccnpj, p1datap as data ,"
				+ " Decode(ccnpj,NULL,NULL,REPLACE(REPLACE(REPLACE(To_Char(LPad(REPLACE(ccnpj,'')"
				+ " ,14 ,'0'),'00,000,000,0000,00'),',','.'),' ') ,'.'||Trim(To_Char(Trunc(Mod(LPad(ccnpj,"
				+ " 14,'0'),1000000)/100),'0000'))||'.' ,'/'||Trim(To_Char(Trunc(Mod(LPad(ccnpj,14,'0'),"
				+ " 1000000)/100) ,'0000'))||'-')) AS cnpj_com_mascara"
				+ " from cadp01,CADVEN,"
				+ " cadcli,cadp01_orc where p1chave=? and ccodcli=p1codcli  and vcodven=P1CODVEN "
				+ " and p1chave=p1orc_chave";

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
				statement.setLong(1, this.clienteTO.getChave());
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						clienteTO = transferenciadeResultSet(resultSet);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clienteTO;
	}

	private static ClienteTO transferenciadeResultSet(ResultSet resultSet) throws Exception {
		ClienteTO clienteTO = new ClienteTO();
		clienteTO.setBairro(resultSet.getString("cbairro"));
		clienteTO.setCep(resultSet.getString("ccep"));
		clienteTO.setCiadade(resultSet.getString("ccidadec"));
		clienteTO.setCnpj(resultSet.getString("cnpj_com_mascara"));
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

	public ClienteTO getClienteTO() {
		return clienteTO;
	}

	public void setClienteTO(ClienteTO clienteTO) {
		this.clienteTO = clienteTO;
	}

}
