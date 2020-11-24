package cpu;

import java.util.Arrays;

import cpu.CpuEstado;
import estado.Estado;

public class Cpu {
	int regContadorPrograma, regAcumulador, codigoInterrupcao;
	String[] memoriaPrograma;
	int[] memoriaDados;
	
	public Cpu(int tamInstrucoes, int tamDados) {
		this.memoriaPrograma = new String[tamInstrucoes];
		this.memoriaDados = new int[tamDados];
	}
	
	public void alteraEstado(CpuEstado cpuEstado) {
		this.regContadorPrograma = cpuEstado.regContadorPrograma;
		this.regAcumulador = cpuEstado.regAcumulador;
		this.codigoInterrupcao = cpuEstado.codigoInterrupcao;
	}
	
	public void alteraPrograma(String[] memoriaPrograma) {
		Arrays.fill(this.memoriaPrograma, null);
		
		for(int i=0; i<memoriaPrograma.length; i++) {
			this.memoriaPrograma[i] = memoriaPrograma[i];
		}
	}
	
	public void alteraDados(int[] memoriaDados) {
		for(int i=0; i<memoriaDados.length; i++) {
			memoriaDados[i] = this.memoriaDados[i];
		}
	}
	
//	public int[] salvaDados() {
//		return this.memoriaDados;
//	}
	
	public int getCodigotInterrupcao() {
		return this.codigoInterrupcao;
	}
	
	public void resetaCodigoInterrupcao(){
		if(this.codigoInterrupcao == Estado.NORMAL.ordinal()) return;
		
		this.codigoInterrupcao = Estado.NORMAL.ordinal();
		this.regContadorPrograma += 1;
	}
	
	public String instrucaoAtual() {
		if(this.regContadorPrograma < this.memoriaPrograma.length)
			return this.memoriaPrograma[this.regContadorPrograma];
		
		return "Posicao de CPU invalida";
	}
	
	public void executa() {
		String[] comandoSeparado; 
		int argumentoInt = 0; 
		String instrucao;
		
		for(int i=0; i<this.memoriaPrograma.length; i++) {
			comandoSeparado = this.memoriaPrograma[i].split(" ");
			
			instrucao = comandoSeparado[0];
			
			if(comandoSeparado.length > 1)
				argumentoInt = Integer.parseInt(comandoSeparado[1]);
			
			switch(comandoSeparado[0]) {
				case "CARGI" :
					this.regAcumulador = argumentoInt;
					this.regContadorPrograma += 1;
					break;
					
				case "CARGM" :
					this.regAcumulador = this.memoriaDados[argumentoInt];
					this.regContadorPrograma += 1;
					break;
					
				case "CARGX" :
					this.regAcumulador = this.memoriaDados[this.memoriaDados[argumentoInt]];
					this.regContadorPrograma += 1;
					break;
					
				case "ARMM" :
					this.memoriaDados[argumentoInt] = this.regAcumulador;
					this.regContadorPrograma += 1;
					break;
					
				case "ARMX" :
					this.memoriaDados[this.memoriaDados[argumentoInt]] = this.regAcumulador;
					this.regContadorPrograma += 1;
					break;
					
				case "SOMA" :
					this.regAcumulador += this.memoriaDados[argumentoInt];
					this.regContadorPrograma += 1;
					break;
					
				case "NEG" :
					this.regAcumulador *= 1;
					this.regContadorPrograma += 1;
					break;
					
				case "DESVZ" :
					if(this.regAcumulador == 0)
						this.regContadorPrograma = argumentoInt;
					this.regContadorPrograma += 1;
					break;
					
				default :
					this.codigoInterrupcao = Estado.INSTRUCAO_ILEGAL.ordinal();
			}
				
		}
	}
}
