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
import enums.Interrupcao;
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
	
	public SistemaOperacional() {
		this.dados = new int[MEMORIA_DADOS];
		this.cpu = new Cpu(MEMORIA_PROGRAMA, MEMORIA_DADOS);
		this.controlador = new Controlador();
		this.criaES();
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
	
	public void trataInterrupcao(Interrupcao codigoInterrupcao, Job job, Timer timer) {
		if (codigoInterrupcao == Interrupcao.VIOLACAO_DE_MEMORIA) {
			System.out.println("Ocorreu uma Violacao de Memoria. Encerrando execucao.");
		} else if (codigoInterrupcao == Interrupcao.INSTRUCAO_ILEGAL) {
			String chamadaSistema = this.cpu.instrucaoAtual();
			switch(chamadaSistema) {
				case "PARA":
					System.out.println("Instrucao PARA executada. Encerrando execucao.");
					break;
				case "LE":
					job.salvaEstado(this.cpu.salvaEstado());
					this.cpu.cpuDormindo();
					timer.pedeInterrupcao(false, 2, "Operacao E/S LE", timer.tempoAtual());
					System.out.println("Inicio de interrupcao do Timer: Operacao E/S LE");
					this.cpu.setAcumulador(leES());
					break;
				case "GRAVA":
					job.salvaEstado(this.cpu.salvaEstado());
					this.cpu.cpuDormindo();
					timer.pedeInterrupcao(false, 2, "Operacao E/S GRAVA", timer.tempoAtual());
					System.out.println("Inicio de interrupcao do Timer: Operacao E/S GRAVA");
					gravaES(this.cpu.getAcumulador());
					break;
				default:
					System.out.println("Instrucao Ilegal. Encerrando execucao.");
					
			}
		}
	}
	
	public void trataInterrupcaoTimer(String codigo, Timer timer, Job job) {
		//this.cpu.alteraEstado(job.getEstado());
		if(codigo != " ")
			System.out.println("Fim de interrupcao do Timer: " + codigo);
		else {
			cpu.alteraEstado(job.getEstado());
			cpu.resetaCodigoInterrupcao();
		}
		
//		if(timer.getFilaInterrupcoes().size() == 0 && cpu.getCodigotInterrupcao() == Interrupcao.DORMINDO) {
//			cpu.alteraEstado(job.getEstado());
//			cpu.resetaCodigoInterrupcao();
//		}
	}
	
	private void criaES() {
		try {
		      File leitura = new File("0.txt");
		      File escrita = new File("1.txt");
		} catch (Exception e) {
		      e.printStackTrace();
	    }
	}
	
	private int leES() {
		Scanner input = new Scanner(System.in);
		System.out.println("Insira o valor do acumulador: ");
		int novoAcumulador = input.nextInt();
		
		try {
	      FileWriter escreve = new FileWriter("1.txt");
	      escreve.write(novoAcumulador + "\n");
	      escreve.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
		
		return novoAcumulador;
	}
	
	private void gravaES(int regAcumulador) {
		try {
	      FileWriter escreve = new FileWriter("0.txt");
	      escreve.write(regAcumulador + "\n");
	      escreve.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
}
