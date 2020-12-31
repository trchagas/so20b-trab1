package cpu;

import enums.InterrupcaoCPU;

public class CpuEstado {
	int regContadorPrograma, regAcumulador;
	InterrupcaoCPU codigoInterrupcao;
	
	public CpuEstado() {
		this.regContadorPrograma = 0;
		this.regAcumulador = 0;
		this.codigoInterrupcao = InterrupcaoCPU.NORMAL;
	}
	
	public CpuEstado(int regContadorPrograma, int regAcumulador, InterrupcaoCPU codigoInterrupcao) {
		this.regContadorPrograma = regContadorPrograma;
		this.regAcumulador = regAcumulador;
		this.codigoInterrupcao = codigoInterrupcao;
	}
}
