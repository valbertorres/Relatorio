package com.intersys.sistema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
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

	private GerarImpressaoTO gerarImpressaoTO;

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

	// public void atualizar() {
	// String sql = "update cadp01_requisicoes set where p1r_chave=?";
	//
	// try (Connection connection =
	// FabricaDeConexao.getInstancia().getConnxao()) {
	// try (PreparedStatement statement = connection.prepareStatement(sql)) {
	// statement.setLong(1, gerarImpressaoTO.getChave());
	// statement.execute();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public GerarImpressaoTO id() {
		String sql = "select p1r_id from cadp01 where p1r_chave==?";

		try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						gerarImpressaoTO.setId(resultSet.getInt("p1r_id"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gerarImpressaoTO;
	}

	public void inserirPdf(long chave) {
		String sql = "update CADP01_REQUISICOES set P1R_ARQUIVO_PDF=?,p1r_dathor_porc=sysdate where p1r_id=?";
		File arquivo = new File(gerarImpressaoTO.getCaminho());
		try {
			FileInputStream file = new FileInputStream(arquivo);

			try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setBinaryStream(1, file, arquivo.length());
					statement.setLong(2, chave);
					statement.executeQuery();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

	}

	// public long chave(){
	// String sql ="select p1r_chave from cadp01_requisicoes ";
	// }

	private static GerarImpressaoTO transferenciaResultset(ResultSet resultSet) throws SQLException {
		GerarImpressaoTO gerarImpressaoTO = new GerarImpressaoTO();

		gerarImpressaoTO.setChave(resultSet.getLong("p1r_chave"));
		gerarImpressaoTO.setTipoEvento(resultSet.getString("p1r_tipo_evento"));
		gerarImpressaoTO.setImpressoraPadrão(resultSet.getString("p1r_impressora_padrao"));

		return gerarImpressaoTO;
	}


	public GerarImpressaoTO getGerarImpressaoTO() {
		return gerarImpressaoTO;
	}

	public void setGerarImpressaoTO(GerarImpressaoTO gerarImpressaoTO) {
		this.gerarImpressaoTO = gerarImpressaoTO;
	}
}
