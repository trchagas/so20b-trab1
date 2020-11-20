package main;

import cpu.Cpu;
import cpu.CpuEstado;
import estado.Estado;

public class Main {

	public static void main(String[] args) {
		String[] programa = new String[] {
		    "CARGI 10",
		    "ARMM 2",
		    "CARGI 32",
		    "SOMA 2",
		    "ARMM 0",
		    "PARA"
		  };
		
		int[] dados = new int[4];
		
		CpuEstado cpuEstado = new CpuEstado();
		
		Cpu cpu = new Cpu(programa.length, dados.length);
		
		cpu.alteraEstado(cpuEstado);
		
		cpu.alteraPrograma(programa);
		
		cpu.alteraDados(dados);
		
		while(cpu.getCodigotInterrupcao() == Estado.NORMAL.ordinal()) {
			cpu.executa();
		}
		
		System.out.println(cpu.instrucaoAtual());
		System.out.println(dados[0]);
	}

}
