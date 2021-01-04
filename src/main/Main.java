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
		
		int tempoCpuOciosa = 0;
		int tempoCpuAtiva = 0;
		int tempoTotalCpu = 0;
		
		int numTrocasDeProcesso = 0;
		
		while(escalonador.haProcesso(filaJob)) {
			
			if(escalonador.processosBloqueados(filaJob)) {
				so.cpuDormindo();
				System.out.println("CPU Ociosa");
				System.out.println("Tempo do timer: " + timer.tempoAtual());
				
				for(int i = 0; i < timer.getFilaInterrupcoes().size(); i++) {
					int idCorrespondente = timer.getFilaInterrupcoes().get(i).getIdJob();
					String interrupcaoTimer = timer.verificaInterrupcao(i);
					boolean isPeriodica = timer.getFilaInterrupcoes().get(i).isPeriodica();
					so.trataInterrupcaoTimer(interrupcaoTimer, isPeriodica, filaJob, idCorrespondente);	
				}	
				timer.limpaFilaInterrupcoes();
				timer.contaPassagem();
				
				for(Job job : filaJob) {
					if(job.getEstado() == EstadoJob.BLOQUEADO)
						job.incrementaTempoBloqueado();
				}
				
				tempoCpuOciosa+=1;
			} else {
				
				Job proximoJob = escalonador.getNextJob(filaJob);
				numTrocasDeProcesso +=1 ;
				proximoJob.incrementaVezesEscalonado();
				
				System.out.println("--- Execucao do processo " + proximoJob.getId() + " ---");
				if(proximoJob.getDataLancamento() == -1)
					proximoJob.setDataLancamento(dataLancamento);
				int contadorUsoCpu = so.chamaExecucao(proximoJob, timer, filaJob);
				dataLancamento = timer.tempoAtual();
				
				tempoCpuAtiva += contadorUsoCpu;
			}
			
		}
		tempoTotalCpu = tempoCpuAtiva + tempoCpuOciosa;
		
		System.out.println("Execucao de todos os processos encerrada.");
		
		System.out.println("\n=== Relatório ===");
		System.out.println(tempoCpuAtiva);
		System.out.println(tempoCpuOciosa);
		System.out.println(tempoTotalCpu);
		
		for(int i = 0; i < filaJob.size(); i++) {
			Job job = filaJob.get(i);
			System.out.println("\n--- Processo com ID: " + job.getId() + " ---");
			System.out.println("Hora de inicio: " + job.getDataLancamento());
			System.out.println("Hora de termino: " + job.getHoraTermino());
			System.out.println("Tempo de retorno: " + (job.getHoraTermino()-job.getDataLancamento()));
			System.out.println("Tempo de CPU: " + (job.getTempoExecutando()));
			System.out.println("Percentual de uso da CPU: " + ((float)job.getTempoExecutando()/(float)tempoCpuAtiva) * 100f + "%");
			System.out.println("Tempo bloqueado: " + job.getTempoBloqueado());
			System.out.println("Vezes que foi bloqueado: " + job.getVezesBloqueado());
			System.out.println("Vezes que foi escalonado: " + job.getVezesEscalonado());
			//preempcao
		}
		/*
		 * cada processo: tempo inicio etc
		 * tempos totais
		 */
	}

}
