package cpu;

import cpu.CpuEstado;

public class Cpu {
	int regContadorPrograma, regAcumulador, codigoInterrupcao;
	String[] memoriaPrograma;
	int[] memoriaDados;
	
	public Cpu(int tamInstrucoes, int tamDados, CpuEstado cpuEstado) {
		this.memoriaPrograma = new String[tamInstrucoes];
		this.memoriaDados = new int[tamDados];
	}
}
