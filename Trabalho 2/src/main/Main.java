package main;

import so.SistemaOperacional;

public class Main {

	public static void main(String[] args) {
		//Configurações do Sistema Operacional
		int quantum = 5;
		boolean prioridadeFixa = false;
		
		int tamMemoriaFisica = 60; //100%
		int tamPagina = 2;
		
		//Se o FIFO vai utilizar Segunda Chance
		boolean segundaChance = false;
		
		SistemaOperacional so = new SistemaOperacional(quantum, prioridadeFixa, tamMemoriaFisica, tamPagina, segundaChance);
		
		//Conjunto de programas com mistura entre limitados por CPU e limitados por E/S
		
		String[] programa1 = new String[] {
				"LE 0",
				"ARMM 0",
				"ARMM 2",
				"LE 0",
				"ARMM 1",
				"ARMM 3",
				"CARGI 10",
				"ARMM 4",
				"LE 0",
				"ARMX 4",
				"CARGI 1",
				"SOMA 4",
				"ARMM 4",
				"CARGI -1",
				"SOMA 3",
				"ARMM 3",
				"DESVZ 19",
				"CARGI 0",
				"DESVZ 8",
				"CARGM 2",
				"ARMM 3",
				"CARGI -1",
				"SOMA 1",
				"ARMM 1",
				"DESVZ 27",
				"CARGI 0",
				"DESVZ 8",
				"CARGI 10",
				"ARMM 4",
				"CARGM 0",
				"ARMM 1",
				"CARGM 2",
				"ARMM 3",
				"CARGI 0",
				"ARMM 5",
				"CARGX 4",
				"SOMA 5",
				"ARMM 5",
				"CARGI 1",
				"SOMA 4",
				"ARMM 4",
				"CARGI -1",
				"SOMA 3",
				"ARMM 3",
				"DESVZ 47",
				"CARGI 0",
				"DESVZ 35",
				"CARGM 5",
				"GRAVA 1",
				"CARGI -1",
				"SOMA 1",
				"ARMM 1",
				"DESVZ 55",
				"CARGI 0",
				"DESVZ 31",
				"PARA"
		};
		int[][] dadosES1 = {
				{1,2,3,4,5,6,7,8,9,10},
				{11,12,13,14,15,16,17,18,19,20},
				{21,22,23,24,25,26,27,28,29,30}
			};
		int[] custoES1 = {2,2,2};
		
		String[] programa2 = new String[] {
				"LE 0",
				"ARMM 0",
				"ARMM 2",
				"LE 0",
				"ARMM 1",
				"ARMM 3",
				"CARGI 10",
				"ARMM 4",
				"LE 0",
				"ARMX 4",
				"CARGI 1",
				"SOMA 4",
				"ARMM 4",
				"CARGI -1",
				"SOMA 3",
				"ARMM 3",
				"DESVZ 19",
				"CARGI 0",
				"DESVZ 8",
				"CARGM 2",
				"ARMM 3",
				"CARGI -1",
				"SOMA 1",
				"ARMM 1",
				"DESVZ 27",
				"CARGI 0",
				"DESVZ 8",
				"CARGI 10",
				"ARMM 6",
				"CARGM 2",
				"ARMM 3",
				"CARGM 0",
				"ARMM 1",
				"CARGI 0",
				"ARMM 5",
				"CARGM 6",
				"ARMM 4",
				"CARGX 4",
				"SOMA 5",
				"ARMM 5",
				"CARGM 2",
				"SOMA 4",
				"ARMM 4",
				"CARGI -1",
				"SOMA 1",
				"ARMM 1",
				"DESVZ 49",
				"CARGI 0",
				"DESVZ 37",
				"CARGM 5",
				"GRAVA 1",
				"CARGI -1",
				"SOMA 3",
				"ARMM 3",
				"DESVZ 60",
				"CARGI 1",
				"SOMA 6",
				"ARMM 6",
				"CARGI 0",
				"DESVZ 31",
				"PARA"
		};
		int[][] dadosES2 = {
				{5,10,15,20,25,30,35,40,45,50},
				{55,60,65,70,75,80,85,90,95,100},
				{105,110,115,120,125,130,135,140,145,150}
			};
		int[] custoES2 = {1,2,3};
		
		so.adicionaJob(programa1, dadosES1, custoES1);
		so.adicionaJob(programa2, dadosES2, custoES2);
		
		so.executa();
	}

}
