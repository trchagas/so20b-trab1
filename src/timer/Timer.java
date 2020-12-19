package timer;

import java.util.ArrayList;

import enums.Interrupcao;

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
		this.contador += this.intervalo;
	}
	
	public String verificaInterrupcao() {
		String interrupcaoTemp;
		
		for(int i = 0; i < filaInterrupcoes.size(); i++) {
			if(filaInterrupcoes.get(i).periodica) {
				if(this.contador % filaInterrupcoes.get(i).periodo == 0) {
					interrupcaoTemp = filaInterrupcoes.get(i).codigo;
					filaInterrupcoes.add(filaInterrupcoes.get(i));
					filaInterrupcoes.remove(filaInterrupcoes.get(i));
					return interrupcaoTemp;
				}
				
			}else {
				interrupcaoTemp = filaInterrupcoes.get(i).codigo;
				filaInterrupcoes.remove(filaInterrupcoes.get(i));
				return interrupcaoTemp;
			}
		}
		return " ";
	}
	
	public ArrayList<InterrupcaoTimer> getFilaInterrupcoes() {
		return this.filaInterrupcoes;
	}
	
	public int tempoAtual() {
		return this.contador;
	}
	
	public void pedeInterrupcao(boolean periodica, int periodo, String codigo) {
		filaInterrupcoes.add(new InterrupcaoTimer(periodica, periodo, codigo));
	}
	
	
}
