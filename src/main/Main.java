package main;

import java.util.ArrayList;

import job.Job;
import so.SistemaOperacional;

public class Main {

	public static void main(String[] args) {
		SistemaOperacional so = new SistemaOperacional();
		
		String[] programa = new String[] {
				"LE",
			    "CARGI 10",
			    "ARMM 2",
			    "CARGI 32",
			    "SOMA 2",
			    "ARMM 0",
			    "PARA"
		};
		String[] programa2 = new String[] {
				"CARGI 6",
				"ARMM  0",
				"CARGI 7",
				"ARMM  1",
				"CARGI 0",
				"ARMM  2",
				"CARGM 0",
				"DESVZ 17",
				"CARGM 2",
				"SOMA  1",
				"ARMM  2",
				"CARGI 1",
				"NEG",
				"SOMA  0",
				"ARMM  0",
				"CARGI 0",
				"DESVZ 6",
				"CARGM 2",
				"PARA"
		};
		
		ArrayList<Job> filaJob;
		filaJob = new ArrayList<Job>();
		
		filaJob.add(new Job(programa));
		filaJob.add(new Job(programa2));
		
		for(int i=0; i<filaJob.size(); i++) {
			if(i==0)
				filaJob.get(i).setDataLancamento(0);
			System.out.println("Execucao do programa " + (i+1) + ":");
			so.chamaExecucao(filaJob.get(i));
		}
	}

}
