package controlador;

import java.util.ArrayList;

import cpu.Cpu;
import enums.EstadoJob;
import enums.InterrupcaoCPU;
import job.Job;
import so.SistemaOperacional;
import timer.InterrupcaoTimer;
import timer.Timer;

public class Controlador {
	
	public int controlaExecucao(Cpu cpu, SistemaOperacional so, Timer timer) {
		int contadorUsoCpu = 0;
		boolean houveInterrupcao = false;
		while(true) {
			System.out.println("Tempo do timer: " + timer.tempoAtual());
			
			cpu.executa();
			
			if(cpu.getCodigotInterrupcao() == InterrupcaoCPU.INSTRUCAO_ILEGAL || cpu.getCodigotInterrupcao() == InterrupcaoCPU.VIOLACAO_DE_MEMORIA) {
				so.trataInterrupcao(cpu.getCodigotInterrupcao(), cpu.instrucaoAtual());
				houveInterrupcao = true;
			}	
			
			for(int i = 0; i < timer.getFilaInterrupcoes().size(); i++) {
				int idCorrespondente = timer.getFilaInterrupcoes().get(i).getIdJob();
				String codigo = timer.verificaInterrupcao(i);
				boolean isPeriodica = timer.getFilaInterrupcoes().get(i).isPeriodica();
				int periodo = timer.getFilaInterrupcoes().get(i).getPeriodo();
				if(houveInterrupcao == false)
					houveInterrupcao = so.trataInterrupcaoTimer(codigo, isPeriodica, periodo, idCorrespondente);
				else
					so.trataInterrupcaoTimer(codigo, isPeriodica, periodo, idCorrespondente);
			}	
			timer.limpaFilaInterrupcoes();
			timer.contaPassagem();
			
			contadorUsoCpu+=1;
			
			if(houveInterrupcao)
				return contadorUsoCpu;
		}
	}
}
