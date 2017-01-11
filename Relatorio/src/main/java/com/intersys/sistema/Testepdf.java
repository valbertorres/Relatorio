package com.intersys.sistema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intersys.relatorio.fabricaconexao.FabricaDeConexao;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class Testepdf {
	public static void main(String[] args) throws Exception {
		ProdutoFactory.setOrderBy("order by pdnome asc");
		ClientePO.setChave(345640);
		Connection connection = FabricaDeConexao.getInstancia().getConnxao();
		List<ProdutoTO> produtoTO = ProdutoFactory.listaProduto();
		List<ClienteTO> cliente = ClientePO.Cliente();
		JRDataSource jre = new JRBeanCollectionDataSource(produtoTO);
		JRDataSource listaCliente = new JRBeanCollectionDataSource(cliente);

		InputStream relatorioSource = GerarRelatorio.class.getResourceAsStream("relatorio_pedido.jrxml");
		ByteArrayOutputStream relatorioOutputCompiled = new ByteArrayOutputStream();
		JasperCompileManager.compileReportToStream(relatorioSource, relatorioOutputCompiled);
		byte[] compiledReportData = relatorioOutputCompiled.toByteArray();
		relatorioOutputCompiled.close();
		
		
		Map<String, Object> parameters = new HashMap();
		parameters.put("data", connection);
		parameters.put("t", false);
		JasperPrint jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(compiledReportData), parameters, jre);
		JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
		jasperViewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
		jasperViewer.setVisible(true);

		// try {
		// String sql = "update CADP01_REQUISICOES set P1R_ARQUIVO_PDF=?
		// where p1r_chave=345642";
		// Path path =
		// Paths.get("C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/relatorio2.pdf");
		// byte[] data = Files.readAllBytes(path);
		// try {
		// try (Connection connection =
		// FabricaDeConexao.getInstancia().getConnxao()) {
		// try (PreparedStatement statement =
		// connection.prepareStatement(sql)) {
		// statement.setBytes(1, data);;
		// statement.executeUpdate();
		// System.out.println("statement ok!");
		// }
		// } catch (FileNotFoundException e1) {
		// System.out.println("statement 1!");
		//
		// e1.printStackTrace();
		// }
		// } catch (SQLException e) {
		// System.out.println("statement 2!");
		//
		// e.printStackTrace();
		// System.out.println("statement 3!");
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		//
		// System.out.println("atualizado com sucesso!");
		// String sql = "update CADP01_REQUISICOES set P1R_ARQUIVO_PDF=? where
		// p1r_chave=345642";
		// File file = new
		// File("C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/relatorio2.pdf");
		// System.out.println(file.length());
		// FileInputStream fis = new FileInputStream(file);
		// byte[] bytes = new byte[(int) file.length()];
		// Connection connection = FabricaDeConexao.getInstancia().getConnxao();
		// PreparedStatement statement = connection.prepareStatement(sql);
		// statement.setBinaryStream(1, fis,(int)file.length());
		// statement.execute();
		// System.out.println("atualizado com sucesso");
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		try {
			String sql = "select P1R_ARQUIVO_PDF from CADP01_REQUISICOES where p1r_chave=345642";
			// Connection connection =
			// FabricaDeConexao.getInstancia().getConnxao();
			// PreparedStatement statement = connection.prepareStatement(null);
			// ResultSet resultSet = statement.executeQuery();
			// if( resultSet.next() ){
			// Blob blob = resultSet.getBlob(1);
			// ImageIcon imageIcon = new ImageIcon(blob.getBytes(1,
			// (int)blob.length()));
			// }

			// byte[] fileBytes;
			// String query;
			// try {
			// query =
			// "select P1R_ARQUIVO_PDF from CADP01_REQUISICOES where
			// p1r_chave=345642";
			// Statement state = connection.createStatement();
			// ResultSet rs = state.executeQuery(query);
			// if (rs.next()) {
			// fileBytes = rs.getBytes(1);
			// OutputStream targetFile= new FileOutputStream(
			// "d://teste2.pdf");
			// targetFile.write(fileBytes);
			// targetFile.close();
			// }
			// System.out.println("baixou");
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			//

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// String sql = "update CADP01_REQUISICOES set P1R_ARQUIVO_PDF=?
			// where p1r_chave=345642";
			// File file = new
			// File("C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/relatorio2.pdf");
			// System.out.println(file.length());
			// FileInputStream fis = new FileInputStream(file);
			// byte[] bytes = new byte[(int) file.length()];
			// Connection connection =
			// FabricaDeConexao.getInstancia().getConnxao();
			// PreparedStatement statement = connection.prepareStatement(sql);
			// statement.setBinaryStream(1, fis,(int)file.length());
			// statement.execute();
			// System.out.println("atualizado com sucesso");

			// String sql = "update CADP01_REQUISICOES set P1R_ARQUIVO_PDF=?
			// where p1r_chave=345642";
			// File file = new
			// File("C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/relatorio2.pdf");
			// System.out.println(file.length());
			// FileInputStream fis = new FileInputStream(file);
			// byte[] bytes = new byte[(int) file.length()];
			// Connection connection =
			// FabricaDeConexao.getInstancia().getConnxao();
			// PreparedStatement statement = connection.prepareStatement(sql);
			// statement.setBinaryStream(1, fis, (int) file.length());
			// statement.execute();
			// System.out.println("atualizado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
