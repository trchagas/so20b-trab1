package so;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import controlador.Controlador;
import cpu.Cpu;
import enums.EstadoJob;
import enums.InterrupcaoCPU;
import escalonador.Escalonador;
import job.Job;
import timer.Timer;

public class SistemaOperacional {
	final int MEMORIA_PROGRAMA = 20;
	final int MEMORIA_DADOS = 5;
	
	Cpu cpu;
	
	int[] dados;
	
	File dispositivosES;
	FileWriter escritaES;
	
	Controlador controlador;
	Escalonador escalonador;
	
	ArrayList<Job> filaJob;
	Job jobAtual;
	
	Timer timer;
	
	int tempoCpuOciosa;
	int tempoCpuAtiva;
	int tempoTotalCpu;
	
	int numTrocasDeProcesso;
	
	int vezesSOexecutado;
	int vezesViolacaoMemoria;
	int vezesIntrucaoIlegal;
	int vezesPreempcao;
	
	int quantumInicial;
	int quantumRestante;
	boolean prioridadeFixa;
	
	public SistemaOperacional(int quantum, boolean prioridadeFixa) {
		dados = new int[MEMORIA_DADOS];
		cpu = new Cpu(MEMORIA_PROGRAMA, MEMORIA_DADOS);
		controlador = new Controlador();
		escalonador = new Escalonador();
		
		filaJob = new ArrayList<Job>();
		
		timer = new Timer();
		
		timer.pedeInterrupcao(0 ,true, 4, "Interrupcao periodica do SO", timer.tempoAtual());
		
		tempoCpuOciosa = 0;
		tempoCpuAtiva = 0;
		tempoTotalCpu = 0;
		
		numTrocasDeProcesso = 0;
		
		vezesSOexecutado = 0;
		vezesViolacaoMemoria = 0;
		vezesIntrucaoIlegal = 0;
		vezesPreempcao = 0;
		
		quantumInicial = quantum;
		quantumRestante = quantum;
		this.prioridadeFixa = prioridadeFixa;
	}
	
	public void executa() {
		int dataLancamento = 0;
		
		while(escalonador.haProcesso(filaJob)) {
			
			if(!escalonador.processosBloqueados(filaJob)) {
				jobAtual = escalonador.getNextJob(filaJob);
				quantumRestante = quantumInicial;
				numTrocasDeProcesso +=1 ;
				jobAtual.incrementaVezesEscalonado();
				
				System.out.println("--- Execucao do processo " + jobAtual.getId() + " ---");
				if(jobAtual.getDataLancamento() == -1)
					jobAtual.setDataLancamento(dataLancamento);
				cpu.resetaCodigoInterrupcao();
			} else {
				cpu.cpuDormindo();
				System.out.println("CPU Ociosa");
				tempoCpuOciosa+=1;
			}
			
			int contadorUsoCpu = chamaExecucao();
			dataLancamento = timer.tempoAtual();
			
			if(cpu.getCodigotInterrupcao() != InterrupcaoCPU.DORMINDO) {
				tempoCpuAtiva += contadorUsoCpu;
				jobAtual.somaTempoExecutando(contadorUsoCpu);
			}
			
		}
		System.out.println("Execucao de todos os processos encerrada.");
		System.out.println("Tempo final do timer: " + timer.tempoAtual());
		
		imprimeRelatorio();
	}
	
	public void imprimeRelatorio() {
		System.out.println("\n=== Relatório ===");
		
		for(int i = 0; i < filaJob.size(); i++) {
			Job job = filaJob.get(i);
			System.out.println("\n--- Processo com ID: " + job.getId() + " ---");
			System.out.println("Hora de inicio: " + job.getDataLancamento());
			System.out.println("Hora de termino: " + job.getHoraTermino());
			System.out.println("Tempo de retorno: " + (job.getHoraTermino()-job.getDataLancamento()));
			System.out.println("Tempo de CPU: " + (job.getTempoExecutando()));
			System.out.println("Percentual de uso da CPU: " + ((float)job.getTempoExecutando()/(float)tempoCpuAtiva) * 100f + "%");
			System.out.println("Tempo bloqueado: " + job.getTempoBloqueado());
			System.out.println("Vezes que foi bloqueado: " + job.getVezesBloqueado());
			System.out.println("Vezes que foi escalonado: " + job.getVezesEscalonado());
			System.out.println("Vezes que perdeu a CPU por preempcao: " + job.getVezesPreempcao());
		}
		
		System.out.println("\nTempos Totais:");
		System.out.println("Tempo CPU ativa: " + tempoCpuAtiva);
		System.out.println("Tempo ocioso da CPU: " + tempoCpuOciosa);
		System.out.println("Quantas vezes o SO executou: " + vezesSOexecutado);
		System.out.println("Vezes que houve interrupcao por Violacao de Memoria: " + vezesViolacaoMemoria);
		System.out.println("Vezes que houve interrupcao por Instrucao Ilegal: " + vezesIntrucaoIlegal);
		System.out.println("Quantidade de trocas de processo: " + numTrocasDeProcesso);
		System.out.println("Quantidade de trocas por preempcao: " + vezesPreempcao);
		
	}
	
