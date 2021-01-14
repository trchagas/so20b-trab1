package so;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controlador.Controlador;
import cpu.Cpu;
import cpu.CpuEstado;
import enums.EstadoJob;
import enums.InterrupcaoCPU;
import escalonador.Escalonador;
import job.Job;
import timer.Timer;

public class SistemaOperacional {
	final int MEMORIA_PROGRAMA = 20;
	final int MEMORIA_DADOS = 5;
	
	Cpu cpu;
	
	int[] dados;
	
	File dispositivosES;
	FileWriter escritaES;
	
	Controlador controlador;
	Escalonador escalonador;
	
	ArrayList<Job> filaJob;
	Job jobAtual;
	
	Timer timer;
	
	public SistemaOperacional() {
		dados = new int[MEMORIA_DADOS];
		cpu = new Cpu(MEMORIA_PROGRAMA, MEMORIA_DADOS);
		controlador = new Controlador();
		escalonador = new Escalonador();
		
		filaJob = new ArrayList<Job>();
		
		timer = new Timer();
		
		timer.pedeInterrupcao(0 ,true, 4, "Interrupcao periodica do SO", timer.tempoAtual());
	}
	
	public void executa() {
		int dataLancamento = 0;
		
		int tempoCpuOciosa = 0;
		int tempoCpuAtiva = 0;
		int tempoTotalCpu = 0;
		
		int numTrocasDeProcesso = 0;
		
		while(escalonador.haProcesso(filaJob)) {
			
			if(!escalonador.processosBloqueados(filaJob)) {
				jobAtual = escalonador.getNextJob(filaJob);
				jobAtual.resetQuantum();
				numTrocasDeProcesso +=1 ;
				jobAtual.incrementaVezesEscalonado();
				
				System.out.println("--- Execucao do processo " + jobAtual.getId() + " ---");
				if(jobAtual.getDataLancamento() == -1)
					jobAtual.setDataLancamento(dataLancamento);
			}
			
			int contadorUsoCpu = chamaExecucao();
			dataLancamento = timer.tempoAtual();
			
			if(escalonador.processosBloqueados(filaJob)) {
				cpu.cpuDormindo();
			} else {
				cpu.resetaCodigoInterrupcao();
			}
			
			if(cpu.getCodigotInterrupcao() == InterrupcaoCPU.DORMINDO && escalonador.haProcesso(filaJob)) {
				System.out.println("CPU Ociosa");
				tempoCpuOciosa+=1;
			}
			
			tempoCpuAtiva += contadorUsoCpu;
			
			jobAtual.somaTempoExecutando(contadorUsoCpu);
			
			for(Job job : filaJob) {
				if(job.getEstado() == EstadoJob.BLOQUEADO && job != jobAtual)
					job.somaTempoBloqueado(contadorUsoCpu);
			}
//			if(proximoJob.getEstado() == EstadoJob.BLOQUEADO) {
//				proximoJob.incrementaVezesBloqueado();
//				proximoJob.recalculaPrioridade((float)contadorUsoCpu/(float)proximoJob.getQuantum());
//			}
//			if(contadorUsoCpu == proximoJob.getQuantum()) {
//				timer.pedeInterrupcao(proximoJob.getId(), false, 1, "Processo bloqueado por limite de quantum", timer.tempoAtual());
//				proximoJob.setEstado(EstadoJob.BLOQUEADO);
//				//jobAtual.incrementaVezesBloqueado();
//				System.out.println("Quantum do processo com ID = " + proximoJob.getId() + " atingido. Bloqueando execucao.");
//				//return contadorUsoCpu;
//			}
			
		}
		System.out.println("Execucao de todos os processos encerrada.");
		
		tempoTotalCpu = tempoCpuAtiva + tempoCpuOciosa;
		
		imprimeRelatorio(tempoCpuAtiva, tempoCpuOciosa);
	}
	
