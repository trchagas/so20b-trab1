package controlador;

import cpu.Cpu;
import enums.Interrupcao;
import so.SistemaOperacional;

public class Controlador {
	public Controlador() {
		
	}
	
	public void controlaExecucao(Cpu cpu, SistemaOperacional so) {
		while(cpu.getCodigotInterrupcao() == Interrupcao.NORMAL) {
			cpu.executa();
			
			if(cpu.getCodigotInterrupcao() == Interrupcao.INSTRUCAO_ILEGAL || cpu.getCodigotInterrupcao() == Interrupcao.VIOLACAO_DE_MEMORIA) {
				so.trataInterrupcao(cpu.getCodigotInterrupcao());
			}
		}
	}
}
