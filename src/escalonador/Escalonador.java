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
	
	public boolean processosDormindo(ArrayList<Job> filaJob) {
		for(Job job : filaJob) {
			if(job.getEstado() == EstadoJob.PRONTO)
				return false;
		}
		return true;
	}
	
	public Job getNextJob(ArrayList<Job> filaJob) {
		return null;
	}
}
