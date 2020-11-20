package cpu;

import java.util.Arrays;

import cpu.CpuEstado;
import cpu.CpuEstado.Estado;

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
		Arrays.fill(this.memoriaDados, 0);
		
		for(int i=0; i<memoriaPrograma.length; i++) {
			this.memoriaDados[i] = memoriaDados[i];
		}
	}
	
	public int[] salvaDados() { /////////////////////////////////////
		return this.memoriaDados;
	}
	
	public int geCodigotInterrupcao() {
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
		int argumentoInt; 
		String instrucao;
		
		for(int i=0; i<this.memoriaPrograma.length; i++) {
			comandoSeparado = this.memoriaPrograma[i].split(" ");
			
			instrucao = comandoSeparado[0];
			argumentoInt = Integer.parseInt(comandoSeparado[1]);
			
			switch(comandoSeparado[0]) {
				case "CARGI" :
					this.regAcumulador = argumentoInt;
					break;
					
				case "CARGM" :
					this.regAcumulador = this.memoriaDados[argumentoInt];
					break;
					
				case "CARGX" :
					this.regAcumulador = this.memoriaDados[this.memoriaDados[argumentoInt]];]
					break;
					
				case "ARMM" :
					this.memoriaDados[argumentoInt] = this.regAcumulador;
					break;
					
				case "ARMX" :
					this.memoriaDados[this.memoriaDados[argumentoInt]] = this.regAcumulador;
					break;
					
				case "SOMA" :
					this.regAcumulador += this.memoriaDados[argumentoInt];
					break;
					
				case "NEG" :
					this.regAcumulador *= 1;
					break;
					
				case "DESVZ" :
					if(this.regAcumulador == 0)
						this.regContadorPrograma = argumentoInt;
					break;
					
				default :
					this.codigoInterrupcao = Estado.INSTRUCAO_ILEGAL.ordinal();
			}
		}
	}
}
