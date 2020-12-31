package job;

import java.io.File;
import java.io.FileWriter;

import cpu.CpuEstado;
import enums.EstadoJob;

public class Job {
	String[] programa;
	int tamPrograma;
//	File dispositivosES;
//	FileWriter escritaES;
	int dataLancamento;
	float prioridade;
	
	EstadoJob estado;
	CpuEstado cpuSalva;
	
	int id;
	
	public Job(String[] programa) {
		this.programa = programa;
		this.tamPrograma = programa.length;
		this.estado = EstadoJob.PRONTO;
		//this.criaES();
		this.dataLancamento = dataLancamento;
		this.prioridade = 0.5f;
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
}
