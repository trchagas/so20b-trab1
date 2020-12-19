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
	
	public void controlaExecucao(Cpu cpu, SistemaOperacional so, Job job) {
		while(cpu.getCodigotInterrupcao() == Interrupcao.NORMAL) {
			
			timer.contaPassagem();
			System.out.println("Tempo do timer: " + timer.tempoAtual());
			
			cpu.executa();
			
			if(cpu.getCodigotInterrupcao() == Interrupcao.INSTRUCAO_ILEGAL || cpu.getCodigotInterrupcao() == Interrupcao.VIOLACAO_DE_MEMORIA) 
				so.trataInterrupcao(cpu.getCodigotInterrupcao(), job, timer);
				
			for(int i = 0; i < timer.getFilaInterrupcoes().size(); i++) {
				so.trataInterrupcaoTimer(timer.verificaInterrupcao(), job);
			}	
		}
	}
}
