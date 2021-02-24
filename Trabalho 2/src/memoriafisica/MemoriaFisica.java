package memoriafisica;

import java.util.Arrays;

public class MemoriaFisica {
	int[] memoria;
	int tamMemoria;
	int tamQuadro;
	
	public MemoriaFisica(int tamMemoria, int tamQuadro) {
		this.tamMemoria = tamMemoria; //número de quadros
		this.tamQuadro = tamQuadro;
		memoria = new int[tamMemoria*tamQuadro];
	}
	
	public int leDado(int endereco) {
		return memoria[endereco];
	}
	
	public void alteraDado(int novoDado ,int endereco) {
		memoria[endereco] = novoDado;
	}
	
	public int[] leQuadro(int indexQuadro) {
		return Arrays.copyOfRange(memoria, indexQuadro*tamQuadro, indexQuadro*tamQuadro+tamQuadro);
	}
	
	public void alteraQuadro(int[] novoQuadro, int indexQuadro) {
		for(int i = indexQuadro*tamQuadro, j=0; i<indexQuadro*tamQuadro+tamQuadro; i++,j++)
			memoria[i] = novoQuadro[j];
	}
	
	public int getTamQuadro() {
		return tamQuadro;
	}
	
	public int getTamMemoria() {
		return tamMemoria;
	}
}
