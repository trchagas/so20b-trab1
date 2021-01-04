package escalonador;

import java.util.ArrayList;

import enums.EstadoJob;
import job.Job;

public class Escalonador {
	
	public boolean haProcesso(ArrayList<Job> filaJob) {
		for(Job job : filaJob) {
			if(job.getEstado() != EstadoJob.TERMINADO)
				return true;
		}
		return false;
	}
	
	public boolean processosBloqueados(ArrayList<Job> filaJob) {
		for(Job job : filaJob) {
			if(job.getEstado() == EstadoJob.PRONTO)
				return false;
		}
		return true;
	}
	
	public Job getNextJob(ArrayList<Job> filaJob) {
		float maiorPrioridade = 0;
		Job job = null;
		for(int i=0; i < filaJob.size(); i++) {
			if(filaJob.get(i).getPrioridade() > maiorPrioridade && filaJob.get(i).getEstado() == EstadoJob.PRONTO) {
				maiorPrioridade = filaJob.get(i).getPrioridade();
				job = filaJob.get(i);
			}
		}
		return job;
	}
}
