package main;

import java.util.ArrayList;

import enums.EstadoJob;
import escalonador.Escalonador;
import job.Job;
import so.SistemaOperacional;
import timer.Timer;

public class Main {

	public static void main(String[] args) {
		SistemaOperacional so = new SistemaOperacional();
		
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
			    "GRAVA arquivo",
				"LE arquivo",
			    "PARA"
		};
		String[] programa3 = new String[] {
			    "CARGI 25",
			    "ARMM 2",
			    "CARGI 65",
			    "SOMA 2",
			    "ARMM 0",
			    "PARA"
		};
		
		so.adicionaJob(programa, 3);
		so.adicionaJob(programa2, 3);
		so.adicionaJob(programa3, 3);
		
		so.executa();
		
//		int dataLancamento = 0;
//		
//		int tempoCpuOciosa = 0;
//		int tempoCpuAtiva = 0;
//		int tempoTotalCpu = 0;
//		
//		int numTrocasDeProcesso = 0;
//		
//		while(escalonador.haProcesso(filaJob)) { //isso aqui vai pro so
//			
//			if(escalonador.processosBloqueados(filaJob)) {
//				so.cpuDormindo();
//				System.out.println("CPU Ociosa");
//				
////				System.out.println("Tempo do timer: " + timer.tempoAtual());
////				
////				for(int i = 0; i < timer.getFilaInterrupcoes().size(); i++) {
////					int idCorrespondente = timer.getFilaInterrupcoes().get(i).getIdJob();
////					String interrupcaoTimer = timer.verificaInterrupcao(i);
////					boolean isPeriodica = timer.getFilaInterrupcoes().get(i).isPeriodica();
////					so.trataInterrupcaoTimer(interrupcaoTimer, isPeriodica, filaJob, idCorrespondente);	
////				}	
////				timer.limpaFilaInterrupcoes();
////				timer.contaPassagem();
////				
////				for(Job job : filaJob) {
////					if(job.getEstado() == EstadoJob.BLOQUEADO)
////						job.incrementaTempoBloqueado();
////				}
//				
//				tempoCpuOciosa+=1;
//			} else {
//				
//				Job proximoJob = escalonador.getNextJob(filaJob);
//				numTrocasDeProcesso +=1 ;
//				proximoJob.incrementaVezesEscalonado();
//				
//				System.out.println("--- Execucao do processo " + proximoJob.getId() + " ---");
//				if(proximoJob.getDataLancamento() == -1)
//					proximoJob.setDataLancamento(dataLancamento);
//				int contadorUsoCpu = so.chamaExecucao(proximoJob, timer, filaJob);
//				dataLancamento = timer.tempoAtual();
//				
//				tempoCpuAtiva += contadorUsoCpu;
//			}
//			
//		}
//		tempoTotalCpu = tempoCpuAtiva + tempoCpuOciosa;
//		
//		System.out.println("Execucao de todos os processos encerrada.");
		
		
	}

}
