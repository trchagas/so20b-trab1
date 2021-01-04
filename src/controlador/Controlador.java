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
	
	public int controlaExecucao(Cpu cpu, SistemaOperacional so, Job jobAtual, Timer timer, ArrayList<Job> filaJob) {
		int contadorUsoCpu = 0;
		while(cpu.getCodigotInterrupcao() == InterrupcaoCPU.NORMAL) {
			
			System.out.println("Tempo do timer: " + timer.tempoAtual());
		
			cpu.executa();
			
			if(cpu.getCodigotInterrupcao() == InterrupcaoCPU.INSTRUCAO_ILEGAL || cpu.getCodigotInterrupcao() == InterrupcaoCPU.VIOLACAO_DE_MEMORIA) 
				so.trataInterrupcao(cpu.getCodigotInterrupcao(), cpu.instrucaoAtual(), jobAtual, timer);
				
			for(int i = 0; i < timer.getFilaInterrupcoes().size(); i++) {
				int idCorrespondente = timer.getFilaInterrupcoes().get(i).getIdJob();
				String interrupcaoTimer = timer.verificaInterrupcao(i);
				boolean isPeriodica = timer.getFilaInterrupcoes().get(i).isPeriodica();
				so.trataInterrupcaoTimer(interrupcaoTimer, isPeriodica, filaJob, idCorrespondente);	
			}	
			timer.limpaFilaInterrupcoes();
			timer.contaPassagem();
			jobAtual.incrementaTempoExecutando();
			
			for(Job job : filaJob) {
				if(job.getEstado() == EstadoJob.BLOQUEADO)
					job.incrementaTempoBloqueado();
			}
			
			contadorUsoCpu+=1;
			
			if(contadorUsoCpu == jobAtual.getQuantum()) {
				timer.pedeInterrupcao(jobAtual.getId(), false, 1, "Processo bloqueado por limite de quantum", timer.tempoAtual());
				jobAtual.setEstado(EstadoJob.BLOQUEADO);
				//jobAtual.incrementaVezesBloqueado();
				System.out.println("Quantum do processo com ID: " + jobAtual.getId() + " atingido. Bloqueando execucao.");
				//return contadorUsoCpu;
			}
			
			if(jobAtual.getEstado() == EstadoJob.BLOQUEADO) {
				jobAtual.incrementaVezesBloqueado();
				jobAtual.recalculaPrioridade(contadorUsoCpu/jobAtual.getQuantum());
				return contadorUsoCpu;
			}
		}
		return contadorUsoCpu;
	}
}
