package com.intersys.sistema;

import java.awt.Desktop;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocFlavor.INPUT_STREAM;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JOptionPane;

import org.springframework.util.SystemPropertyUtils;

import com.ibm.icu.impl.Trie2.CharSequenceValues;
import com.intersys.relatorio.fabricaconexao.FabricaDeConexao;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.view.JasperViewer;

public class GerarRelatorio {
	public GerarRelatorio() {

	}

	private Map<String, Object> parameters = new HashMap();
	private String dir;
	private String nomerelatorio;
	private String orderBy;
	private long chave;
	private int id;
	private boolean grupo;
	private boolean subgrupo;
	private boolean ambiente;
	private String dir_chave;
	private String dir_av;
	private String dir_parcela;
	private boolean sem;
	private String evento;
	private File file;
	private File diretorio;
	private JasperPrint jasperPrint;
	private ProdutoTO produtoTO = new ProdutoTO();
	private int contador;
	private String impressora;
	Properties properties;
	private String dir_logo;
	private FileInputStream fileLogo;

	private void cliente(long chave) {
		ClienteTO clienteTO = new ClienteTO();
		clienteTO.setChave(chave);
		ClientePO clientePO = new ClientePO();
		clientePO.setClienteTO(clienteTO);
		clienteTO = clientePO.Cliente();
		this.parameters.put("cliente_nome", clienteTO.getNomeCliente());
		this.parameters.put("cliente_fantasia", clienteTO.getNomeFantasia());
		this.parameters.put("cliente_endereco", clienteTO.getEndereco());
		this.parameters.put("cliente_cidade", clienteTO.getCiadade());
		this.parameters.put("cliente_cnpj", clienteTO.getCnpj());
		this.parameters.put("cliente_vendedor", clienteTO.getVendedor());
		this.parameters.put("cliente_prazoEntrega", clienteTO.getPrazo());
		this.parameters.put("cliente_frete", clienteTO.getFrete());
		this.parameters.put("cliente_pontoRef", clienteTO.getPontoDeReferencia());
		this.parameters.put("cliente_bairro", clienteTO.getBairro());
		this.parameters.put("cliente_uf", clienteTO.getUf());
		this.parameters.put("cliente_fone", clienteTO.getContato());
		this.parameters.put("cliente_ie", clienteTO.getRg());
		this.parameters.put("cliente_data", clienteTO.getData());
		this.parameters.put("cliente_validade", clienteTO.getValidade());
		this.parameters.put("cliente_cep", clienteTO.getCep());
	}

	private void agrupamento() {
		this.parameters.put("exibir_subgrupo", this.subgrupo);
		this.parameters.put("exibir_grupo", this.grupo);
		this.parameters.put("exibir_ambiente", this.ambiente);
	}

	private void subreport() {
		InputStream subInputStream = this.getClass().getResourceAsStream("sge_relatorio_chavetipo.jasper");
		InputStream subInputStreamav = this.getClass().getResourceAsStream("sge_relatorio_av.jasper");
		InputStream subInputStreamvanc = this.getClass().getResourceAsStream("sge_relatorio_vencimento.jasper");
		this.parameters.put("dir_chave", subInputStream);
		this.parameters.put("dir_av", subInputStreamav);
		this.parameters.put("dir_parcelas", subInputStreamvanc);
	}

