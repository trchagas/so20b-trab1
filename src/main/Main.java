package main;

import so.SistemaOperacional;

public class Main {

	public static void main(String[] args) {
		int quantum = 3;
		boolean prioridadeFixa = false;
		
		SistemaOperacional so = new SistemaOperacional(quantum, prioridadeFixa);
		
		String[] programa1 = new String[] {
				"CARGI 10",
				"ARMM 2",
				"CARGI 32",
				"SOMA 2",
				"ARMM 0",
				"PARA"
		};
		int[][] dadosES1 = {
				{1,2,3},
				{4,5,6},
				{7,8,9}
			};
		int custoES1 = 2;
		
		String[] programa2 = new String[] {
				"CARGI 6",
				"ARMM 0",
				"CARGI 7",
				"ARMM 1",
				"CARGI 0",
				"ARMM 2",
				"CARGM 2",
				"DESVZ 17",
				"CARGM 2",
				"SOMA 1",
				"ARMM 2",
				"CARGI 1",
				"NEG",
				"SOMA 0",
				"ARMM 0",
				"CARGI 0",
				"DESVZ 6",
				"PARA"
		};
		int[][] dadosES2 = {
				{10,20,30},
				{40,50,60},
				{70,80,90}
			};
		int custoES2 = 3;
		
		String[] programa3 = new String[] {
				"CARGI 1",
				"ARMM 1",
				"SOMA 1",
				"ARMM 0",
				"ARMX 1",
				"CARGI 50",
				"NEG",
				"ARMM 4",
				"PARA"
		};
		int[][] dadosES3 = {
				{5,10,15},
				{20,25,30},
				{35,40,45}
			};
		int custoES3 = 4;
		
		so.adicionaJob(programa1, dadosES1, custoES1);
		so.adicionaJob(programa2, dadosES2, custoES2);
		so.adicionaJob(programa3, dadosES3, custoES3);
		
		so.executa();
	}

}
