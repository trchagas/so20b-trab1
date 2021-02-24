package mmu;

public class DescritorPagina {
	boolean valido, alteravel, acessado, alterado;
	int quadroCorrespondente, id;
	public DescritorPagina(int id) {
		this.id = id;
		valido = false;
		alteravel = true;
		acessado = false;
		alterado = false;
		quadroCorrespondente = -1; //nenhum correspondente
	}
	public boolean isValido() {
		return valido;
	}
	public void setValido(boolean valido) {
		this.valido = valido;
	}
	public boolean isAlteravel() {
		return alteravel;
	}
	public void setAlteravel(boolean alteravel) {
		this.alteravel = alteravel;
	}
	public boolean isAcessado() {
		return acessado;
	}
	public void setAcessado(boolean acessado) {
		this.acessado = acessado;
	}
	public boolean isAlterado() {
		return alterado;
	}
	public void setAlterado(boolean alterado) {
		this.alterado = alterado;
	}
	public int getQuadroCorrespondente() {
		return quadroCorrespondente;
	}
	public void setQuadroCorrespondente(int quadroCorrespondente) {
		this.quadroCorrespondente = quadroCorrespondente;
	}
	public int getId() {
		return id;
	}
	
}
