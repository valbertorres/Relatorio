package com.intersys.relatorio.fabricaconexao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class GerarRelatorio {
	public GerarRelatorio() {
		// tempo t = new tempo();
		// t.start();
	}

	private Map<String, Object> parameters = new HashMap();
	/**
	 * 
	 */
	private String str = "C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/src/main/java/relatorio_pedido.jasper";

	private static String VerificarCNPJ(String cnpj) throws SQLException, Exception {
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

	public void imprimirRelatorio() throws Exception {

		ProdutoFactory.setOrderBy("order by pdcodgru");
		ClientePO.setChave(345640);
		List<ProdutoTO> produtoTO = ProdutoFactory.listaProduto();
		List<ClienteTO> cliente = ClientePO.getInstancia().Cliente();
		List<ChaveTO> listaTipoChve = ChavePO.chave();
		List<VenciementoTO> listaVencimento = VencimentoPO.vencimento();
		List<ParcelasTO> listaparcelas = ParcelasPO.parcelas();
		List<ClienteTO> nomecliente = ClientePO.getInstancia().Cliente();
		JRDataSource jre = new JRBeanCollectionDataSource(produtoTO);
		JRDataSource listaCliente = new JRBeanCollectionDataSource(cliente);
		JRDataSource listaCahveTipo = new JRBeanCollectionDataSource(listaTipoChve);
		JRDataSource listavencimeto = new JRBeanCollectionDataSource(listaVencimento);
		JRDataSource listaParcelas = new JRBeanCollectionDataSource(listaparcelas);
		JRDataSource nomeCliente = new JRBeanCollectionDataSource(nomecliente);

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
			this.parameters.put("exibir_group", true);
			this.parameters.put("exibir_colum_header", true);
			this.parameters.put("connection", listaCliente);
			this.parameters.put("DataSource_chaveTipo", listaCahveTipo);
			this.parameters.put("DataSource_Vencimento", listavencimeto);
			this.parameters.put("DataSource_parcelas", listaParcelas);
			this.parameters.put("DataSource_empresanome", nomeCliente);
			this.parameters.put("exibir_subgurpo", false);
			// int codigo = Integer.parseInt(lista1().get(0));
			// this.parameters.put("p1chave", codigo);
			// this.p1chave = new BigDecimal(codigo);
			this.parameters.put("Logo", new FileInputStream("C:/sge/LOGO0.JPG"));

			InputStream relatorioSource = GerarRelatorio.class.getResourceAsStream("relatorio_pedido.jrxml");
			ByteArrayOutputStream relatorioOutputCompiled = new ByteArrayOutputStream();
			JasperCompileManager.compileReportToStream(relatorioSource, relatorioOutputCompiled);
			byte[] compiledReportData = relatorioOutputCompiled.toByteArray();
			relatorioOutputCompiled.close();

			JasperPrint jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(compiledReportData), this.parameters, jre);
			JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
			jasperViewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
			jasperViewer.setVisible(true);
			// JasperPrintManager.printPage(jasperPrint, 0, false);
			// JasperExportManager.exportReportToPdfFile(jasperPrint,
			// "C:/sge/relatorio2.pdf");

		} catch (Exception e) {
			e.printStackTrace();
		}
		// atualizar();

	}

	static GerarRelatorio gerarRelatorio = new GerarRelatorio();

	public static void main(String[] args) throws SQLException, Exception {

		// int lista = lista1().size();
		// if (lista != 0) {
		gerarRelatorio.imprimirRelatorio();
		// }

	}
	
	
	static int t = 0;

	public class tempo extends Thread {
		int i = 0;

		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
					i++;
				} catch (Exception e) {
				}
				System.out.println(i);

				switch (i) {
				case 10:
					try {
//						int lista = lista1().size();
//						if (lista != 0) {
//							gerarRelatorio.imprimirRelatorio();
//						} else {
//							i = 0;
//						}
//						i = 0;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
	}
}
