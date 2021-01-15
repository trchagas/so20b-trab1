package cpu;

import cpu.CpuEstado;
import enums.InterrupcaoCPU;

public class Cpu {
	int regContadorPrograma, regAcumulador;
	InterrupcaoCPU codigoInterrupcao;
	String[] memoriaPrograma;
	int[] memoriaDados;
	
	public Cpu(int tamInstrucoes, int tamDados) {
		memoriaPrograma = new String[tamInstrucoes];
		memoriaDados = new int[tamDados];
		estadoInicializa();
	}
	
	public void alteraPrograma(String[] memoriaPrograma) {
		this.memoriaPrograma = new String[memoriaPrograma.length];
		
		for(int i=0; i<memoriaPrograma.length; i++) {
			this.memoriaPrograma[i] = memoriaPrograma[i];
		}
	}
	
	public void alteraDados(int[] memoriaDados) {
		for(int i=0; i<memoriaDados.length; i++) {
			this.memoriaDados[i] = memoriaDados[i];
		}
	}
	
	public int[] salvaDados() {
		return memoriaDados;
	}
	
	public InterrupcaoCPU getCodigotInterrupcao() {
		return codigoInterrupcao;
	}
	
	public void resetaCodigoInterrupcao(){
		if(codigoInterrupcao == InterrupcaoCPU.NORMAL) return;
		
		codigoInterrupcao = InterrupcaoCPU.NORMAL;
		regContadorPrograma += 1;
	}
	
	public String instrucaoAtual() {
		if(regContadorPrograma < memoriaPrograma.length)
			return memoriaPrograma[regContadorPrograma];
		
		return "Posicao de CPU invalida";
	}
	
	public void executa() {
		if(codigoInterrupcao != InterrupcaoCPU.NORMAL) return;
		
		String[] comandoSeparado; 
		String instrucao;
		int argumentoInt = 0;
		String argumentoString;
		
		comandoSeparado = memoriaPrograma[regContadorPrograma].split(" ");
		
		instrucao = comandoSeparado[0];
		
		if(comandoSeparado.length > 1) {
			try {
				argumentoInt = Integer.parseInt(comandoSeparado[1]);
			}
			catch(Exception e) {
				argumentoString = comandoSeparado[1];
			}
		}
		
		switch(comandoSeparado[0]) {
			case "CARGI" :
				regAcumulador = argumentoInt;
				regContadorPrograma += 1;
				break;
				
			case "CARGM" :
				try {
					regAcumulador = memoriaDados[argumentoInt];
					regContadorPrograma += 1;
				} catch (Exception e) {
					codigoInterrupcao = InterrupcaoCPU.VIOLACAO_DE_MEMORIA;
				}
				break;
				
			case "CARGX" :
				try {
					regAcumulador = memoriaDados[memoriaDados[argumentoInt]];
					regContadorPrograma += 1;
				} catch (Exception e) {
					codigoInterrupcao = InterrupcaoCPU.VIOLACAO_DE_MEMORIA;
				}
				break;
				
			case "ARMM" :
				try {
					memoriaDados[argumentoInt] = regAcumulador;
					regContadorPrograma += 1;
				} catch (Exception e) {
					codigoInterrupcao = InterrupcaoCPU.VIOLACAO_DE_MEMORIA;
				}
				break;
				
			case "ARMX" :
				try {
					memoriaDados[memoriaDados[argumentoInt]] = regAcumulador;
					regContadorPrograma += 1;
				} catch (Exception e) {
					codigoInterrupcao = InterrupcaoCPU.VIOLACAO_DE_MEMORIA;
				}
				break;
				
			case "SOMA" :
				try {
					regAcumulador += memoriaDados[argumentoInt];
					regContadorPrograma += 1;
				} catch (Exception e) {
					codigoInterrupcao = InterrupcaoCPU.VIOLACAO_DE_MEMORIA;
				}
				break;
				
			case "NEG" :
				regAcumulador *= -1;
				regContadorPrograma += 1;
				break;
				
			case "DESVZ" :
				if(regAcumulador == 0)
					regContadorPrograma = argumentoInt;
				break;
				
			default :
				codigoInterrupcao = InterrupcaoCPU.INSTRUCAO_ILEGAL;
		}
	}
	
	public CpuEstado salvaEstado() {
		return new CpuEstado(regContadorPrograma, regAcumulador, codigoInterrupcao);
	}
	
	public void estadoInicializa() {
		CpuEstado cpuEstado = new CpuEstado();
		regContadorPrograma = cpuEstado.regContadorPrograma;
		regAcumulador = cpuEstado.regAcumulador;
		codigoInterrupcao = cpuEstado.codigoInterrupcao;
	}
	
	public void alteraEstado(CpuEstado cpuEstado) {
		regContadorPrograma = cpuEstado.regContadorPrograma;
		regAcumulador = cpuEstado.regAcumulador;
		codigoInterrupcao = cpuEstado.codigoInterrupcao;
	}
	
	public void setAcumulador(int novoAcumulador) {
		regAcumulador = novoAcumulador;
	}
	
	public int getAcumulador() {
		return regAcumulador;
	}
	
	public void cpuDormindo() {
		codigoInterrupcao = InterrupcaoCPU.DORMINDO;
	}
	
}
