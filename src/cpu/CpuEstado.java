package cpu;

import cpu.Cpu;
import estado.Estado;

public class CpuEstado {
	int regContadorPrograma, regAcumulador, codigoInterrupcao;
	
	public CpuEstado() {
		this.regContadorPrograma = 0;
		this.regAcumulador = 0;
		this.codigoInterrupcao = Estado.NORMAL.ordinal(); 
	}
	
	public void salvaEstado(Cpu cpu) {
		this.regContadorPrograma = cpu.regContadorPrograma;
		this.regAcumulador = cpu.regAcumulador;
		this.codigoInterrupcao = cpu.codigoInterrupcao; 
	}
}
