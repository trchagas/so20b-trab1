package job;

import java.util.ArrayList;

import cpu.CpuEstado;
import enums.EstadoJob;

public class Job {
	int id;
	
	String[] programa;
	int tamPrograma;
	
	int[] dadosCPU;
	int tamDados;
	
	int tempoExecutando;
	
	int dataLancamento;
	int horaTermino;
	
	int vezesBloqueado;
	int tempoBloqueado;
	int vezesEscalonado;
	int vezesPreempcao;
	
	float prioridade;
	
	EstadoJob estado;
	CpuEstado cpuSalva;
	
	int[][] dadosES;
	int custoES;
	int contadorES;
	
	public Job(String[] programa, int id, int tamDados, int[][] dadosES, int custoES) {
		this.id = id+1;
		this.programa = programa;
		tamPrograma = programa.length;
		
		dadosCPU = new int[tamDados];
		
		estado = EstadoJob.PRONTO;
		
		tempoExecutando = 0;
		
		dataLancamento = -1;
		
		vezesBloqueado = 0;
		tempoBloqueado = 0;
		vezesEscalonado = 0;
		vezesPreempcao = 0;
		
		prioridade = 0.5f;

		this.dadosES = dadosES;
		this.custoES = custoES;
		this.contadorES = 0;
		
		cpuSalva = new CpuEstado();
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
	
	public int getCustoES() {
		return custoES;
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
	
	public void setDados(int[] dadosCPU) {
		for(int i=0; i<dadosCPU.length; i++) {
			this.dadosCPU[i] = dadosCPU[i];
		}
	}
	
	public int[] getDados() {
		return dadosCPU;
	}
	
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
	
	public void incrementaContadorES() {
		contadorES +=1 ;
	}
	
	public int leDadoES(int dispositivo) {
		return dadosES[dispositivo][contadorES];
	}
	
	public void gravaDadoES(int dispositivo, int acumulador) {
		dadosES[dispositivo][contadorES] = acumulador;
	}
	
	public void incrementaVezesPreempcao() {
		vezesPreempcao += 1;
	}
	
	public int getVezesPreempcao() {
		return vezesPreempcao;
	}
	
}
