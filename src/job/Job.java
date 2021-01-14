package job;

import java.util.ArrayList;

import cpu.CpuEstado;
import enums.EstadoJob;

public class Job {
	int id;
	
	String[] programa;
	int tamPrograma;
	
	int[] dados;
	int tamDados;
	
	int quantum;
	int quantumInicial;
	
	int tempoExecutando;
	
	int dataLancamento;
	int horaTermino;
	
	int vezesBloqueado;
	int tempoBloqueado;
	int vezesEscalonado;
	
	float prioridade;
	
	EstadoJob estado;
	CpuEstado cpuSalva;
	
	ArrayList<JobLocalDado> listaLocalDados;
	int tempoES;
	
	public Job(String[] programa, int id, int tamDados, int quantum) {
		this.id = id+1;
		this.programa = programa;
		tamPrograma = programa.length;
		
		dados = new int[tamDados];
		
		estado = EstadoJob.PRONTO;
		
		tempoExecutando = 0;
		
		dataLancamento = -1;
		
		vezesBloqueado = 0;
		tempoBloqueado = 0;
		vezesEscalonado = 0;
		
		prioridade = 0.5f;

		tempoES = 2;
		listaLocalDados = new ArrayList<JobLocalDado>();
		
		cpuSalva = new CpuEstado();
		
		this.quantum = quantum;
		quantumInicial = quantum;
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
	
	public int getTempoES() {
		return tempoES;
	}
	
	public void addLocalDado(String nomeArquivo, int linhaDado) {
		listaLocalDados.add(new JobLocalDado(nomeArquivo, linhaDado));
	}
	
	public ArrayList<JobLocalDado> getListaLocalDados() {
		return listaLocalDados;
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
	
	public void setDados(int[] dados) {
		for(int i=0; i<dados.length; i++) {
			this.dados[i] = dados[i];
		}
	}
	
	public int[] getDados() {
		return dados;
	}
	
	public int getQuantum() {
		return quantum;
	}
	
	public void resetQuantum() {
		this.quantum = quantumInicial;
	}
	
	public void diminuiQuantum(int periodo) {
		quantum -= periodo;
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
	
	public void somaTempoBloqueado(int tempo) {
		tempoBloqueado += tempo;
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
	
	public int getQuantumInicial() {
		return quantumInicial;
	}
	
}
