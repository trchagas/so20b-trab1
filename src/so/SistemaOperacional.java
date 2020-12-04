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
import java.util.List;
import java.util.Scanner;

import controlador.Controlador;
import cpu.Cpu;
import cpu.CpuEstado;
import enums.Interrupcao;

public class SistemaOperacional {
	Cpu cpu;
	String[] programa;
	int[] dados;
	
	File dispositivosES;
	FileWriter escritaES;
	
	Controlador controlador;
	
	public SistemaOperacional() {
		programa = new String[] {
			    "CARGI 10",
			    "ARMM 2",
			    "CARGI 32",
			    "SOMA 2",
			    "ARMM 0",
			    "PARA"
		};
		this.dados = new int[4];
		this.cpu = new Cpu(programa.length, dados.length);
		
		this.controlador = new Controlador();
		this.cpu.alteraPrograma(programa);
		this.criaES();
	}
		
	public void chamaExecucao() {
		controlador.controlaExecucao(this.cpu, this);
		this.cpu.alteraDados(dados);
		System.out.println(cpu.instrucaoAtual());
		System.out.println(dados[0]);
	}
	
	public void trataInterrupcao(Interrupcao codigoInterrupcao) {
		if (codigoInterrupcao == Interrupcao.VIOLACAO_DE_MEMORIA) {
			System.out.println("Ocorreu uma Violacao de Memoria. Encerrando execucao");
		} else if (codigoInterrupcao == Interrupcao.INSTRUCAO_ILEGAL) {
			String chamadaSistema = this.cpu.instrucaoAtual();
			switch(chamadaSistema) {
				case "PARA":
					System.out.println("Instrucao PARA executada. Encerrando execucao.");
					this.cpu.resetaCodigoInterrupcao();
					break;
				case "LE":
					this.cpu.setAcumulador(leES());
					this.cpu.resetaCodigoInterrupcao();
					break;
				case "GRAVA":
					gravaES(this.cpu.getAcumulador());
					this.cpu.resetaCodigoInterrupcao();
					break;
				default:
					System.out.println("Instrucao Ilegal. Encerrando execucao.");
					
			}
		}
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
