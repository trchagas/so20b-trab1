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
		filaJob.add(new Job(programa, filaJob.size(), MEMORIA_DADOS, 5));
		filaJob.add(new Job(programa2, filaJob.size(), MEMORIA_DADOS, 5));
		filaJob.add(new Job(programa3, filaJob.size(), MEMORIA_DADOS, 5));
		
		int dataLancamento = 0;
		boolean primeiraExecucao = true;
		
		int tempoCpuOciosa = 0;
		
		while(escalonador.haProcesso(filaJob)) {
			if(escalonador.processosBloqueados(filaJob)) { //enquanto tiver dormindo
				System.out.println("CPU Ociosa");
				System.out.println("Tempo do timer: " + timer.tempoAtual());
				tempoCpuOciosa+=1;
				for(int i = 0; i < timer.getFilaInterrupcoes().size(); i++) {
					int idCorrespondente = timer.getFilaInterrupcoes().get(i).getIdJob();
					String interrupcaoTimer = timer.verificaInterrupcao(i);
					boolean isPeriodica = timer.getFilaInterrupcoes().get(i).isPeriodica();
					so.trataInterrupcaoTimer(interrupcaoTimer, isPeriodica, filaJob, idCorrespondente);	
				}	
				timer.limpaFilaInterrupcoes();
				timer.contaPassagem();
				
			} else {
				
				Job proximoJob = escalonador.getNextJob(filaJob);
				
				System.out.println("--- Execucao do processo " + proximoJob.getId() + " ---");
				if(primeiraExecucao)
					proximoJob.setDataLancamento(dataLancamento);
				so.chamaExecucao(proximoJob, timer, filaJob);
				dataLancamento = timer.tempoAtual();
				
			}
				
			primeiraExecucao = false;
		}
		System.out.println("Execucao de todos os processos encerrada.");
	}

}
