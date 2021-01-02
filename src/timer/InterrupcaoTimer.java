package timer;

public class InterrupcaoTimer {
	boolean periodica;
	int periodo;
	String codigo;
	int contadorInicial;
	boolean valida;
	
	public InterrupcaoTimer(boolean periodica, int periodo, String codigo, int contadorInicial) {
		this.periodica = periodica;
		this.periodo = periodo;
		this.codigo = codigo;
		this.contadorInicial = contadorInicial;
		this.valida = true;
	}
	
	public String getCodigo() {
		return this.codigo;
	}
	
	public void setContadorInicial(int novoContadorInicial) {
		this.contadorInicial = novoContadorInicial;
	}
	
	public boolean isPeriodica() {
		return this.periodica;
	}
	
	public void incrementaPeriodo() {
		this.contadorInicial += this.periodo;
	}
	
	public void invalidaInterrupcao() {
		this.valida = false;
	}
	
	public boolean isValida() {
		return this.valida;
	}
}
