package timer;

import enums.Interrupcao;

public class Timer {
	int contador, intervalo, termino;
	Interrupcao interrupcao;
	
	public Timer() {
		this.contador = 0;
		this.interrupcao = Interrupcao.NORMAL;
	}
	
	public void passagemInicio(int intervalo) {
		this.intervalo = intervalo;
	}
	
	public void passagemFim(int termino) {
		this.termino = termino;
	}
	
	public void contaPassagem() {
		this.contador += this.intervalo;
	}
	
	public Interrupcao verificaInterrupcao() {
		Interrupcao interrupcaoTemp = this.interrupcao;
		this.interrupcao = Interrupcao.NORMAL;
		return interrupcaoTemp;
	}
	
	public int tempoAtual() {
		return this.contador;
	}
	
	//public void pedeInterrupcao(tipo, periodo, codigo)
	
	
}