	public void imprimeRelatorio(int tempoCpuAtiva, int tempoCpuOciosa) {
		System.out.println("\n=== Relatório ===");
//		System.out.println(tempoCpuAtiva);
//		System.out.println(tempoCpuOciosa);
//		System.out.println(tempoTotalCpu);
		
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
	
	public void adicionaJob(String[] programa, int quantum) {
		filaJob.add(new Job(programa, filaJob.size(), MEMORIA_DADOS, 5));
	}
		
	public int chamaExecucao() {
		//System.out.println(jobAtual.getId());
		cpu.alteraPrograma(jobAtual.getPrograma());
		cpu.alteraEstado(jobAtual.getCpuEstadoSalva());
		cpu.alteraDados(jobAtual.getDados());
		
		int contadorUsoCpu = controlador.controlaExecucao(cpu, this, timer);
			
		jobAtual.setDados(cpu.salvaDados());
		jobAtual.setCpuEstado(cpu.salvaEstado());
		
//		for(Job jobteste : filaJob) {
//			System.out.println(jobteste.getEstado() + " id" + jobteste.getId());
//		}
		
		if(jobAtual.getEstado() == EstadoJob.TERMINADO) {
			System.out.println("Resultados do processo com ID = " + jobAtual.getId() + " foram:");
			jobAtual.setHoraTermino(timer.tempoAtual());
			dados = jobAtual.getDados();
			for(int dado : dados)
				System.out.println(dado);
		}
		
		return contadorUsoCpu;
	}
	
	public void trataInterrupcao(InterrupcaoCPU codigoInterrupcao, String instrucao) {
		//System.out.println(jobAtual.getId());
		String[] comandoSeparado = instrucao.split(" ");
		String argumento = "default";
		
		String chamadaSistema = comandoSeparado[0];
		
		if(instrucao.split(" ").length > 1)
			argumento = comandoSeparado[1];
		
		if (codigoInterrupcao == InterrupcaoCPU.VIOLACAO_DE_MEMORIA) {
			System.out.println("Ocorreu uma Violacao de Memoria. Encerrando execucao.");
			jobAtual.setEstado(EstadoJob.TERMINADO);
		} else if (codigoInterrupcao == InterrupcaoCPU.INSTRUCAO_ILEGAL) {
			switch(chamadaSistema) {
				case "PARA":
					System.out.println("Instrucao PARA executada. Encerrando execucao do processo com ID = " + jobAtual.getId());
					jobAtual.setEstado(EstadoJob.TERMINADO);
					break;
				case "LE":
					timer.pedeInterrupcao(jobAtual.getId(), false, jobAtual.getTempoES(), "Operacao E/S LE", timer.tempoAtual());
					System.out.println("Processo bloqueado devido a inicio de interrupcao do Timer: Operacao E/S LE");
					cpu.setAcumulador(leES(argumento, jobAtual));
					cpu.resetaCodigoInterrupcao();
					jobAtual.setEstado(EstadoJob.BLOQUEADO);
					jobAtual.incrementaVezesBloqueado();
					jobAtual.recalculaPrioridade((float)jobAtual.getQuantum()/(float)jobAtual.getQuantumInicial());
					break;
				case "GRAVA":
					timer.pedeInterrupcao(jobAtual.getId(), false, jobAtual.getTempoES(), "Operacao E/S GRAVA", timer.tempoAtual());
					System.out.println("Processo bloqueado devido a inicio de interrupcao do Timer: Operacao E/S GRAVA");
					gravaES(cpu.getAcumulador(), argumento, jobAtual);
					cpu.resetaCodigoInterrupcao();
					jobAtual.setEstado(EstadoJob.BLOQUEADO);
					jobAtual.incrementaVezesBloqueado();
					jobAtual.recalculaPrioridade((float)jobAtual.getQuantum()/(float)jobAtual.getQuantumInicial());
					break;
				default:
					System.out.println("Instrucao Ilegal. Encerrando execucao do processo com ID = " + jobAtual.getId());
					jobAtual.setEstado(EstadoJob.TERMINADO);
					
			}
		}
	}
	
	public boolean trataInterrupcaoTimer(String codigo, boolean periodica, int periodo, int idCorrespondente) {
		if(codigo != "Nao ha interrupcao") {
			if(periodica) {
				System.out.println("Execucao de interrupcao Periodica do Timer: " + codigo);
				jobAtual.diminuiQuantum(periodo);
				if(jobAtual.getQuantum() <= 0) {
					jobAtual.setEstado(EstadoJob.BLOQUEADO);
					jobAtual.incrementaVezesBloqueado();
					jobAtual.recalculaPrioridade(1);
					timer.pedeInterrupcao(jobAtual.getId(), false, 1, "Processo bloqueado por limite de quantum", timer.tempoAtual());
					System.out.println("Quantum do processo com ID = " + jobAtual.getId() + " atingido. Bloqueando execucao.");
					return true;
				}
					
			}
			else {
				for(Job job : filaJob) {
					if(job.getId() == idCorrespondente) {
						System.out.println("Fim de interrupcao do Timer: " + codigo + ", pertencente ao processo com ID = " + job.getId());
						job.setEstado(EstadoJob.PRONTO);
					}
				}	
			}
		}
		return false;
	}
	
	private int leES(String nomeArquivo, Job job) {
		try {
			File le = new File(nomeArquivo + ".txt");
			Scanner scanner = new Scanner(le);
			
			for(int i=0; i<job.getListaLocalDados().size(); i++) {
				if(Files.readAllLines(Paths.get(nomeArquivo + ".txt")).get(job.getListaLocalDados().get(i).getLinhaDado()) != null) {
					String novoAcumuladorString = Files.readAllLines(Paths.get(nomeArquivo + ".txt")).get(job.getListaLocalDados().get(i).getLinhaDado());
					int novoAcumuladorInt = Integer.parseInt(novoAcumuladorString);
					scanner.close();
					return novoAcumuladorInt;
				} else {
					System.out.println("Operacao de E/S LE: Nao ha nenhum valor nesse dispositivo.");
				}
			}
			scanner.close();
					
	   } catch (Exception e) {
	     e.printStackTrace();
	   }
	   return cpu.getAcumulador();
	}
	
	private void gravaES(int regAcumulador, String nomeArquivo, Job job) {
		try {
		  File cria = new File(nomeArquivo + ".txt");
	      FileWriter escreve = new FileWriter(nomeArquivo + ".txt", true);
	      escreve.write(String.valueOf(regAcumulador + "\n"));
	      
	      int linhaDado = 0;
	      
	      Scanner scanner = new Scanner(cria);
	      
	      while(scanner.hasNextLine()) {
	    	  String linha = scanner.nextLine();
	    	  linhaDado+=1;
	      }
	    	  
	      
	      escreve.close();
	      scanner.close();
	      
	      for(int i=0; i<job.getListaLocalDados().size(); i++) {
	    	  if(job.getListaLocalDados().get(i).getNomeArquivo() == nomeArquivo) {
	    		  job.getListaLocalDados().get(i).setLinhaDado(linhaDado);
	    		  return;
	    	  }
	      }
	      job.addLocalDado(nomeArquivo, linhaDado);
	      
	      
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
}
