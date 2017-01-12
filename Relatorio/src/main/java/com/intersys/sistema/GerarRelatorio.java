package com.intersys.sistema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intersys.relatorio.fabricaconexao.FabricaDeConexao;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.view.JasperViewer;

public class GerarRelatorio {
	public GerarRelatorio() {

	}

	private Map<String, Object> parameters = new HashMap();

	public static String VerificarCNPJ(String cnpj) throws SQLException, Exception {
		StringBuffer cnpjBuffer = new StringBuffer();
		int countCnpj = cnpj.length();
		switch (countCnpj) {
		case 14:
			cnpjBuffer.append(cnpj);
			cnpjBuffer.insert(2, ".");
			cnpjBuffer.insert(6, ".");
			cnpjBuffer.insert(10, "/");
			cnpjBuffer.insert(15, "-");
			break;
		case 13:
			cnpj = "0" + cnpj;
			cnpjBuffer.append(cnpj);
			cnpjBuffer.insert(2, ".");
			cnpjBuffer.insert(6, ".");
			cnpjBuffer.insert(10, "/");
			cnpjBuffer.insert(15, "-");
			break;
		case 12:
			cnpj = "00" + cnpj;
			cnpjBuffer.append(cnpj);
			cnpjBuffer.insert(2, ".");
			cnpjBuffer.insert(6, ".");
			cnpjBuffer.insert(10, "/");
			cnpjBuffer.insert(15, "-");
			break;
		}
		cnpj = cnpjBuffer.toString();
		return cnpj;
	}

	public void imprimirRelatorio(String orderBy, long chave, boolean grupo, boolean subgrupo, boolean ambiente)
			throws Exception {
		Connection connection = FabricaDeConexao.getInstancia().getConnxao();
		ProdutoFactory.setOrderBy(orderBy);
		ClientePO.setChave(chave);
		List<ProdutoTO> produtoTO = ProdutoFactory.listaProduto();

		JRDataSource jre = new JRBeanCollectionDataSource(produtoTO);

		try {
			EmpresaTO empresaTO = EmpresaPO.empresa();

			this.parameters.put("empresa_nome", empresaTO.getRazao());
			this.parameters.put("empresa_cnpj", VerificarCNPJ(empresaTO.getCnpj()));
			this.parameters.put("empresa_endereco", empresaTO.getEndereco());
			this.parameters.put("empresa_fone", empresaTO.getFone());
			this.parameters.put("empresa_ie", empresaTO.getIe());
			this.parameters.put("empresa_bairro", empresaTO.getSetor());
			this.parameters.put("empresa_cep", empresaTO.getCep());
			this.parameters.put("empresa_cidade", empresaTO.getCidade());
			this.parameters.put("empresa_uf", empresaTO.getUf());
			this.parameters.put("empresa_email", empresaTO.getEmail());
			this.parameters.put("p1chave", ClientePO.getChave());
			this.parameters.put("exibir_subgrupo", subgrupo);
			this.parameters.put("exibir_grupo", grupo);
			this.parameters.put("exibir_ambiente", ambiente);
			this.parameters.put("con", connection);
			this.parameters.put("Logo", new FileInputStream("C:/sge/LOGO0.JPG"));

			InputStream relatorioSource = GerarRelatorio.class.getResourceAsStream("relatorio_pedido.jrxml");
			ByteArrayOutputStream relatorioOutputCompiled = new ByteArrayOutputStream();
			JasperCompileManager.compileReportToStream(relatorioSource, relatorioOutputCompiled);
			byte[] compiledReportData = relatorioOutputCompiled.toByteArray();
			relatorioOutputCompiled.close();

			JasperPrint jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(compiledReportData),
					this.parameters, jre);
			JasperViewer jasperViewer = new JasperViewer(jasperPrint, true);
			jasperViewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
			jasperViewer.setVisible(true);
			// JasperPrintManager.printPage(jasperPrint, 0, false);

			boolean sucesso;
			String nomerelatorio = "teste";
			String dir = "c:\\uploard" + "\\";
			File file = new File(dir);
			if (!file.exists()) {
				sucesso = (new File(dir).mkdir());
			}
			File relatorio = new File(dir + nomerelatorio);
			if (relatorio.exists()) {
				relatorio.deleteOnExit();
			}
			GerarImpressaoTO gerarImpressaoTO = new GerarImpressaoTO();
			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, dir + "/" + nomerelatorio + ".pdf");
			exporter.exportReport();

			gerarImpressaoTO.setCaminho(dir + "/" + nomerelatorio + ".pdf");
			GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();
			gerarImpressaoPO.setGerarImpressaoTO(gerarImpressaoTO);

			File fileapagando = new File(dir + "/" + nomerelatorio + ".pdf");
			System.out.println("salvo");
			// fileapagando.delete();
			// System.out.println("apagnado");

			// JasperExportManager.exportReportToPdfFile(jasperPrint,
			// dir+"/"+"relatorio.pdf");

			// File file = new File("/Relatorio/relatorio.pdf");
			// gerarImpressaoTO.setFile(file);
			// GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();
			// gerarImpressaoPO.setGerarImpressaoTO(gerarImpressaoTO);
			// gerarImpressaoPO.inserirPdf();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// atualizar();

	}

	static GerarRelatorio gerarRelatorio = new GerarRelatorio();

	// public static void main(String[] args) throws SQLException, Exception {
	//
	// gerarRelatorio.imprimirRelatorio("order by pdnome", 345642, false, false,
	// false);
	// System.out.println("!");
	//
	// }

	// public static void main(String[] args) {
	// GerarImpressaoTO gerarImpressaoTO = new GerarImpressaoTO();
	// gerarImpressaoTO.setCaminho("C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/relatorio2.pdf");
	// GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();
	// gerarImpressaoPO.setGerarImpressaoTO(gerarImpressaoTO);
	// try {
	// gerarImpressaoPO.inserirPdf();
	// System.out.println("salvo com sucesso");
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	// }

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

}
