package timer;

import enums.Interrupcao;

public class InterrupcaoTimer {
	boolean periodica;
	int periodo;
	String codigo;
	int contadorInicial;
	
	public InterrupcaoTimer(boolean periodica, int periodo, String codigo, int contadorInicial) {
		this.periodica = periodica;
		this.periodo = periodo;
		this.codigo = codigo;
		this.contadorInicial = contadorInicial;
	}
	
	public String getCodigo() {
		return this.codigo;
	}
	
	public void setContadorInicial(int novoContadorInicial) {
		this.contadorInicial = novoContadorInicial;
	}
}
