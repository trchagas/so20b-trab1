package timer;

import enums.Interrupcao;

public class InterrupcaoTimer {
	boolean periodica;
	int periodo;
	Interrupcao codigo;
	
	public InterrupcaoTimer(boolean periodica, int periodo, Interrupcao codigo) {
		this.periodica = periodica;
		this.periodo = periodo;
		this.codigo = codigo;
	}
}