	private void logo() {
		try {
			Properties properties = FabricaDeConexao.getProperties();
			this.dir_logo = properties.getProperty("LOGO");
			this.fileLogo = new FileInputStream(dir_logo);
			this.parameters.put("Logo", fileLogo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void empresa() {
		EmpresaTO empresaTO = EmpresaPO.empresa();

		this.parameters.put("empresa_nome", empresaTO.getRazao());
		this.parameters.put("empresa_cnpj", empresaTO.getCnpj());
		this.parameters.put("empresa_endereco", empresaTO.getEndereco());
		this.parameters.put("empresa_fone", empresaTO.getFone());
		this.parameters.put("empresa_ie", empresaTO.getIe());
		this.parameters.put("empresa_bairro", empresaTO.getSetor());
		this.parameters.put("empresa_cep", empresaTO.getCep());
		this.parameters.put("empresa_cidade", empresaTO.getCidade());
		this.parameters.put("empresa_uf", empresaTO.getUf());
		this.parameters.put("empresa_email", empresaTO.getEmail());
	}

	private JRDataSource jrdataSource() {
		this.produtoTO.setChave(this.chave);
		ProdutoFactory.setProdutoTO(this.produtoTO);
		List<ProdutoTO> produtoTO2 = ProdutoFactory.listaProduto();
		JRDataSource jre = new JRBeanCollectionDataSource(produtoTO2);

		return jre;
	}

	private void conexao() {
		Connection connection;
		try {
			connection = FabricaDeConexao.getInstancia().getConnxao();
			this.parameters.put("con", connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void montarDir() {
		try {
			this.nomerelatorio = String.valueOf(this.chave) + "_id_" + id + "_" + contador;
			this.dir = "c:\\uploadPDF" + "\\";
			this.file = new File(dir);
			if (!file.exists()) {
				this.file.mkdir();
			}
			JasperExportManager.exportReportToPdfFile(this.jasperPrint, this.dir + "/" + this.nomerelatorio + ".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}

	}

	String imprimir;

	public void salvarPdf() {
		GerarImpressaoTO gerarImpressaoTO = new GerarImpressaoTO();
		imprimir = dir + "" + this.nomerelatorio + ".pdf";
		dir = dir.replace("\\", "/");
		this.diretorio = new File(dir + "" + this.nomerelatorio + ".pdf");
		System.out.println(dir + "" + this.nomerelatorio + ".pdf");
		gerarImpressaoTO.setFile(diretorio);
		GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();
		gerarImpressaoPO.setGerarImpressaoTO(gerarImpressaoTO);
		gerarImpressaoPO.inserirPdf(id);
	}

	public void imprimirRelatorioPdf() {

		if (evento.equals("I")) {
			// try {
			// JasperPrintManager.printPage(this.jasperPrint, 0, false);
			// } catch (JRException e) {
			// e.printStackTrace();
			// }

			try {
//				PrintService[] servicosImpressao = PrintServiceLookup
//						.lookupPrintServices(DocFlavor.INPUT_STREAM.AUTOSENSE, null);
//				PrintService impressora = PrintServiceLookup.lookupDefaultPrintService();
//				DocFlavor docFlavor = DocFlavor.INPUT_STREAM.PDF;
//				HashDocAttributeSet attributes = new HashDocAttributeSet();
//				FileInputStream fi = new FileInputStream(this.diretorio);
//				Doc documentoTexto = new SimpleDoc(fi, docFlavor, attributes);
//				PrintRequestAttributeSet printerAttributes = new HashPrintRequestAttributeSet();
//				printerAttributes.add(new Copies(0));  
//				DocPrintJob printJob = impressora.createPrintJob();
//				printJob.print(documentoTexto, printerAttributes);
				
				
				 PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                 printRequestAttributeSet.add(MediaSizeName.ISO_A4);
                 PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
                 printServiceAttributeSet.add(new PrinterName(this.impressora, null));
                 JRPrintServiceExporter exporter = new JRPrintServiceExporter();
                 exporter.setParameter(JRExporterParameter.JASPER_PRINT, this.jasperPrint);
                 exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
                 exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
                 exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
                 exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);

                 exporter.exportReport();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void limparFolder() {
		if (file.isDirectory()) {
			File[] countFile = file.listFiles();
			for (File toDelete : countFile) {
				toDelete.delete();
			}
		}
		file.delete();
	}

	public void openPdf() {
		if (this.evento.equals("V")) {
			JOptionPane.showMessageDialog(null, "imprimir na tela ");
			File pdf = new File(dir + "" + this.nomerelatorio + ".pdf");
			try {
				Desktop.getDesktop().open(pdf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void exibir_vencimento() {
		ChaveTO chaveTO = new ChaveTO();
		chaveTO.setChave(this.chave);

		ChavePO chavePO = new ChavePO();
		chavePO.setChaveTO(chaveTO);
		chaveTO = chavePO.chave();
		if (chaveTO.getTipoVenda().equals("Emprestimo de Mercadorias")) {
			this.parameters.put("exebir_vancimento", true);
		} else {
			this.parameters.put("exebir_vancimento", false);
		}

	}

	public void gerarRelatorio() throws Exception {
		ProdutoFactory.setOrderBy(this.orderBy);

		cliente(this.chave);
		this.empresa();
		this.conexao();
		this.agrupamento();
		this.logo();
		this.subreport();
		this.exibir_vencimento();

		try {

			InputStream relatorioSource = GerarRelatorio.class.getResourceAsStream("relatorio_pedido.jrxml");
			ByteArrayOutputStream relatorioOutputCompiled = new ByteArrayOutputStream();
			JasperCompileManager.compileReportToStream(relatorioSource, relatorioOutputCompiled);
			byte[] compiledReportData = relatorioOutputCompiled.toByteArray();
			relatorioOutputCompiled.close();

			this.jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(compiledReportData),
					this.parameters, jrdataSource());
			JasperViewer jasperViewer = new JasperViewer(this.jasperPrint, false);
			jasperViewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
			jasperViewer.setDefaultCloseOperation(JasperViewer.DISPOSE_ON_CLOSE);
			jasperViewer.setVisible(false);

			this.montarDir();
			this.imprimirRelatorioPdf();
			this.salvarPdf();
			this.limparFolder();
			this.fileLogo.close();

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

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getNomerelatorio() {
		return nomerelatorio;
	}

	public void setNomerelatorio(String nomerelatorio) {
		this.nomerelatorio = nomerelatorio;
	}

	public ProdutoTO getProdutoTO() {
		return produtoTO;
	}

	public void setProdutoTO(ProdutoTO produtoTO) {
		this.produtoTO = produtoTO;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public long getChave() {
		return chave;
	}

	public void setChave(long chave) {
		this.chave = chave;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isGrupo() {
		return grupo;
	}

	public void setGrupo(boolean grupo) {
		this.grupo = grupo;
	}

	public boolean isSubgrupo() {
		return subgrupo;
	}

	public void setSubgrupo(boolean subgrupo) {
		this.subgrupo = subgrupo;
	}

	public boolean isAmbiente() {
		return ambiente;
	}

	public void setAmbiente(boolean ambiente) {
		this.ambiente = ambiente;
	}

	public boolean isSem() {
		return sem;
	}

	public void setSem(boolean sem) {
		this.sem = sem;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

	public void setJasperPrint(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
	}

	public String getDir_logo() {
		return dir_logo;
	}

	public int getContador() {
		return contador;
	}

	public void setContador(int contador) {
		this.contador = contador;
	}

	public String getImpressora() {
		return impressora;
	}

	public void setImpressora(String impressora) {
		this.impressora = impressora;
	}

}
