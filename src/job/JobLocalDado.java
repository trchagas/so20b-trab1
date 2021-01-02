package job;

public class JobLocalDado {
	String nomeArquivo;
	int linhaDado;
	
	public JobLocalDado(String nomeArquivo, int linhaDado) {
		this.nomeArquivo = nomeArquivo;
		this.linhaDado = linhaDado;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public int getLinhaDado() {
		return linhaDado;
	}

	public void setLinhaDado(int linhaDado) {
		this.linhaDado = linhaDado;
	}
}
