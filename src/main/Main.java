package main;

import java.util.ArrayList;

import enums.EstadoJob;
import escalonador.Escalonador;
import job.Job;
import so.SistemaOperacional;
import timer.Timer;

public class Main {

	public static void main(String[] args) {
		final int MEMORIA_PROGRAMA = 20;
		final int MEMORIA_DADOS = 5;
		
		Timer timer = new Timer();
		SistemaOperacional so = new SistemaOperacional(timer, MEMORIA_PROGRAMA, MEMORIA_DADOS);
		Escalonador escalonador = new Escalonador();
		
		String[] programa = new String[] {
			    "CARGI 10",
			    "ARMM 2",
			    "CARGI 32",
			    "SOMA 2",
			    "ARMM 0",
			    "GRAVA arquivo",
				"LE arquivo",
			    "PARA"
		};
		String[] programa2 = new String[] {
				"CARGI 15",
			    "ARMM 2",
			    "CARGI 77",
			    "SOMA 2",
			    "ARMM 0",
			    "PARA"
		};
		String[] programa3 = new String[] {
			    "CARGI 25",
			    "ARMM 2",
			    "CARGI 65",
			    "SOMA 2",
			    "ARMM 0",
			    "GRAVA arquivo",
				"LE arquivo",
			    "PARA"
		};
		
		ArrayList<Job> filaJob = new ArrayList<Job>();
		filaJob.add(new Job(programa, filaJob.size(), MEMORIA_DADOS));
		filaJob.add(new Job(programa2, filaJob.size(), MEMORIA_DADOS));
		filaJob.add(new Job(programa3, filaJob.size(), MEMORIA_DADOS));
		
		int dataLancamento = 0;
		boolean primeiraExecucao = true;
		
		while(escalonador.haProcesso(filaJob)) {
			for(int i=0; i<filaJob.size(); i++) {
				if(filaJob.get(i).getEstado() != EstadoJob.TERMINADO) {
					System.out.println("--- Execucao do processo " + (i+1) + " ---");
					if(primeiraExecucao)
						filaJob.get(i).setDataLancamento(dataLancamento);
					so.chamaExecucao(filaJob.get(i), timer, filaJob);
					dataLancamento = timer.tempoAtual();
				}	
			}
			primeiraExecucao = false;
		}
		System.out.println("Execucao de todos os processos encerrada.");
	}

}
