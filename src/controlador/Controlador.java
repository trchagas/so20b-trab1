package controlador;

import cpu.Cpu;
import enums.Interrupcao;
import so.SistemaOperacional;
import timer.Timer;

public class Controlador {
	Timer timer;
	
	public Controlador() {
		timer = new Timer();
		//timer.passagemInicio(1);
		//timer.passagemFim(5);
	}
	
	public void controlaExecucao(Cpu cpu, SistemaOperacional so) {
		while(cpu.getCodigotInterrupcao() == Interrupcao.NORMAL) {
			
			timer.contaPassagem();
			System.out.println("Tempo do timer: " + timer.tempoAtual());
			
			cpu.executa();
			
			if(cpu.getCodigotInterrupcao() == Interrupcao.INSTRUCAO_ILEGAL || cpu.getCodigotInterrupcao() == Interrupcao.VIOLACAO_DE_MEMORIA) {
				timer.pedeInterrupcao(false, 0, cpu.getCodigotInterrupcao());
				
			}
			while(timer.getFilaInterrupcoes().size() != 0) {
				so.trataInterrupcao(timer.verificaInterrupcao());
			}
			
		}
	}
}
