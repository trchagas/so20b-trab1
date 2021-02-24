package mmu;

import memoriafisica.MemoriaFisica;

public class MemoryManagementUnit {
	TabelaPaginas tabelaPaginas;
	MemoriaFisica memoriaFisica;
	
	public MemoryManagementUnit(MemoriaFisica memoriaFisica){
		this.memoriaFisica = memoriaFisica;
	}
	
	public void trocaTabelaPaginas(TabelaPaginas tabelaPaginas) {
		this.tabelaPaginas = tabelaPaginas;
	}
	
	public int leDado(int endereco) {
		tabelaPaginas.descritorAcessado(endereco);
		int fisico = tabelaPaginas.converteLogicoFisico(endereco);
		return memoriaFisica.leDado(fisico);
			
	}
	
	public void alteraDado(int novoDado ,int endereco) {
		tabelaPaginas.descritorAlterado(endereco);
		tabelaPaginas.descritorAcessado(endereco);
		int fisico = tabelaPaginas.converteLogicoFisico(endereco);
		memoriaFisica.alteraDado(novoDado, fisico);
	}
	
	public int verificaErro(int endereco) {
		return tabelaPaginas.acessoImpossivel(endereco, memoriaFisica.getTamMemoria());
	}
}
