package controlador;

import java.util.ArrayList;

import cpu.Cpu;
import enums.EstadoJob;
import enums.InterrupcaoCPU;
import job.Job;
import so.SistemaOperacional;
import timer.InterrupcaoTimer;
import timer.Timer;

public class Controlador {
	
	public void controlaExecucao(Cpu cpu, SistemaOperacional so, Job job, Timer timer, ArrayList<Job> filaJob) {
		while(cpu.getCodigotInterrupcao() == InterrupcaoCPU.NORMAL || cpu.getCodigotInterrupcao() == InterrupcaoCPU.DORMINDO && job.getEstado() != EstadoJob.TERMINADO) {
			
			System.out.println("Tempo do timer: " + timer.tempoAtual());
			
			if(cpu.getCodigotInterrupcao() == InterrupcaoCPU.NORMAL && job.getEstado() == EstadoJob.PRONTO)
				cpu.executa();
			
			if(cpu.getCodigotInterrupcao() == InterrupcaoCPU.INSTRUCAO_ILEGAL || cpu.getCodigotInterrupcao() == InterrupcaoCPU.VIOLACAO_DE_MEMORIA) 
				so.trataInterrupcao(cpu.getCodigotInterrupcao(), cpu.instrucaoAtual(), job, timer);
				
			for(int i = 0; i < timer.getFilaInterrupcoes().size(); i++) {
				int idCorrespondente = timer.getFilaInterrupcoes().get(i).getIdJob();
				String interrupcaoTimer = timer.verificaInterrupcao(i);
				boolean isPeriodica = timer.getFilaInterrupcoes().get(i).isPeriodica();
				so.trataInterrupcaoTimer(interrupcaoTimer, isPeriodica, filaJob, idCorrespondente);	
			}	
			timer.limpaFilaInterrupcoes();
			timer.contaPassagem();
			
			if(job.getEstado() == EstadoJob.BLOQUEADO)
				return;
		}
	}
}
