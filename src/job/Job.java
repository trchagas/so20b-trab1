package job;

import java.io.File;
import java.io.FileWriter;

import cpu.CpuEstado;
import enums.EstadoJob;

public class Job {
	String[] programa;
	int tamPrograma;
	
	int dataLancamento;
	float prioridade;
	
	EstadoJob estado;
	CpuEstado cpuSalva;
	
	int localDados;
	int tempoES;
	
	public Job(String[] programa, int idArquivo) {
		this.programa = programa;
		this.tamPrograma = programa.length;
		this.estado = EstadoJob.PRONTO;
		
		this.prioridade = 0.5f;

		this.tempoES = 2;
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
	
	public void setLocalDados(int localDados) {
		this.localDados = localDados;
	}
}
