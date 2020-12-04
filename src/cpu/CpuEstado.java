package cpu;

import enums.Interrupcao;

public class CpuEstado {
	int regContadorPrograma, regAcumulador;
	Interrupcao codigoInterrupcao;
	
	public CpuEstado() {
		this.regContadorPrograma = 0;
		this.regAcumulador = 0;
		this.codigoInterrupcao = Interrupcao.NORMAL;
	}
	
	public CpuEstado(int regContadorPrograma, int regAcumulador, Interrupcao codigoInterrupcao) {
		this.regContadorPrograma = regContadorPrograma;
		this.regAcumulador = regAcumulador;
		this.codigoInterrupcao = codigoInterrupcao;
	}
}
