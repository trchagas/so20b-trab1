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
import enums.InterrupcaoCPU;
import job.Job;
import timer.Timer;

public class SistemaOperacional {
	public static final int MEMORIA_PROGRAMA = 20;
	public static final int MEMORIA_DADOS = 5;
	
	Cpu cpu;
	
	int[] dados;
	
	File dispositivosES;
	FileWriter escritaES;
	
	Controlador controlador;
	
	public SistemaOperacional(Timer timer) {
		this.dados = new int[MEMORIA_DADOS];
		this.cpu = new Cpu(MEMORIA_PROGRAMA, MEMORIA_DADOS);
		this.controlador = new Controlador();
		timer.pedeInterrupcao(true, 4, "Interrupcao periodica do SO", timer.tempoAtual());
		//System.out.println("Inicio de interrupcao Periodica do SO");
	}
		
	public void chamaExecucao(Job job, Timer timer) {
		this.cpu.alteraPrograma(job.getPrograma());
		
		this.controlador.controlaExecucao(this.cpu, this, job, timer);
		
		this.cpu.alteraDados(dados);
		//System.out.println(cpu.instrucaoAtual());
		for(int dado : dados)
			System.out.println(dado);
		
		job.salvaEstado(this.cpu.salvaEstado());
		this.cpu.estadoInicializa();
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
					System.out.println("Instrucao PARA executada. Encerrando execucao.");
					break;
				case "LE":
					timer.pedeInterrupcao(false, job.getTempoES(), "Operacao E/S LE", timer.tempoAtual());
					System.out.println("Inicio de interrupcao do Timer: Operacao E/S LE");
					this.cpu.setAcumulador(leES(argumento, job));
					job.salvaEstado(this.cpu.salvaEstado());
					this.cpu.cpuDormindo();
					break;
				case "GRAVA":
					timer.pedeInterrupcao(false, job.getTempoES(), "Operacao E/S GRAVA", timer.tempoAtual());
					System.out.println("Inicio de interrupcao do Timer: Operacao E/S GRAVA");
					gravaES(this.cpu.getAcumulador(), argumento, job);
					job.salvaEstado(this.cpu.salvaEstado());
					this.cpu.cpuDormindo();
					break;
				default:
					System.out.println("Instrucao Ilegal. Encerrando execucao.");
					
			}
		}
	}
	
	public void trataInterrupcaoTimer(String codigo, Job job, boolean periodica) {
		if(codigo != "Nao ha interrupcao") {
			if(periodica) {
				System.out.println("Execucao de interrupcao Periodica do Timer: " + codigo);
			}
			else {
				System.out.println("Fim de interrupcao do Timer: " + codigo);
				cpu.alteraEstado(job.getEstado());
				cpu.resetaCodigoInterrupcao();
			}
		}
	}
	
//	private int leES(String nomeArquivo) {
//		try {
//			File le = new File(nomeArquivo + ".txt");
//			Scanner myReader = new Scanner(le);
//			if(myReader.hasNextLine()) {
//				String novoAcumuladorString = myReader.nextLine();
//				int novoAcumuladorInt = Integer.parseInt(novoAcumuladorString);
//				myReader.close();
//				return novoAcumuladorInt;
//			} else {
//				System.out.println("Operacao de E/S LE: Nao ha nenhum valor nesse dispositivo.");
//			}
//	   } catch (Exception e) {
//	     e.printStackTrace();
//	   }
//		return 0;
//	}
//	
//	private void gravaES(int regAcumulador, String nomeArquivo) {
//		try {
//		  File cria = new File(nomeArquivo + ".txt");
//	      FileWriter escreve = new FileWriter(nomeArquivo + ".txt");
//	      escreve.write(String.valueOf(regAcumulador + "\n"));
//	      escreve.close();
//	    } catch (Exception e) {
//	      e.printStackTrace();
//	    }
//	}
	
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
