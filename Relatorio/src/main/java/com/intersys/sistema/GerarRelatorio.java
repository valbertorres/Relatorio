package com.intersys.sistema;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import com.intersys.relatorio.fabricaconexao.FabricaDeConexao;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class GerarRelatorio {
	public GerarRelatorio() {

	}

	private Map<String, Object> parameters = new HashMap();
	private String dir;
	private String nomerelatorio;

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

	public void imprimirRelatorio(String orderBy, long chave, long id, boolean grupo, boolean subgrupo,
			boolean ambiente, String tipo) throws Exception {
		Connection connection = FabricaDeConexao.getInstancia().getConnxao();
		ProdutoFactory.setOrderBy(orderBy);
		ClientePO.setChave(chave);
		ProdutoTO produtoTO = new ProdutoTO();
		produtoTO.setChave(chave);
		ProdutoFactory.setProdutoTO(produtoTO);
		List<ProdutoTO> produtoTO2 = ProdutoFactory.listaProduto();

		JRDataSource jre = new JRBeanCollectionDataSource(produtoTO2);

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
			JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
			jasperViewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
			// jasperViewer.setVisible(true);
			// JasperPrintManager.printPage(jasperPrint, 0, false);

			this.nomerelatorio = String.valueOf(chave) + "" + id;
			this.dir = "c:\\uploard" + "\\";
			File file = new File(dir);
			if (!file.exists()) {
				file.mkdir();
			}

			GerarImpressaoTO gerarImpressaoTO = new GerarImpressaoTO();
			JasperExportManager.exportReportToPdfFile(jasperPrint, dir + "/" + nomerelatorio + ".pdf");
			
			dir = dir.replace("\\", "/");
			File diretorio = new File(dir + "" + this.nomerelatorio + ".pdf");
			System.out.println(dir + "" + this.nomerelatorio + ".pdf");
			gerarImpressaoTO.setFile(diretorio);
			GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();
			gerarImpressaoPO.setGerarImpressaoTO(gerarImpressaoTO);
			gerarImpressaoPO.inserirPdf(id);
			
			if (tipo.equals("I")) {
				PrintService impressoraPadrao = PrintServiceLookup.lookupDefaultPrintService();
				DocFlavor docFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
				HashDocAttributeSet docAttributeSet = new HashDocAttributeSet();
				try {
					FileInputStream inputStream = new FileInputStream(diretorio);
					Doc doc = new SimpleDoc(inputStream, docFlavor, docAttributeSet);
					PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
					DocPrintJob job = impressoraPadrao.createPrintJob();
					job.print(doc, printRequestAttributeSet);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (tipo.equals("V")) {
				File pdf = new File(dir + "" + this.nomerelatorio + ".pdf");
				Desktop.getDesktop().open(pdf);
				System.out.println("open");
			}

			// JasperExportManager.exportReportToPdfFile(jasperPrint,
			// dir+"/"+"relatorio.pdf");
			if (file.isDirectory()) {
				File[] countFile = file.listFiles();
				for (File toDelete : countFile) {
					toDelete.delete();
				}
			}
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

}
