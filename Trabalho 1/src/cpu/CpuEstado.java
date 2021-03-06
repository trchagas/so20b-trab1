package cpu;

import enums.InterrupcaoCPU;

public class CpuEstado {
	int regContadorPrograma, regAcumulador;
	InterrupcaoCPU codigoInterrupcao;
	
	public CpuEstado() {
		regContadorPrograma = 0;
		regAcumulador = 0;
		codigoInterrupcao = InterrupcaoCPU.NORMAL;
	}
	
	public CpuEstado(int regContadorPrograma, int regAcumulador, InterrupcaoCPU codigoInterrupcao) {
		this.regContadorPrograma = regContadorPrograma;
		this.regAcumulador = regAcumulador;
		this.codigoInterrupcao = codigoInterrupcao;
	}
}
