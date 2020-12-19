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
		
	public void chamaExecucao(Job job) {
		this.cpu.alteraPrograma(job.getPrograma());
		
		this.controlador.controlaExecucao(this.cpu, this, job);
		this.cpu.alteraDados(dados);
		//System.out.println(cpu.instrucaoAtual());
		for(int dado : dados)
			System.out.println(dado);
		
		job.salvaEstado(this.cpu.salvaEstado());
		this.cpu.estadoInicializa();
	}
	
	public void trataInterrupcao(Interrupcao codigoInterrupcao, Job job, Timer timer) {
		if (codigoInterrupcao == Interrupcao.VIOLACAO_DE_MEMORIA) {
			System.out.println("Ocorreu uma Violacao de Memoria. Encerrando execucao");
		} else if (codigoInterrupcao == Interrupcao.INSTRUCAO_ILEGAL) {
			String chamadaSistema = this.cpu.instrucaoAtual();
			switch(chamadaSistema) {
				case "PARA":
					System.out.println("Instrucao PARA executada. Encerrando execucao.");
					break;
				case "LE":
					job.salvaEstado(this.cpu.salvaEstado());
					this.cpu.cpuDormindo();
					timer.pedeInterrupcao(false, 2, "Operacao E/S LE");
					this.cpu.setAcumulador(leES());
					break;
				case "GRAVA":
					job.salvaEstado(this.cpu.salvaEstado());
					this.cpu.cpuDormindo();
					timer.pedeInterrupcao(false, 2, "Operacao E/S GRAVA");
					gravaES(this.cpu.getAcumulador());
					break;
				default:
					System.out.println("Instrucao Ilegal. Encerrando execucao.");
					
			}
		}
	}
	
	public void trataInterrupcaoTimer(String codigo, Job job) {
		this.cpu.alteraEstado(job.getEstado());
		System.out.println("Interrupcao do Timer: " + codigo);
	}
	
	private void criaES() {
		try {
			escritaES = new FileWriter("dispositivosES.txt");
			escritaES.write("0 0\n1 0");
			escritaES.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int leES() {
		Scanner input = new Scanner(System.in);
		int novoAcumulador = input.nextInt();
		
		try {
			URI uri = this.getClass().getResource("dispositivosES.txt").toURI();
	        List<String> linhas = Files.readAllLines(Paths.get(uri), Charset.defaultCharset());
	        String[] linhasStr = linhas.toArray(new String[linhas.size()]);
	        linhasStr[0] = "0" + String.valueOf(novoAcumulador);
			
	        PrintWriter printWriter = new PrintWriter(new FileWriter("dispositivosES.txt"));
	        for(String linha : linhasStr) {
	        	printWriter.println(linha);
	        }
	        printWriter.close();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return novoAcumulador;
	}
	
	private void gravaES(int regAcumulador) {
		try {
			URI uri = this.getClass().getResource("dispositivosES.txt").toURI();
	        List<String> linhas = Files.readAllLines(Paths.get(uri), Charset.defaultCharset());
	        String[] linhasStr = linhas.toArray(new String[linhas.size()]);
	        linhasStr[1] = "1" + String.valueOf(regAcumulador);
			
	        PrintWriter printWriter = new PrintWriter(new FileWriter("dispositivosES.txt"));
	        for(String linha : linhasStr) {
	        	printWriter.println(linha);
	        }
	        printWriter.close();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
