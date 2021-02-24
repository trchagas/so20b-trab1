package job;

import cpu.CpuEstado;
import enums.EstadoJob;
import mmu.TabelaPaginas;

public class Job {
	int id;
	
	String[] programa;
	int tamPrograma;
	
//	int[] dadosCPU;
//	int tamDadosCPU;
	
	int tempoExecutando;
	
	int dataLancamento;
	int horaTermino;
	
	int vezesBloqueado;
	int tempoBloqueado;
	int vezesEscalonado;
	int vezesPreempcao;
	
	int vezesPageFault;
	int tempoPageFault;
	
	float prioridade;
	
	EstadoJob estado;
	CpuEstado cpuSalva;
	
	int[][] dadosES;
	int[] contadorES;
	int[] custoES;
	
	TabelaPaginas tabelaPaginas;
	int tamPagina;
	int numPaginas;
	
	public Job(String[] programa, int id, int numPaginas, int[][] dadosES, int custoES[], int tamPagina) {
		this.id = id;
		this.programa = programa;
		tamPrograma = programa.length;
		
		//dadosCPU = new int[tamDadosCPU];
		
		estado = EstadoJob.PRONTO;
		
		tempoExecutando = 0;
		
		dataLancamento = -1;
		
		vezesBloqueado = 0;
		tempoBloqueado = 0;
		vezesEscalonado = 0;
		vezesPreempcao = 0;
		
		vezesPageFault = 0;
		tempoPageFault = 0;
		
		prioridade = 0.5f;

		this.dadosES = dadosES;
		this.custoES = custoES;
		this.contadorES = new int[dadosES.length];
		
		cpuSalva = new CpuEstado();
		
		this.tamPagina = tamPagina; 
		this.numPaginas = numPaginas;
		tabelaPaginas = new TabelaPaginas(numPaginas, tamPagina);
	}

	public String[] getPrograma() {
		return programa;
	}
	
	public void setCpuEstado(CpuEstado cpuEstado) {
		cpuSalva = cpuEstado;
	}
	
	public CpuEstado getCpuEstadoSalva() {
		return cpuSalva;
	}
	
	public void setDataLancamento(int dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
	public int getDataLancamento() {
		return dataLancamento;
	}
	
	public int getCustoES(int dispositivo) {
		return custoES[dispositivo];
	}
	
	public EstadoJob getEstado() {
		return estado;
	}
	
	public void setEstado(EstadoJob estado) {
		this.estado = estado;
	}
	
	public int getId() {
		return id;
	}
	
//	public void setDadosCPU(int[] dadosCPU) {
//		for(int i=0; i<dadosCPU.length; i++) {
//			this.dadosCPU[i] = dadosCPU[i];
//		}
//	}
	
//	public int[] getDadosCPU() {
//		return dadosCPU;
//	}
	
	public void recalculaPrioridade(float fracaoQuantum) {
		prioridade = (prioridade+fracaoQuantum)/2f;
	}
	
	public float getPrioridade() {
		return prioridade;
	}
	
	public void setHoraTermino(int horaTermino) {
		this.horaTermino = horaTermino;
	}
	
	public int getHoraTermino() {
		return horaTermino;
	}

	public int getVezesBloqueado() {
		return vezesBloqueado;
	}

	public void incrementaVezesBloqueado() {
		vezesBloqueado += 1;
	}
	
	public int getTempoBloqueado() {
		return tempoBloqueado;
	}
	
	public void incrementaTempoBloqueado() {
		tempoBloqueado += 1;
	}
	
	public int getTempoExecutando() {
		return tempoExecutando;
	}
	
	public void somaTempoExecutando(int tempo) {
		tempoExecutando += tempo;
	}
	
	public int getVezesEscalonado() {
		return vezesEscalonado;
	}
	
	public void incrementaVezesEscalonado() {
		vezesEscalonado += 1;
	}
	
	public void incrementaContadorES(int dispositivo) {
		contadorES[dispositivo] +=1 ;
	}
	
	public int leDadoES(int dispositivo) {
		return dadosES[dispositivo][contadorES[dispositivo]];
	}
	
	public void gravaDadoES(int dispositivo, int acumulador) {
		dadosES[dispositivo][contadorES[dispositivo]] = acumulador;
	}
	
	public int getNumDispositivosES() {
		return dadosES.length;
	}
	
	public boolean haMemoriaDispositivoES(int dispositivo) {
		return contadorES[dispositivo] < dadosES[dispositivo].length;
	}
	
	public int[][] getDadosES(){
		return dadosES;
	}
	
	public void incrementaVezesPreempcao() {
		vezesPreempcao += 1;
	}
	
	public int getVezesPreempcao() {
		return vezesPreempcao;
	}

	public TabelaPaginas getTabelaPaginas() {
		return tabelaPaginas;
	}
	
	public void incrementaVezesPageFault() {
		vezesPageFault +=1 ;
	}
	
	public int getVezesPageFault() {
		return vezesPageFault;
	}
	
	public void somaTempoPageFault(int tempo) {
		tempoPageFault += tempo;
	}
	
	public int getTempoPageFault() {
		return tempoPageFault;
	}
	
	public int getNumPaginas() {
		return numPaginas;
	}
	
}
