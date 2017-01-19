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
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class GerarRelatorio2 {
	public static void main(String[] args) throws FileNotFoundException {

		ProdutoTO produtoTO = new ProdutoTO();
		produtoTO.setChave(345640);
		ProdutoFactory.setProdutoTO(produtoTO);
		List<ProdutoTO> listaprodutos = ProdutoFactory.listaProduto();
		JRDataSource produto = new JRBeanCollectionDataSource(listaprodutos);
		JRDataSource produto2 = new JRBeanCollectionDataSource(listaprodutos);
		JRDataSource produto3 = new JRBeanCollectionDataSource(listaprodutos);

		ClienteTO clienteTO = new ClienteTO();
		clienteTO.setChave(345640);
		ClientePO clientePO = new ClientePO();
		clientePO.setClienteTO(clienteTO);
		clienteTO = clientePO.Cliente();
		EmpresaTO empresaTO = EmpresaPO.empresa();

		Map<String, Object> parametro = new HashMap<>();

		// Dados da empresas
		parametro.put("empresa_nome", empresaTO.getRazao());
		parametro.put("empresa_fone", empresaTO.getFone());
		parametro.put("empresa_uf", empresaTO.getUf());
		parametro.put("empresa_cep", empresaTO.getCep());
		parametro.put("empresa_cidade", empresaTO.getCidade());
		parametro.put("empresa_ie", empresaTO.getIe());
		parametro.put("empresa_cnpj", empresaTO.getCnpj());
		parametro.put("empresa_email", empresaTO.getEmail());
		parametro.put("empresa_endereco", empresaTO.getEndereco());

		// Dados do cliente
		parametro.put("cliente_nome", clienteTO.getNomeCliente());
		parametro.put("cliente_endereco", clienteTO.getEndereco());
		parametro.put("cliente_cidade", clienteTO.getCiadade());
		parametro.put("cliente_cnpj", clienteTO.getCnpj());
		parametro.put("cliente_vendedor", clienteTO.getVendedor());
		parametro.put("cliente_fone", clienteTO.getContato());
		parametro.put("cliente_bairro", clienteTO.getBairro());
		parametro.put("cliente_uf", clienteTO.getUf());
		parametro.put("cliente_ie", clienteTO.getRg());
		parametro.put("cliente_cep", clienteTO.getCep());
		parametro.put("collection", produto2);
		parametro.put("collection2", produto3);

		parametro.put("Logo", new FileInputStream("C:/sge/LOGO0.JPG"));

		try {
			System.out.println("compilando....");
			InputStream inputStream = GerarRelatorio2.class.getResourceAsStream("sge_relatorio_vias.jrxml");
			System.out.println("input....");
			ByteArrayOutputStream compileRelatorio = new ByteArrayOutputStream();
			System.out.println("compileRelatorio....");
			JasperCompileManager.compileReportToStream(inputStream, compileRelatorio);
			System.out.println("jaseprComplie....");
			byte[] relatorioCompileReporte = compileRelatorio.toByteArray();
			System.out.println("relatorio byte....");
			compileRelatorio.close();
			System.out.println("Close....");
			JasperPrint jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(relatorioCompileReporte),
					parametro, produto);
			System.out.println("print....");

			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "c:/gerado.pdf");

			JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
			System.out.println("view....");
			jasperViewer.setExtendedState(jasperViewer.MAXIMIZED_BOTH);
			System.out.println("maximized....");
			jasperViewer.setVisible(true);
			System.out.println("gerando....");

		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
