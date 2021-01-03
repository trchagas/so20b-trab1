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
import job.Job;
import timer.Timer;

public class SistemaOperacional {
	Cpu cpu;
	
	int[] dados;
	
	File dispositivosES;
	FileWriter escritaES;
	
	Controlador controlador;
	
	public SistemaOperacional(Timer timer, int memoriaPrograma, int memoriaDados) {
		this.dados = new int[memoriaDados];
		this.cpu = new Cpu(memoriaPrograma, memoriaDados);
		this.controlador = new Controlador();
		timer.pedeInterrupcao(0 ,true, 4, "Interrupcao periodica do SO", timer.tempoAtual());
	}
		
	public void chamaExecucao(Job job, Timer timer, ArrayList<Job> filaJob) {
		this.cpu.alteraPrograma(job.getPrograma());
		this.cpu.alteraEstado(job.getCpuEstadoSalva());
		if(this.cpu.getCodigotInterrupcao() == InterrupcaoCPU.DORMINDO)
			this.cpu.resetaCodigoInterrupcao();
		this.cpu.alteraDados(job.getDados());
		
		this.controlador.controlaExecucao(this.cpu, this, job, timer, filaJob);
		
		job.setDados(this.cpu.salvaDados());
		job.setCpuEstado(this.cpu.salvaEstado());
		
		if(job.getEstado() == EstadoJob.TERMINADO) {
			dados = job.getDados();
			for(int dado : dados)
				System.out.println(dado);
		}	
	}
	
	public void trataInterrupcao(InterrupcaoCPU codigoInterrupcao, String instrucao, Job job, Timer timer) {
		String[] comandoSeparado = instrucao.split(" ");
		String argumento = "default";
		
		String chamadaSistema = comandoSeparado[0];
		
		if(instrucao.split(" ").length > 1)
			argumento = comandoSeparado[1];
		
		if (codigoInterrupcao == InterrupcaoCPU.VIOLACAO_DE_MEMORIA) {
			System.out.println("Ocorreu uma Violacao de Memoria. Encerrando execucao.");
		} else if (codigoInterrupcao == InterrupcaoCPU.INSTRUCAO_ILEGAL) {
			switch(chamadaSistema) {
				case "PARA":
					System.out.println("Instrucao PARA executada. Encerrando execucao do processo com ID: " + job.getId());
					job.setEstado(EstadoJob.TERMINADO);
					break;
				case "LE":
					timer.pedeInterrupcao(job.getId(), false, job.getTempoES(), "Operacao E/S LE", timer.tempoAtual());
					System.out.println("Processo bloqueado devido a inicio de interrupcao do Timer: Operacao E/S LE");
					this.cpu.setAcumulador(leES(argumento, job));
					job.setCpuEstado(this.cpu.salvaEstado());
					job.setDados(this.cpu.salvaDados());
					this.cpu.cpuDormindo();
					job.setEstado(EstadoJob.BLOQUEADO);
					break;
				case "GRAVA":
					timer.pedeInterrupcao(job.getId(), false, job.getTempoES(), "Operacao E/S GRAVA", timer.tempoAtual());
					System.out.println("Processo bloqueado devido a inicio de interrupcao do Timer: Operacao E/S GRAVA");
					gravaES(this.cpu.getAcumulador(), argumento, job);
					job.setCpuEstado(this.cpu.salvaEstado());
					job.setDados(this.cpu.salvaDados());
					this.cpu.cpuDormindo();
					job.setEstado(EstadoJob.BLOQUEADO);
					break;
				default:
					System.out.println("Instrucao Ilegal. Encerrando execucao do processo com ID: " + job.getId());
					job.setEstado(EstadoJob.TERMINADO);
					
			}
		}
	}
	
	public void trataInterrupcaoTimer(String codigo, boolean periodica, ArrayList<Job> filaJob, int idCorrespondente) {
		if(codigo != "Nao ha interrupcao") {
			if(periodica) {
				System.out.println("Execucao de interrupcao Periodica do Timer: " + codigo);
			}
			else {
				for(Job job : filaJob) {
					if(job.getId() == idCorrespondente) {
						System.out.println("Fim de interrupcao do Timer: " + codigo + ", pertencente ao processo com ID: " + job.getId());
						job.setEstado(EstadoJob.PRONTO);
					}
				}
					
			}
		}
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
					
	   } catch (Exception e) {
	     e.printStackTrace();
	   }
	   return cpu.getAcumulador();
	}
	
	private void gravaES(int regAcumulador, String nomeArquivo, Job job) {
		try {
		  File cria = new File(nomeArquivo + ".txt");
	      FileWriter escreve = new FileWriter(nomeArquivo + ".txt");
	      escreve.append(String.valueOf(regAcumulador + "\n"));
	      
	      int linhaDado = 0;
	      Scanner scanner = new Scanner(cria);
	      while(scanner.hasNextLine())
	    	  linhaDado+=1;
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
