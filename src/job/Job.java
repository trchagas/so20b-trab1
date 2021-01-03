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
	
	int dataLancamento;
	float prioridade;
	
	EstadoJob estado;
	CpuEstado cpuSalva;
	
	ArrayList<JobLocalDado> listaLocalDados;
	int tempoES;
	
	public Job(String[] programa, int id, int tamDados) {
		this.id = id+1;
		this.programa = programa;
		this.tamPrograma = programa.length;
		
		this.dados = new int[tamDados];
		
		this.estado = EstadoJob.PRONTO;
		
		this.prioridade = 0.5f;

		this.tempoES = 2;
		this.listaLocalDados = new ArrayList<JobLocalDado>();
		
		this.cpuSalva = new CpuEstado();
	}

	public String[] getPrograma() {
		return this.programa;
	}
	
	public void setCpuEstado(CpuEstado cpuEstado) {
		this.cpuSalva = cpuEstado;
	}
	
	public CpuEstado getCpuEstadoSalva() {
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
	
	public EstadoJob getEstado() {
		return this.estado;
	}
	
	public void setEstado(EstadoJob estado) {
		this.estado = estado;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setDados(int[] dados) {
		for(int i=0; i<dados.length; i++) {
			this.dados[i] = dados[i];
		}
	}
	
	public int[] getDados() {
		return this.dados;
	}
}
