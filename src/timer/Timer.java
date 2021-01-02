package timer;

import java.util.ArrayList;

import enums.InterrupcaoCPU;

public class Timer {
	int contador, intervalo, termino;
	//Interrupcao interrupcao;
	ArrayList<InterrupcaoTimer> filaInterrupcoes; 
	
	public Timer() {
		this.contador = 0;
		//this.interrupcao = Interrupcao.NORMAL;
		this.filaInterrupcoes = new ArrayList<InterrupcaoTimer>();
	}
	
	public void passagemInicio(int intervalo) {
		this.intervalo = intervalo;
	}
//	
//	public void passagemFim(int termino) {
//		this.termino = termino;
//	}
	
	public void contaPassagem() {
		this.contador += 1;
	}
	
	public String verificaInterrupcao(int i) {
		InterrupcaoTimer interrupcao = this.filaInterrupcoes.get(i);
		
		if(this.contador == interrupcao.contadorInicial + interrupcao.periodo) {
			if(interrupcao.periodica) {
				interrupcao.incrementaPeriodo();
				this.filaInterrupcoes.add(new InterrupcaoTimer(interrupcao.periodica, interrupcao.periodo, interrupcao.codigo, interrupcao.contadorInicial));
				
				interrupcao.invalidaInterrupcao();
				return interrupcao.codigo;
			}
			else {
				interrupcao.invalidaInterrupcao();
				return interrupcao.codigo;
			}	
		}
		
		return "Nao ha interrupcao";
	}
	
	public ArrayList<InterrupcaoTimer> getFilaInterrupcoes() {
		return this.filaInterrupcoes;
	}
	
	public int tempoAtual() {
		return this.contador;
	}
	
	public void pedeInterrupcao(boolean periodica, int periodo, String codigo, int contadorInicial) {
		this.filaInterrupcoes.add(new InterrupcaoTimer(periodica, periodo, codigo, contadorInicial));
	}
	
	public void limpaFilaInterrupcoes() {
		for(int i = 0; i < this.filaInterrupcoes.size(); i++) {
			if(!this.filaInterrupcoes.get(i).isValida())
				this.filaInterrupcoes.remove(i);
		}
	}
}
