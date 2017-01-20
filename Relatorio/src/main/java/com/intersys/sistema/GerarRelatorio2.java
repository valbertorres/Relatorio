package com.intersys.sistema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GerarRelatorio2 {
	private Map<String, Object> parametro = new HashMap<>();
	private GerarRelatorio gerarRelatorio;
	private String dir_1;
	private JasperPrint jasperPrint;
	private String dir_logo;
	private boolean imprimir_via;

	private void empresa() {
		EmpresaTO empresaTO = EmpresaPO.empresa();
		this.parametro.put("empresa_nome", empresaTO.getRazao());
		this.parametro.put("empresa_fone", empresaTO.getFone());
		this.parametro.put("empresa_uf", empresaTO.getUf());
		this.parametro.put("empresa_cep", empresaTO.getCep());
		this.parametro.put("empresa_cidade", empresaTO.getCidade());
		this.parametro.put("empresa_ie", empresaTO.getIe());
		this.parametro.put("empresa_cnpj", empresaTO.getCnpj());
		this.parametro.put("empresa_email", empresaTO.getEmail());
		this.parametro.put("empresa_endereco", empresaTO.getEndereco());
	}

	private void produto() {
		ProdutoTO produtoTO = new ProdutoTO();
		produtoTO.setChave(gerarRelatorio.getChave());
		ProdutoFactory.setProdutoTO(produtoTO);
		List<ProdutoTO> listaprodutos = ProdutoFactory.listaProduto();
		JRDataSource produto2 = new JRBeanCollectionDataSource(listaprodutos);
		JRDataSource produto3 = new JRBeanCollectionDataSource(listaprodutos);

		this.parametro.put("collection", produto2);
		this.parametro.put("collection2", produto3);
		this.parametro.put("imprimir_via", this.imprimir_via);
	}

	private JRDataSource jrdataSource() {
		ProdutoTO produtoTO = new ProdutoTO();
		produtoTO.setChave(this.gerarRelatorio.getChave());
		ProdutoFactory.setProdutoTO(produtoTO);
		List<ProdutoTO> listaprodutos = ProdutoFactory.listaProduto();
		JRDataSource produto = new JRBeanCollectionDataSource(listaprodutos);

		return produto;
	}

	private void cliente() {

		ClienteTO clienteTO = new ClienteTO();
		clienteTO.setChave(this.gerarRelatorio.getChave());
		ClientePO clientePO = new ClientePO();
		clientePO.setClienteTO(clienteTO);
		clienteTO = clientePO.Cliente();

		this.parametro.put("cliente_nome", clienteTO.getNomeCliente());
		this.parametro.put("cliente_endereco", clienteTO.getEndereco());
		this.parametro.put("cliente_cidade", clienteTO.getCiadade());
		this.parametro.put("cliente_cnpj", clienteTO.getCnpj());
		this.parametro.put("cliente_vendedor", clienteTO.getVendedor());
		this.parametro.put("cliente_fone", clienteTO.getContato());
		this.parametro.put("cliente_bairro", clienteTO.getBairro());
		this.parametro.put("cliente_uf", clienteTO.getUf());
		this.parametro.put("cliente_ie", clienteTO.getRg());
		this.parametro.put("cliente_cep", clienteTO.getCep());
	}

	private void logo() {
		this.dir_logo = this.gerarRelatorio.getDir_logo();
		try {
			this.parametro.put("Logo", new FileInputStream(dir_logo));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void subreport() {
		this.dir_1 = "C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/src/main/java/com/intersys/sistema/sge_relatorio2_subreport1.jasper";
		this.parametro.put("dir_1", this.dir_1);
		this.parametro.put("dir_2", this.dir_1);

	}

	private void montarDir() {
		gerarRelatorio.setJasperPrint(this.jasperPrint);
		this.gerarRelatorio.montarDir();
	}

	private void salvarPdf() {
		this.gerarRelatorio.salvarPdf();
	}

	private void imprimirPdf() {
		this.gerarRelatorio.setJasperPrint(this.jasperPrint);
		gerarRelatorio.imprimirRelatorioPdf();
	}

	private void openPdf() {
		this.gerarRelatorio.openPdf();
	}

	public void gerarRelatorio2() {
		this.produto();
		this.cliente();
		this.empresa();
		this.logo();
		this.subreport();

		try {
			InputStream inputStream = GerarRelatorio2.class.getResourceAsStream("sge_relatorio_vias.jrxml");
			ByteArrayOutputStream compileRelatorio = new ByteArrayOutputStream();
			JasperCompileManager.compileReportToStream(inputStream, compileRelatorio);
			byte[] relatorioCompileReporte = compileRelatorio.toByteArray();
			compileRelatorio.close();
			jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(relatorioCompileReporte), parametro,
					jrdataSource());
			this.montarDir();
			this.salvarPdf();
			this.imprimirPdf();
			// JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
			// jasperViewer.setExtendedState(jasperViewer.MAXIMIZED_BOTH);
			// jasperViewer.setVisible(true);

		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public GerarRelatorio getGerarRelatorio() {
		return gerarRelatorio;
	}

	public void setGerarRelatorio(GerarRelatorio gerarRelatorio) {
		this.gerarRelatorio = gerarRelatorio;
	}

	public boolean isImprimir_via() {
		return imprimir_via;
	}

	public void setImprimir_via(boolean imprimir_via) {
		this.imprimir_via = imprimir_via;
	}

}
