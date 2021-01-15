package main;

import so.SistemaOperacional;

public class Main {

	public static void main(String[] args) {
		SistemaOperacional so = new SistemaOperacional();
		
		String[] programa1 = new String[] {
			    "CARGI 10",
			    "ARMM 2",
			    "CARGI 32",
			    "SOMA 2",
			    "ARMM 0",
			    "GRAVA 1",
				"LE 1",
			    "PARA"
		};
		int[][] dadosES1 = {
				{1,2,3},
				{4,5,6},
				{7,8,9}
			};
		int custoES1 = 2;
		int quantum1 = 3;
		
		String[] programa2 = new String[] {
				"CARGI 15",
			    "ARMM 2",
			    "CARGI 77",
			    "SOMA 2",
			    "ARMM 0",
			    "GRAVA 1",
				"LE 1",
			    "PARA"
		};
		int[][] dadosES2 = {
				{10,20,30},
				{40,50,60},
				{70,80,90}
			};
		int custoES2 = 3;
		int quantum2 = 4;
		
		String[] programa3 = new String[] {
			    "CARGI 25",
			    "ARMM 2",
			    "CARGI 65",
			    "SOMA 2",
			    "ARMM 0",
			    "PARA"
		};
		int[][] dadosES3 = {
				{5,10,15},
				{20,25,30},
				{35,40,45}
			};
		int custoES3 = 4;
		int quantum3 = 5;
		
		so.adicionaJob(programa1, dadosES1, custoES1, quantum1);
		so.adicionaJob(programa2, dadosES2, custoES2, quantum2);
		so.adicionaJob(programa3, dadosES3, custoES3, quantum3);
		
		so.executa();
	}

}
