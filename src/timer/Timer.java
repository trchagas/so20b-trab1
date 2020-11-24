package timer;

public class Timer {
	int interrupcao, contador, intervalo, termino;
	
	public Timer() {
		this.interrupcao = 0;
		this.contador = 0;
		this.intervalo = 0;
		this.termino = 0;
	}
	
	public void passagemInicio(int intervalo, String mensagem) {
		
	}
	
	public void passagemFim(int termino, String mensagem) {
		
	}
	
	public void contaPassagem() {
		
	}
	
	public int verificaInterrupcao() {
		return this.interrupcao;
	}
	
	public int tempoAtual() {
		return this.contador;
	}
	
	
	
	
}
