package main;

import so.SistemaOperacional;

public class Main {

	public static void main(String[] args) {
		SistemaOperacional so = new SistemaOperacional();
		
		String[] programa = new String[] {
			    "CARGI 10",
			    "ARMM 2",
			    "CARGI 32",
			    "SOMA 2",
			    "ARMM 0",
			    "GRAVA arquivo",
				"LE arquivo",
			    "PARA"
		};
		String[] programa2 = new String[] {
				"CARGI 15",
			    "ARMM 2",
			    "CARGI 77",
			    "SOMA 2",
			    "ARMM 0",
			    "GRAVA arquivo",
				"LE arquivo",
			    "PARA"
		};
		String[] programa3 = new String[] {
			    "CARGI 25",
			    "ARMM 2",
			    "CARGI 65",
			    "SOMA 2",
			    "ARMM 0",
			    "PARA"
		};
		
		so.adicionaJob(programa, 3);
		so.adicionaJob(programa2, 3);
		so.adicionaJob(programa3, 3);
		
		so.executa();
	}

}
