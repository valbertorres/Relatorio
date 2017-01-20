package com.intersys.sistema;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
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
import javax.swing.JOptionPane;

import com.intersys.relatorio.fabricaconexao.FabricaDeConexao;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
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
	private ProdutoTO produtoTO = new ProdutoTO();
	private String orderBy;
	private long chave;
	private int id;
	private boolean grupo;
	private boolean subgrupo;
	private boolean ambiente;
	private String tipo;
	private String dir_logo = "C:/sge/LOGO0.JPG";
	private String dir_chave;
	private String dir_av;
	private String dir_parcela;
	private boolean sem;
	private String evento;
	private File file;
	private File diretorio;
	private JasperPrint jasperPrint;

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
		this.dir_chave = "C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/src/main/java/com/intersys/sistema/sge_relatorio_chavetipo.jasper";
		this.parameters.put("dir_chave", this.dir_chave);

		this.dir_av = "C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/src/main/java/com/intersys/sistema/sge_relatorio_av.jasper";
		this.parameters.put("dir_av", this.dir_av);

		this.dir_parcela = "C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/src/main/java/com/intersys/sistema/sge_relatorio_vencimento.jasper";
		this.parameters.put("dir_parcelas", this.dir_parcela);

	}

	public void logo() {
		try {
			this.parameters.put("Logo", new FileInputStream(dir_logo));

		} catch (FileNotFoundException e) {
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
		this.nomerelatorio = String.valueOf(this.chave) + "_id_" + id;
		this.dir = "c:\\uploadPDF" + "\\";
		this.file = new File(dir);
		if (!file.exists()) {
			this.file.mkdir();
		}
		try {
			JasperExportManager.exportReportToPdfFile(this.jasperPrint, this.dir + "/" + this.nomerelatorio + ".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}

	}

	public void salvarPdf() {
		GerarImpressaoTO gerarImpressaoTO = new GerarImpressaoTO();
		dir = dir.replace("\\", "/");
		this.diretorio = new File(dir + "" + this.nomerelatorio + ".pdf");
		System.out.println(dir + "" + this.nomerelatorio + ".pdf");
		gerarImpressaoTO.setFile(diretorio);
		GerarImpressaoPO gerarImpressaoPO = new GerarImpressaoPO();
		gerarImpressaoPO.setGerarImpressaoTO(gerarImpressaoTO);
		gerarImpressaoPO.inserirPdf(id);
	}

	public void imprimirRelatorioPdf() {
		if (tipo.equals("I")) {
			System.out.println("entrou no i");
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
				JOptionPane.showMessageDialog(null, "Não há impressora encontrada!");
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

	public void gerarRelatorio() throws Exception {
		ProdutoFactory.setOrderBy(this.orderBy);

		cliente(this.chave);
		this.empresa();
		this.conexao();
		this.agrupamento();
		this.logo();
		this.subreport();

		try {

			InputStream relatorioSource = GerarRelatorio.class.getResourceAsStream("relatorio_pedido.jrxml");
			ByteArrayOutputStream relatorioOutputCompiled = new ByteArrayOutputStream();
			JasperCompileManager.compileReportToStream(relatorioSource, relatorioOutputCompiled);
			byte[] compiledReportData = relatorioOutputCompiled.toByteArray();
			relatorioOutputCompiled.close();

			jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(compiledReportData), this.parameters,
					jrdataSource());
			JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
			jasperViewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
			this.montarDir();

			this.salvarPdf();
			this.imprimirRelatorioPdf();

			if (tipo.equals("V")) {
				JOptionPane.showMessageDialog(null, "imprimir na tela ");
				File pdf = new File(dir + "" + this.nomerelatorio + ".pdf");
				// Desktop.getDesktop().open(pdf);
				System.out.println("open");
			}
			this.limparFolder();

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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public static void main(String[] args) {
		int i = 0;
		GerarRelatorio gerarRelatorio = new GerarRelatorio();
		gerarRelatorio.setChave(345640);
		gerarRelatorio.setOrderBy("");
		gerarRelatorio.setId(10);
		gerarRelatorio.setTipo("A");
		try {

			GerarRelatorio2 gerarRelatorio2 = new GerarRelatorio2();
			gerarRelatorio2.setGerarRelatorio(gerarRelatorio);
			gerarRelatorio2.gerarRelatorio2();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