	public void adicionaJob(String[] programa, int[][] dados, int custoES) {
		filaJob.add(new Job(programa, filaJob.size(), MEMORIA_DADOS, dados, custoES));
	}
		
	public int chamaExecucao() {
		if(cpu.getCodigotInterrupcao() != InterrupcaoCPU.DORMINDO) {
			cpu.alteraPrograma(jobAtual.getPrograma());
			cpu.alteraEstado(jobAtual.getCpuEstadoSalva());
			cpu.alteraDados(jobAtual.getDados());
		}
		
		int contadorUsoCpu = controlador.controlaExecucao(cpu, this, timer);
		
		if(cpu.getCodigotInterrupcao() != InterrupcaoCPU.DORMINDO) {
			jobAtual.setDados(cpu.salvaDados());
			jobAtual.setCpuEstado(cpu.salvaEstado());	
		
			if(jobAtual.getEstado() == EstadoJob.TERMINADO) {
				System.out.println("Resultados do processo com ID = " + jobAtual.getId() + " foram:");
				jobAtual.setHoraTermino(timer.tempoAtual());
				dados = jobAtual.getDados();
				for(int dado : dados)
					System.out.println(dado);
			}
		}
		
		return contadorUsoCpu;
	}
	
	public void trataInterrupcao(InterrupcaoCPU codigoInterrupcao, String instrucao) {
		vezesSOexecutado+=1;
		
		String[] comandoSeparado = instrucao.split(" ");
		int argumento = 0;
		
		String chamadaSistema = comandoSeparado[0];
		
		if(instrucao.split(" ").length > 1) {
			argumento = Integer.parseInt(comandoSeparado[1]);
		}
		
		if (codigoInterrupcao == InterrupcaoCPU.VIOLACAO_DE_MEMORIA) {
			System.out.println("Ocorreu uma Violacao de Memoria. Encerrando execucao.");
			jobAtual.setEstado(EstadoJob.TERMINADO);
			vezesViolacaoMemoria+=1;
		} else if (codigoInterrupcao == InterrupcaoCPU.INSTRUCAO_ILEGAL) {
			switch(chamadaSistema) {
				case "PARA":
					System.out.println("Instrucao PARA executada. Encerrando execucao do processo com ID = " + jobAtual.getId());
					jobAtual.setEstado(EstadoJob.TERMINADO);
					break;
				case "LE":
					timer.pedeInterrupcao(jobAtual.getId(), false, jobAtual.getCustoES(), "Operacao E/S LE", timer.tempoAtual());
					System.out.println("Processo bloqueado devido a inicio de interrupcao do Timer: Operacao E/S LE");
					
					cpu.setAcumulador(jobAtual.leDadoES(argumento));
					jobAtual.incrementaContadorES();
					
					cpu.resetaCodigoInterrupcao();
					jobAtual.setEstado(EstadoJob.BLOQUEADO);
					jobAtual.incrementaVezesBloqueado();
					
					if(!prioridadeFixa)
						jobAtual.recalculaPrioridade((float)(quantumInicial-quantumRestante)/(float)quantumInicial);
					
					break;
				case "GRAVA":
					timer.pedeInterrupcao(jobAtual.getId(), false, jobAtual.getCustoES(), "Operacao E/S GRAVA", timer.tempoAtual());
					System.out.println("Processo bloqueado devido a inicio de interrupcao do Timer: Operacao E/S GRAVA");
					
					jobAtual.gravaDadoES(argumento, cpu.getAcumulador());
					jobAtual.incrementaContadorES();
					
					cpu.resetaCodigoInterrupcao();
					jobAtual.setEstado(EstadoJob.BLOQUEADO);
					jobAtual.incrementaVezesBloqueado();
					
					if(!prioridadeFixa)
						jobAtual.recalculaPrioridade((float)(quantumInicial-quantumRestante)/(float)quantumInicial);
					
					break;
				default:
					System.out.println("Instrucao Ilegal. Encerrando execucao do processo com ID = " + jobAtual.getId());
					jobAtual.setEstado(EstadoJob.TERMINADO);
					
			}
			vezesIntrucaoIlegal+=1;
		}
	}
	
	public boolean trataInterrupcaoTimer(String codigo, boolean periodica, int periodo, int idCorrespondente) {
		if(codigo != "Nao ha interrupcao") {
			if(periodica) {
				System.out.println("Execucao de interrupcao Periodica do Timer: " + codigo);
				quantumRestante -= periodo;
				if(quantumRestante <= 0) {
					if(!prioridadeFixa)
						jobAtual.recalculaPrioridade(1f);
					System.out.println("Quantum do processo com ID = " + jobAtual.getId() + " atingido.");
					vezesPreempcao+=1;
					jobAtual.incrementaVezesPreempcao();
					return true;
				}
					
			}
			else {
				for(Job job : filaJob) {
					if(job.getId() == idCorrespondente) {
						System.out.println("Fim de interrupcao do Timer: " + codigo + ", pertencente ao processo com ID = " + job.getId());
						job.setEstado(EstadoJob.PRONTO);
					} 
				}	
			}
		}
		
		return false;
	}
	
	public void contaTempoBloqueado() {
		for(Job job : filaJob) {
			if(job.getEstado() == EstadoJob.BLOQUEADO)
				job.incrementaTempoBloqueado();
		}
	}
}
