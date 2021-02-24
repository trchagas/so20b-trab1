package so;

import java.util.ArrayList;
import java.util.Arrays;

import controlador.Controlador;
import cpu.Cpu;
import enums.EstadoJob;
import enums.InterrupcaoCPU;
import escalonador.Escalonador;
import job.Job;
import memoriafisica.MemoriaFisica;
import mmu.DescritorPagina;
import mmu.MemoryManagementUnit;
import timer.Timer;

public class SistemaOperacional {
	final int MEMORIA_PROGRAMA = 80;
	final int MEMORIA_DADOS = 30; // cada processo tem 30 paginas
	
	Cpu cpu;
	
	int[] dadosCPU;
	int[][] dadosES;
	
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
	int vezesInterrupcaoPeriodica;
	
	int vezesPageFault;
	
	int quantumInicial;
	int quantumRestante;
	boolean prioridadeFixa;
	
	MemoryManagementUnit mmu;
	MemoriaFisica memoriaFisica;
	
	int[][] memoriaSecundaria; //tamanho da memoria virtual
	
	int[][] mapaMemoriaPrincipal; //tamanho de linhas deve ser a quantidade de quadros
	
	int tamMemoriaFisica;
	int tamPagina;
	
	boolean segundaChance;
	int contadorFIFO;
	
	public SistemaOperacional(int quantum, boolean prioridadeFixa, int tamMemoriaFisica, int tamPagina, boolean segundaChance) {
		this.tamMemoriaFisica = tamMemoriaFisica;
		this.tamPagina = tamPagina;
		memoriaFisica = new MemoriaFisica(tamMemoriaFisica, tamPagina);
		mmu = new MemoryManagementUnit(memoriaFisica);
		
		cpu = new Cpu(MEMORIA_PROGRAMA, mmu);
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
		vezesInterrupcaoPeriodica = 0;
		
		vezesPageFault = 0;
		
		quantumInicial = quantum;
		quantumRestante = quantum;
		this.prioridadeFixa = prioridadeFixa;
		
		this.segundaChance = segundaChance;
		contadorFIFO = 0;
	}
	
	private void inicializaMapa() {
		mapaMemoriaPrincipal = new int[memoriaFisica.getTamMemoria()][2];
		
		int j = 0;
		int k = 0;
		
		for(int i = 0; i< mapaMemoriaPrincipal.length; i++) {
			if(i!=0 && i%MEMORIA_DADOS==0) {
				j++;
				k=0;
			}
			if(j>=filaJob.size())
				break;
			
			filaJob.get(j).getTabelaPaginas().getDescritores().get(k).setQuadroCorrespondente(i);
			filaJob.get(j).getTabelaPaginas().getDescritores().get(k).setValido(true);
			mapaMemoriaPrincipal[i][0] = filaJob.get(j).getId();
			mapaMemoriaPrincipal[i][1] = filaJob.get(j).getTabelaPaginas().getDescritores().get(k).getId();
			k++;
		}
			
	}
	
