package com.intersys.sistema;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		String sql = "select p1r_tipo_evento,p1r_id,p1r_chave ,p1r_ordem_impressao,p1r_agrupamento, p1r_qtdvias,p1r_qtdvias "
				+ "from cadp01_requisicoes where p1r_dathor_proc is  null";

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

	public void inserirPdf(long id) {
		System.out.println(id);
		String sql = "update CADP01_REQUISICOES set p1r_dathor_proc=sysdate, P1R_ARQUIVO_PDF=? where p1r_id=? and p1r_dathor_proc is null";
		try {
			FileInputStream file = new FileInputStream(gerarImpressaoTO.getFile());

			try (Connection connection = FabricaDeConexao.getInstancia().getConnxao()) {
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setLong(2, id);
					statement.setBinaryStream(1, file, (int) this.gerarImpressaoTO.getFile().length());
					statement.execute();
					connection.commit();
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

	private static GerarImpressaoTO transferenciaResultset(ResultSet resultSet) throws SQLException {
		GerarImpressaoTO gerarImpressaoTO = new GerarImpressaoTO();

		gerarImpressaoTO.setChave(resultSet.getLong("p1r_chave"));
		gerarImpressaoTO.setId(resultSet.getInt("p1r_id"));
		gerarImpressaoTO.setTipoEvento(resultSet.getString("p1r_tipo_evento"));
		gerarImpressaoTO.setAgrupamento(resultSet.getInt("p1r_agrupamento"));
		gerarImpressaoTO.setOrdemImpressao(resultSet.getInt("p1r_ordem_impressao"));
		gerarImpressaoTO.setQtdVias(resultSet.getInt("p1r_qtdvias"));
		gerarImpressaoTO.setModeloRelatorio(resultSet.getInt("p1r_modelo_relatorio"));
		// gerarImpressaoTO.setImpressoraPadrão(resultSet.getString("p1r_impressora_padrao"));

		return gerarImpressaoTO;
	}

	public GerarImpressaoTO getGerarImpressaoTO() {
		return gerarImpressaoTO;
	}

	public void setGerarImpressaoTO(GerarImpressaoTO gerarImpressaoTO) {
		this.gerarImpressaoTO = gerarImpressaoTO;
	}

}
