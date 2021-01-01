package job;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import cpu.CpuEstado;
import enums.EstadoJob;

public class Job {
	String[] programa;
	int tamPrograma;
	
	int dataLancamento;
	float prioridade;
	
	EstadoJob estado;
	CpuEstado cpuSalva;
	
	ArrayList<JobLocalDado> listaLocalDados;
	int tempoES;
	
	public Job(String[] programa) {
		this.programa = programa;
		this.tamPrograma = programa.length;
		this.estado = EstadoJob.PRONTO;
		
		this.prioridade = 0.5f;

		this.tempoES = 2;
		this.listaLocalDados = new ArrayList<JobLocalDado>();
	}

	public String[] getPrograma() {
		return this.programa;
	}
	
	public void salvaEstado(CpuEstado cpuEstado) {
		this.cpuSalva = cpuEstado;
	}
	
	public CpuEstado getEstado() {
		return this.cpuSalva;
	}
	
	public void setDataLancamento(int dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
	public int getDataLancamento() {
		return this.dataLancamento;
	}
	
	public int getTempoES() {
		return this.tempoES;
	}
	
	public void addLocalDado(String nomeArquivo, int linhaDado) {
		listaLocalDados.add(new JobLocalDado(nomeArquivo, linhaDado));
	}
	
	public ArrayList<JobLocalDado> getListaLocalDados() {
		return this.listaLocalDados;
	}
}
