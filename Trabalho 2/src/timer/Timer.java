package timer;

import java.util.ArrayList;

public class Timer {
	int contador, intervalo, termino;
	ArrayList<InterrupcaoTimer> filaInterrupcoes; 
	
	public Timer() {
		contador = 0;
		filaInterrupcoes = new ArrayList<InterrupcaoTimer>();
	}
	
	public void passagemInicio(int intervalo) {
		this.intervalo = intervalo;
	}
	
	public void contaPassagem() {
		contador += 1;
	}
	
	public String verificaInterrupcao(int i) {
		InterrupcaoTimer interrupcao = filaInterrupcoes.get(i);
		
		if(contador == interrupcao.contadorInicial + interrupcao.periodo) {
			if(interrupcao.periodica) {
				interrupcao.incrementaPeriodo();
				filaInterrupcoes.add(new InterrupcaoTimer(interrupcao.idJob , interrupcao.periodica, interrupcao.periodo, interrupcao.codigo, interrupcao.contadorInicial));
				
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
		return filaInterrupcoes;
	}
	
	public int tempoAtual() {
		return contador;
	}
	
	public void pedeInterrupcao(int idJob ,boolean periodica, int periodo, String codigo, int contadorInicial) {
		filaInterrupcoes.add(new InterrupcaoTimer(idJob ,periodica, periodo, codigo, contadorInicial));
	}
	
	public void limpaFilaInterrupcoes() {
		for(int i = 0; i < filaInterrupcoes.size(); i++) {
			if(!filaInterrupcoes.get(i).isValida())
				filaInterrupcoes.remove(i);
		}
	}
}
