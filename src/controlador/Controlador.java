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
		int contadorUsoCpu = 0;
		while(cpu.getCodigotInterrupcao() == InterrupcaoCPU.NORMAL || cpu.getCodigotInterrupcao() == InterrupcaoCPU.DORMINDO) { //tirar dormindo aqui
			if(cpu.getCodigotInterrupcao() == InterrupcaoCPU.DORMINDO)
				System.out.println("teste");
			
			System.out.println("Tempo do timer: " + timer.tempoAtual());
			
			if(cpu.getCodigotInterrupcao() == InterrupcaoCPU.NORMAL)
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
			contadorUsoCpu+=1;
			
			if(contadorUsoCpu == job.getQuantum()) {
				timer.pedeInterrupcao(job.getId(), false, 1, "Processo bloqueado por limite de quantum", timer.tempoAtual());
				job.setEstado(EstadoJob.BLOQUEADO);
				System.out.println("Quantum do processo com ID: " + job.getId() + " atingido. Bloqueando execucao.");
				return;
			}
			
			if(job.getEstado() == EstadoJob.BLOQUEADO) {
				job.recalculaPrioridade(contadorUsoCpu/job.getQuantum());
			}	
		}
	}
}
