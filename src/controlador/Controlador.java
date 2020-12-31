package controlador;

import cpu.Cpu;
import enums.Interrupcao;
import job.Job;
import so.SistemaOperacional;
import timer.InterrupcaoTimer;
import timer.Timer;

public class Controlador {
	
	public Controlador() {
		
	}
	
	public void controlaExecucao(Cpu cpu, SistemaOperacional so, Job job, Timer timer) {
		while(cpu.getCodigotInterrupcao() == Interrupcao.NORMAL || cpu.getCodigotInterrupcao() == Interrupcao.DORMINDO) {
			
			System.out.println("Tempo do timer: " + timer.tempoAtual());
			timer.contaPassagem();
			
			if(cpu.getCodigotInterrupcao() == Interrupcao.NORMAL)
				cpu.executa();
			
			if(cpu.getCodigotInterrupcao() == Interrupcao.INSTRUCAO_ILEGAL || cpu.getCodigotInterrupcao() == Interrupcao.VIOLACAO_DE_MEMORIA) 
				so.trataInterrupcao(cpu.getCodigotInterrupcao(), job, timer);
				
			for(int i = 0; i < timer.getFilaInterrupcoes().size(); i++) {
				so.trataInterrupcaoTimer(timer.verificaInterrupcao(), timer, job);
			}	
		}
	}
}
