package timer;

public class InterrupcaoTimer {
	int idJob;
	boolean periodica;
	int periodo;
	String codigo;
	int contadorInicial;
	boolean valida;
	
	public InterrupcaoTimer(int idJob ,boolean periodica, int periodo, String codigo, int contadorInicial) {
		this.idJob = idJob;
		this.periodica = periodica;
		this.periodo = periodo;
		this.codigo = codigo;
		this.contadorInicial = contadorInicial;
		valida = true;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setContadorInicial(int novoContadorInicial) {
		contadorInicial = novoContadorInicial;
	}
	
	public boolean isPeriodica() {
		return periodica;
	}
	
	public void incrementaPeriodo() {
		contadorInicial += periodo;
	}
	
	public void invalidaInterrupcao() {
		valida = false;
	}
	
	public boolean isValida() {
		return valida;
	}
	
	public int getIdJob() {
		return idJob;
	}
	
	public int getPeriodo() {
		return periodo;
	}
}
