package cpu;

import cpu.CpuEstado;
import enums.InterrupcaoCPU;
import mmu.MemoryManagementUnit;

public class Cpu {
	int regContadorPrograma, regAcumulador;
	InterrupcaoCPU codigoInterrupcao;
	String[] memoriaPrograma;
	//int[] memoriaDados;
	
	MemoryManagementUnit mmu;
	
	public Cpu(int tamInstrucoes, MemoryManagementUnit mmu) {
		memoriaPrograma = new String[tamInstrucoes];
		//memoriaDados = new int[tamDados];
		estadoInicializa();
		this.mmu = mmu;
	}
	
	public void alteraPrograma(String[] memoriaPrograma) {
		this.memoriaPrograma = new String[memoriaPrograma.length];
		
		for(int i=0; i<memoriaPrograma.length; i++) {
			this.memoriaPrograma[i] = memoriaPrograma[i];
		}
	}
	
//	public void alteraDados(int[] memoriaDados) {
//		for(int i=0; i<memoriaDados.length; i++) {
//			mmu.alteraDado(memoriaDados[i], i);
//		}
//	}
	
//	public int[] salvaDados() {
//		return memoriaDados;
//	}
	
	public InterrupcaoCPU getCodigotInterrupcao() {
		return codigoInterrupcao;
	}
	
	public void resetaCodigoInterrupcao(){
		if(codigoInterrupcao == InterrupcaoCPU.NORMAL) return;
		
		if(codigoInterrupcao != InterrupcaoCPU.PAGE_FAULT) regContadorPrograma += 1;
		codigoInterrupcao = InterrupcaoCPU.NORMAL;
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
				switch(mmu.verificaErro(argumentoInt)) {
					case 0:
						regAcumulador = mmu.leDado(argumentoInt);
						regContadorPrograma += 1;
						break;
					case 1:
						codigoInterrupcao = InterrupcaoCPU.PAGE_FAULT;
						break;
					default:
						codigoInterrupcao = InterrupcaoCPU.VIOLACAO_DE_MEMORIA;
				}
				
				break;
				
			case "CARGX" :
				try {
					switch(mmu.verificaErro(mmu.leDado(argumentoInt))) {
						case 0:
							regAcumulador = mmu.leDado(mmu.leDado(argumentoInt));
							regContadorPrograma += 1;
							break;
						case 1:
							codigoInterrupcao = InterrupcaoCPU.PAGE_FAULT;
							break;
						default:
							codigoInterrupcao = InterrupcaoCPU.VIOLACAO_DE_MEMORIA;
					}
				} catch(Exception e) {
					codigoInterrupcao = InterrupcaoCPU.VIOLACAO_DE_MEMORIA;
				}
				
				
				break;
				
			case "ARMM" :
				switch(mmu.verificaErro(argumentoInt)) {
					case 0:
						mmu.alteraDado(regAcumulador, argumentoInt);
						regContadorPrograma += 1;
						break;
					case 1:
						codigoInterrupcao = InterrupcaoCPU.PAGE_FAULT;
						break;
					default:
						codigoInterrupcao = InterrupcaoCPU.VIOLACAO_DE_MEMORIA;
				}
				
				break;
				
			case "ARMX" :
				try {
					switch(mmu.verificaErro(mmu.leDado(argumentoInt))) {
						case 0:
							mmu.alteraDado(regAcumulador, mmu.leDado(argumentoInt));
							regContadorPrograma += 1;
							break;
						case 1:
							codigoInterrupcao = InterrupcaoCPU.PAGE_FAULT;
							break;
						default:
							codigoInterrupcao = InterrupcaoCPU.VIOLACAO_DE_MEMORIA;
					}
				} catch(Exception e) {
					codigoInterrupcao = InterrupcaoCPU.VIOLACAO_DE_MEMORIA;
				}
				
				
				break;
				
			case "SOMA" :
				switch(mmu.verificaErro(argumentoInt)) {
					case 0:
						regAcumulador += mmu.leDado(argumentoInt);
						regContadorPrograma += 1;
						break;
					case 1:
						codigoInterrupcao = InterrupcaoCPU.PAGE_FAULT;
						break;
					default:
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
				else
					regContadorPrograma += 1;
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
