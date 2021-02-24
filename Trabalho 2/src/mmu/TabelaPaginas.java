package mmu;

import java.util.ArrayList;

public class TabelaPaginas {
	ArrayList<DescritorPagina> descritores;
	int tamPagina;
	public TabelaPaginas(int numPaginas, int tamPagina) {
		descritores = new ArrayList<DescritorPagina>();
		for(int i=0; i<numPaginas; i++)
			descritores.add(new DescritorPagina(i));
		this.tamPagina = tamPagina;
		
	}
	
	public int converteLogicoFisico(int endereco) {
		int indexPagina = endereco/tamPagina;
		int posQuadro = endereco%tamPagina;
		int quadro = descritores.get(indexPagina).getQuadroCorrespondente();
		return (quadro*tamPagina)+posQuadro;
	}
	
	public int acessoImpossivel(int endereco, int tamMemoria) {
		int indexPagina = endereco/tamPagina;
		if(!descritores.get(indexPagina).isValido()) {
			System.out.println("Pagina invalida");
			return 1;
		} else if (!descritores.get(indexPagina).isAlteravel()) {
			System.out.println("Pagina nao alteravel");
			return 2;
		} else if (endereco >= tamMemoria*tamPagina) {
			System.out.println("Pagina nao mapeada");
			return 3;
		}
		return 0;
	}
	
	public ArrayList<DescritorPagina> getDescritores(){
		return descritores;
	}
	
	public void descritorAcessado(int endereco) {
		int indexPagina = endereco/tamPagina;
		descritores.get(indexPagina).setAcessado(true);
	}
	
	public void descritorAlterado(int endereco) {
		int indexPagina = endereco/tamPagina;
		descritores.get(indexPagina).setAlterado(true);
	}
	
}
