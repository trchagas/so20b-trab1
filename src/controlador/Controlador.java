package controlador;

import cpu.Cpu;
import enums.Interrupcao;
import job.Job;
import so.SistemaOperacional;
import timer.InterrupcaoTimer;
import timer.Timer;

public class Controlador {
	Timer timer;
	
	public Controlador() {
		timer = new Timer();
	}
	
	public int controlaExecucao(Cpu cpu, SistemaOperacional so, Job job) {
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
		return timer.tempoAtual();
	}
}
