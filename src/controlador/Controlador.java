package controlador;

import cpu.Cpu;
import enums.InterrupcaoCPU;
import job.Job;
import so.SistemaOperacional;
import timer.InterrupcaoTimer;
import timer.Timer;

public class Controlador {
	
	public Controlador() {
		
	}
	
	public void controlaExecucao(Cpu cpu, SistemaOperacional so, Job job, Timer timer) {
		while(cpu.getCodigotInterrupcao() == InterrupcaoCPU.NORMAL || cpu.getCodigotInterrupcao() == InterrupcaoCPU.DORMINDO) {
			
			System.out.println("Tempo do timer: " + timer.tempoAtual());
			timer.contaPassagem();
			
			if(cpu.getCodigotInterrupcao() == InterrupcaoCPU.NORMAL)
				cpu.executa();
			
			if(cpu.getCodigotInterrupcao() == InterrupcaoCPU.INSTRUCAO_ILEGAL || cpu.getCodigotInterrupcao() == InterrupcaoCPU.VIOLACAO_DE_MEMORIA) 
				so.trataInterrupcao(cpu.getCodigotInterrupcao(), cpu.instrucaoAtual(), job, timer);
				
			for(int i = 0; i < timer.getFilaInterrupcoes().size(); i++) {
				String interrupcaoTimer = timer.verificaInterrupcao(i);
				boolean isPeriodica = timer.getFilaInterrupcoes().get(i).isPeriodica();
				so.trataInterrupcaoTimer(interrupcaoTimer, job, isPeriodica);	
			}	
			timer.limpaFilaInterrupcoes();
		}
	}
}
