package com.intersys.sistema;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

public class ImpressaoCtrlVew {

	private static ImpressaoCtrlVew instancia;

	public static synchronized ImpressaoCtrlVew getinstancia() {
		if (instancia == null) {
			instancia = new ImpressaoCtrlVew();
		}
		return instancia;
	}

	private ImpressaoVew impressaoVew;

	private JButton imprimir;
	private JButton salvar;
	private JButton cancelar;
	private JCheckBox boxNumItem;
	private JCheckBox boxDescricao;
	private JCheckBox boxCodProduto;
	private JCheckBox boxGruSubgruDescricao;
	private JCheckBox boxGruSubgruCodigo;
	private JCheckBox boxGruCodigo;
	private JCheckBox boxGruDescricao;
	private JCheckBox boxSecao;
	private JCheckBox boxGrupoAgru;
	private JCheckBox boxAmbienteAgru;
	private JCheckBox boxSemAgru;
	private JCheckBox boxGruSubgrupoAgru;
	private JCheckBox boxImprimirAuto;

	private String ordemOmpressao;

	public void inicializar() {
		this.inicalizarComponente();
		this.inicializarListene();
		// tempo t = new tempo();
		// t.start();
	}

	private void inicalizarComponente() {
		this.imprimir = this.impressaoVew.getBtnImprimir();
		this.cancelar = this.impressaoVew.getBtnCancelar();
		this.salvar = this.impressaoVew.getBtnSalvar();
		this.boxAmbienteAgru = this.impressaoVew.getBoxAmbienteAgru();
		this.boxCodProduto = this.impressaoVew.getBoxCodProduto();
		this.boxDescricao = this.impressaoVew.getBoxDescricao();
		this.boxGruCodigo = this.impressaoVew.getBoxGruCodigo();
		this.boxGruDescricao = this.impressaoVew.getBoxGruDescricao();
		this.boxGrupoAgru = this.impressaoVew.getBoxGrupoAgru();
		this.boxGruSubgruCodigo = this.impressaoVew.getBoxGruSubgruCodigo();
		this.boxGruSubgruDescricao = this.impressaoVew.getBoxGruSubgruDescricao();
		this.boxGruSubgrupoAgru = this.impressaoVew.getBoxGruSubgrupoAgru();
		this.boxImprimirAuto = this.impressaoVew.getBoxImprimirAuto();
		this.boxNumItem = this.impressaoVew.getBoxNumItem();
		this.boxSecao = this.impressaoVew.getBoxSecao();
		this.boxSemAgru = this.impressaoVew.getBoxSemAgru();

	}

	private void inicializarListene() {
		this.boxSemAgru.setSelected(true);
		this.boxNumItem.setSelected(true);
		this.salvar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		this.imprimir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selecaoimpressao();
				System.out.println(ordemOmpressao);

			}
		});

		this.cancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
	}

	private void selecaoimpressao() {
		if (this.boxAmbienteAgru.isSelected()) {
			this.ordemOmpressao = "order by p2ambiente";
		}

		if (this.boxCodProduto.isSelected()) {
			this.ordemOmpressao = "order by pdcodpro";
		}

		if (this.boxDescricao.isSelected()) {
			this.ordemOmpressao = "order by pdnome";
		}

		if (this.boxGruCodigo.isSelected()) {
			this.ordemOmpressao = "order by pdcodgru,pdcodpro";
		}

		if (this.boxGruDescricao.isSelected()) {
			this.ordemOmpressao = "order by pdcodgru,pdnome";
		}

		if (this.boxGruSubgruCodigo.isSelected()) {
			this.ordemOmpressao = "order by pdcodgru,pdcodram,pdcodpro";
		}

		if (this.boxGruSubgruDescricao.isSelected()) {
			this.ordemOmpressao = "order by pdcodgru,pdcodram,pdnome";
		}

		if (this.boxNumItem.isSelected()) {
			this.ordemOmpressao = "order by p2item";
		}

		if (this.boxSecao.isSelected()) {
			this.ordemOmpressao = "order by pdsecao";
		}

	}

	public void imprimirAuto() {
		GerarRelatorio gerarRelatorio = new GerarRelatorio();
		String tipoI = "I";
		String tipoB = "B";
		String tipoV = "V";
		List<GerarImpressaoTO> listaImpressao = GerarImpressaoPO.getinstancia().impressaoLista();

		for (GerarImpressaoTO gerarImpressaoTO : listaImpressao) {
			if (gerarImpressaoTO.getTipoEvento() == tipoI) {
				// gerarRelatorio.imprimirRelatorio(tipoV, t);
			}
		}

	}

	public ImpressaoVew getImpressaoVew() {
		return impressaoVew;
	}

	public void setImpressaoVew(ImpressaoVew impressaoVew) {
		this.impressaoVew = impressaoVew;
	}

	public JButton getImprimir() {
		return imprimir;
	}

	public void setImprimir(JButton imprimir) {
		this.imprimir = imprimir;
	}

	public JButton getSalvar() {
		return salvar;
	}

	public void setSalvar(JButton salvar) {
		this.salvar = salvar;
	}

	public JButton getCancelar() {
		return cancelar;
	}

	public void setCancelar(JButton cancelar) {
		this.cancelar = cancelar;
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
						// int lista = lista1().size();
						// if (lista != 0) {
						// gerarRelatorio.imprimirRelatorio();
						// } else {
						// i = 0;
						// }
						// i = 0;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
	}
}
