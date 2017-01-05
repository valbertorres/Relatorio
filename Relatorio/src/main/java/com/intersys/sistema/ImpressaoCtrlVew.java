package com.intersys.sistema;

public class ImpressaoCtrlVew {

	private static ImpressaoCtrlVew instancia;

	public static synchronized ImpressaoCtrlVew getinstancia() {
		if (instancia == null) {
			instancia = new ImpressaoCtrlVew();
		}
		return instancia;
	}

	private ImpressaoVew impressaoVew;

	public void inicializar(){
		
	}
	
	public ImpressaoVew getImpressaoVew() {
		return impressaoVew;
	}

	public void setImpressaoVew(ImpressaoVew impressaoVew) {
		this.impressaoVew = impressaoVew;
	}

}
