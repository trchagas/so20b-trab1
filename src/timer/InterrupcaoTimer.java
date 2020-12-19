package timer;

import enums.Interrupcao;

public class InterrupcaoTimer {
	boolean periodica;
	int periodo;
	String codigo;
	
	public InterrupcaoTimer(boolean periodica, int periodo, String codigo) {
		this.periodica = periodica;
		this.periodo = periodo;
		this.codigo = codigo;
	}
}
