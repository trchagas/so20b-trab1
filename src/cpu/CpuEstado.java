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
<<<<<<< HEAD
	
	public void cpu_estado_altera_acumulador(int novoAcumulador) {
		this.regAcumulador = novoAcumulador;
	}
	
	public int cpuEstadoAcumulador() {
		return this.regAcumulador;
	}
=======
>>>>>>> 584e7c9e3257b33e26faf18fcf52e5b7f225fd6b
}