	public void executa() {
		memoriaSecundaria = new int[filaJob.size()][MEMORIA_DADOS*tamPagina];
		inicializaMapa();
		
		int dataLancamento = 0;
		
		while(escalonador.haProcesso(filaJob)) {
			
			if(!escalonador.processosBloqueados(filaJob)) {
				jobAtual = escalonador.getNextJob(filaJob);
				
				quantumRestante = quantumInicial;
				
				jobAtual.incrementaVezesEscalonado();
				numTrocasDeProcesso +=1 ;
				
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
		System.out.println("\nExecucao de todos os processos encerrada.");
		System.out.println("Tempo final do timer: " + timer.tempoAtual());
		
		imprimeRelatorio();
	}
	//################################# Relatório utilizado no Trabalho 1 #################################
//	public void imprimeRelatorio() { 
//		System.out.println("\n=== Relatório ===");
//		
//		for(int i = 0; i < filaJob.size(); i++) {
//			Job job = filaJob.get(i);
//			System.out.println("\n--- Processo com ID: " + job.getId() + " ---");
//			System.out.println("Hora de inicio: " + job.getDataLancamento());
//			System.out.println("Hora de termino: " + job.getHoraTermino());
//			System.out.println("Tempo de retorno: " + (job.getHoraTermino()-job.getDataLancamento()));
//			System.out.println("Tempo de CPU: " + (job.getTempoExecutando()));
//			System.out.println("Percentual de uso da CPU: " + ((float)job.getTempoExecutando()/(float)tempoCpuAtiva) * 100f + "%");
//			System.out.println("Tempo bloqueado: " + job.getTempoBloqueado());
//			System.out.println("Vezes que foi bloqueado: " + job.getVezesBloqueado());
//			System.out.println("Vezes que foi escalonado: " + job.getVezesEscalonado());
//			System.out.println("Vezes que perdeu a CPU por preempcao: " + job.getVezesPreempcao());
//		}
//		
//		System.out.println("\n--- Tempos Totais ---");
//		System.out.println("Tempo CPU ativa: " + tempoCpuAtiva);
//		System.out.println("Tempo ocioso da CPU: " + tempoCpuOciosa);
//		System.out.println("Quantas vezes o SO executou: " + vezesSOexecutado);
//		System.out.println("Vezes que houve interrupcao por Violacao de Memoria: " + vezesViolacaoMemoria);
//		System.out.println("Vezes que houve interrupcao por Instrucao Ilegal: " + vezesIntrucaoIlegal);
//		System.out.println("Vezes que houve interrupcao periodica do Sistema Operacional: " + vezesInterrupcaoPeriodica);
//		System.out.println("Quantidade de trocas de processo: " + numTrocasDeProcesso);
//		System.out.println("Quantidade de trocas por preempcao: " + vezesPreempcao);
//		
//	}
	//################################# Relatório utilizado no Trabalho 2 #################################
	public void imprimeRelatorio() {
		System.out.println("\n=== Relatório ===");
		
		//Configuração
		System.out.println("\n--- Configurações do sistema ---");
		System.out.println("Quantum: " + quantumInicial);
		System.out.println("Prioridade Fixa: " + prioridadeFixa);
		System.out.println("Número de quadros da memória física: " + tamMemoriaFisica);
		System.out.println("Tamanho de cada página ou quadro: " + tamPagina);
		System.out.println("Utilização de FIFO com 2a chance: " + segundaChance);
		
		//Tempos Totais
		System.out.println("\n--- Tempos totais ---");
		System.out.println("Tempo gasto para executar todos os processos: " + (tempoCpuAtiva+tempoCpuOciosa));
		System.out.println("Número total de falhas de página: " + vezesPageFault);
		
		//Cada Processo
		for(int i = 0; i < filaJob.size(); i++) {
			Job job = filaJob.get(i);
			System.out.println("\n--- Processo com ID: " + job.getId() + " ---");
			System.out.println("Número de páginas: " + job.getNumPaginas());
			System.out.println("Número de endereços de memória virtual: " + job.getNumPaginas()*tamPagina);
			System.out.println("Tempo de retorno: " + (job.getHoraTermino()-job.getDataLancamento()));
			System.out.println("Tempo de CPU: " + job.getTempoExecutando());
			System.out.println("Número de faltas de página: " + job.getVezesPageFault());
			System.out.println("Número de vezes que foi escalonado: " + job.getVezesEscalonado());
			System.out.println("Tempo bloqueado em E/S: " + job.getTempoBloqueado());
			System.out.println("Tempo bloqueado esperando troca de página: " + job.getTempoPageFault());
			System.out.println("Tempo de espera em fila de escalonamento: " + (job.getTempoBloqueado() + job.getDataLancamento()));
		}
		
	}
	
	public void adicionaJob(String[] programa, int[][] dados, int[] custoES) {
		filaJob.add(new Job(programa, filaJob.size(), MEMORIA_DADOS, dados, custoES, tamPagina));
	}
		
	public int chamaExecucao() {
		if(cpu.getCodigotInterrupcao() != InterrupcaoCPU.DORMINDO) {
			cpu.alteraPrograma(jobAtual.getPrograma());
			cpu.alteraEstado(jobAtual.getCpuEstadoSalva());
			//cpu.alteraDados(jobAtual.getDadosCPU()); //função descontinuada
			mmu.trocaTabelaPaginas(jobAtual.getTabelaPaginas());
		}
		
		int contadorUsoCpu = controlador.controlaExecucao(cpu, this, timer);
		
		if(cpu.getCodigotInterrupcao() != InterrupcaoCPU.DORMINDO) {
			//jobAtual.setDadosCPU(cpu.salvaDados()); //função descontinuada
			jobAtual.setCpuEstado(cpu.salvaEstado());	
		
			if(jobAtual.getEstado() == EstadoJob.TERMINADO) {
				System.out.println("Resultados do processo com ID = " + jobAtual.getId() + " foram:");
				jobAtual.setHoraTermino(timer.tempoAtual());
				
				//coloca memória física na secundária pra imprimir em ordem
				for(int i = 0; i<jobAtual.getTabelaPaginas().getDescritores().size(); i++) {
					if(jobAtual.getTabelaPaginas().getDescritores().get(i).isValido()) {
						
						int[] quadro = memoriaFisica.leQuadro(jobAtual.getTabelaPaginas().getDescritores().get(i).getQuadroCorrespondente());
						
						for(int j = 0, k=i*tamPagina; j<tamPagina; j++,k++) {
							memoriaSecundaria[jobAtual.getId()][k] = quadro[j];
						}
					}
				}
				for(int dado : memoriaSecundaria[jobAtual.getId()])
					System.out.println(dado);
					
				
//				dadosCPU = jobAtual.getDadosCPU(); //função descontinuada
//				System.out.println("Dados da CPU: ");
//				for(int dado : dadosCPU)
//					System.out.println(dado);
				
				dadosES = jobAtual.getDadosES();
				System.out.println("Dados de E/S: ");
				for(int[] linhaDado : dadosES) {
					for(int dado : linhaDado)
						System.out.print(dado + " ");
					System.out.println();
				}
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
			
			timer.pedeInterrupcao(jobAtual.getId(), false, 2, "Acesso a MMU negado", timer.tempoAtual());
			System.out.println("Processo bloqueado devido a acesso negado na MMU");
			
			cpu.resetaCodigoInterrupcao();
			jobAtual.setEstado(EstadoJob.BLOQUEADO);
			jobAtual.incrementaVezesBloqueado();
			
			if(!prioridadeFixa)
				jobAtual.recalculaPrioridade((float)(quantumInicial-quantumRestante)/(float)quantumInicial);
			vezesViolacaoMemoria+=1;
			
		} else if (codigoInterrupcao == InterrupcaoCPU.INSTRUCAO_ILEGAL) {
			switch(chamadaSistema) {
				case "PARA":
					System.out.println("Instrucao PARA executada. Encerrando execucao do processo com ID = " + jobAtual.getId());
					jobAtual.setEstado(EstadoJob.TERMINADO);
					break;
				case "LE":
					if(argumento >= 0 && argumento < jobAtual.getNumDispositivosES() && jobAtual.haMemoriaDispositivoES(argumento)) {
						timer.pedeInterrupcao(jobAtual.getId(), false, jobAtual.getCustoES(argumento), "Operacao E/S LE", timer.tempoAtual());
						System.out.println("Processo bloqueado devido a inicio de interrupcao do Timer: Operacao E/S LE");
						
						cpu.setAcumulador(jobAtual.leDadoES(argumento));
						jobAtual.incrementaContadorES(argumento);
						
						cpu.resetaCodigoInterrupcao();
						jobAtual.setEstado(EstadoJob.BLOQUEADO);
						jobAtual.incrementaVezesBloqueado();
						
						if(!prioridadeFixa)
							jobAtual.recalculaPrioridade((float)(quantumInicial-quantumRestante)/(float)quantumInicial);
					} else {
						System.out.println("Ocorreu uma Violacao de Memoria na Operacao E/S LE. Encerrando execucao do processo com ID = " + jobAtual.getId());
						jobAtual.setEstado(EstadoJob.TERMINADO);
						vezesViolacaoMemoria+=1;
						return;
					}
					
					break;
				case "GRAVA":
					if(argumento >= 0 && argumento < jobAtual.getNumDispositivosES() && jobAtual.haMemoriaDispositivoES(argumento)) {
						timer.pedeInterrupcao(jobAtual.getId(), false, jobAtual.getCustoES(argumento), "Operacao E/S GRAVA", timer.tempoAtual());
						System.out.println("Processo bloqueado devido a inicio de interrupcao do Timer: Operacao E/S GRAVA");
						
						jobAtual.gravaDadoES(argumento, cpu.getAcumulador());
						jobAtual.incrementaContadorES(argumento);
						
						cpu.resetaCodigoInterrupcao();
						jobAtual.setEstado(EstadoJob.BLOQUEADO);
						jobAtual.incrementaVezesBloqueado();
						
						if(!prioridadeFixa)
							jobAtual.recalculaPrioridade((float)(quantumInicial-quantumRestante)/(float)quantumInicial);
					} else {
						System.out.println("Ocorreu uma Violacao de Memoria na Operacao E/S GRAVA. Encerrando execucao do processo com ID = " + jobAtual.getId());
						jobAtual.setEstado(EstadoJob.TERMINADO);
						vezesViolacaoMemoria+=1;
						return;
					}
					
					break;
				default:
					System.out.println("Instrucao Ilegal. Encerrando execucao do processo com ID = " + jobAtual.getId());
					jobAtual.setEstado(EstadoJob.TERMINADO);
			}
			vezesIntrucaoIlegal+=1;
		} else if(codigoInterrupcao == InterrupcaoCPU.PAGE_FAULT) {
			
			substituiPagina();
			
			cpu.resetaCodigoInterrupcao();
			jobAtual.setEstado(EstadoJob.BLOQUEADO);
			jobAtual.incrementaVezesBloqueado();
			
			if(!prioridadeFixa)
				jobAtual.recalculaPrioridade((float)(quantumInicial-quantumRestante)/(float)quantumInicial);
			vezesPageFault++;
			jobAtual.incrementaVezesPageFault();
			jobAtual.somaTempoPageFault(4);
		}
	}
	
	private void substituiPagina() {
		int enderecoVirtual = Integer.parseInt(cpu.instrucaoAtual().split(" ")[1]);
		int indexPagina = enderecoVirtual/tamPagina;
		DescritorPagina novaReferencia = jobAtual.getTabelaPaginas().getDescritores().get(indexPagina);
		
		int idJobAnterior = mapaMemoriaPrincipal[contadorFIFO][0];
		int idDescritorAnterior = mapaMemoriaPrincipal[contadorFIFO][1];
		
		//processo do algoritmo de fifo com segunda chance caso verdadeiro
		while(segundaChance && filaJob.get(idJobAnterior).getTabelaPaginas().getDescritores().get(idDescritorAnterior).isAcessado()) {
			filaJob.get(idJobAnterior).getTabelaPaginas().getDescritores().get(idDescritorAnterior).setAcessado(false);
			contadorFIFO++;
			if(contadorFIFO == tamMemoriaFisica)
				contadorFIFO = 0;
			idJobAnterior = mapaMemoriaPrincipal[contadorFIFO][0];
			idDescritorAnterior = mapaMemoriaPrincipal[contadorFIFO][1];
		}
		
		//referencia o descritor em relação ao mapa
		novaReferencia.setQuadroCorrespondente(contadorFIFO);
		novaReferencia.setValido(true);
		
		//guarda o que estava ocupando
		int[] quadroAnterior = memoriaFisica.leQuadro(contadorFIFO);
		
		for(int i=0, j=idDescritorAnterior*tamPagina; i<tamPagina; i++, j++) {
			memoriaSecundaria[idJobAnterior][j] = quadroAnterior[i];
		}
		
		timer.pedeInterrupcao(jobAtual.getId(), false, 4, "Interrupcao por esvaziamento de quadro", timer.tempoAtual());
		System.out.println("Inicio de interrupcao por esvaziamento do quadro");
		
		//desreferencia o que estava ocupando
		filaJob.get(idJobAnterior).getTabelaPaginas().getDescritores().get(idDescritorAnterior).setQuadroCorrespondente(-1);
		filaJob.get(idJobAnterior).getTabelaPaginas().getDescritores().get(idDescritorAnterior).setValido(false);
		
		//coloca o que estava na secundaria no quadro correspondente
		int[] quadroNovo = Arrays.copyOfRange(memoriaSecundaria[jobAtual.getId()],
												jobAtual.getTabelaPaginas().getDescritores().get(indexPagina).getId()*tamPagina,
												jobAtual.getTabelaPaginas().getDescritores().get(indexPagina).getId()*tamPagina+tamPagina);
		memoriaFisica.alteraQuadro(quadroNovo, contadorFIFO);
		
		timer.pedeInterrupcao(jobAtual.getId(), false, 4, "Interrupcao por preenchimento de quadro", timer.tempoAtual());
		System.out.println("Inicio de interrupcao por preenchimento do quadro");
		
		//referencia o mapa a nova referencia
		mapaMemoriaPrincipal[contadorFIFO][0] = jobAtual.getId();
		mapaMemoriaPrincipal[contadorFIFO][1] = novaReferencia.getId();
		
		//incrementa o contador auxiliar do fifo
		contadorFIFO++;
		if(contadorFIFO == tamMemoriaFisica)
			contadorFIFO = 0;
	}
	
	public boolean trataInterrupcaoTimer(String codigo, boolean periodica, int periodo, int idCorrespondente) {
		if(codigo != "Nao ha interrupcao") {
			if(periodica) {
				vezesSOexecutado+=1;
				vezesInterrupcaoPeriodica+=1;
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
