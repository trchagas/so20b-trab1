package job;

import java.io.File;
import java.io.FileWriter;

import cpu.CpuEstado;

public class Job {
	String[] programa;
	int tamPrograma;
//	File dispositivosES;
//	FileWriter escritaES;
	int dataLancamento;
	float prioridade;
	
	CpuEstado estado;
	int id;
	
	public Job(String[] programa) {
		this.programa = programa;
		this.tamPrograma = programa.length;
		//this.criaES();
		this.dataLancamento = dataLancamento;
		//this.prioridade
	}

	public String[] getPrograma() {
		return this.programa;
	}
	
	public void salvaEstado(CpuEstado cpuEstado) {
		this.estado = cpuEstado;
	}
	
	public CpuEstado getEstado() {
		return this.estado;
	}
	
	public void setDataLancamento(int dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
}
