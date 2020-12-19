package cpu;

import java.util.Arrays;

import cpu.CpuEstado;
import enums.Interrupcao;

public class Cpu {
	int regContadorPrograma, regAcumulador;
	Interrupcao codigoInterrupcao;
	String[] memoriaPrograma;
	int[] memoriaDados;
	
	public Cpu(int tamInstrucoes, int tamDados) {
		this.memoriaPrograma = new String[tamInstrucoes];
		this.memoriaDados = new int[tamDados];
		this.estadoInicializa();
	}
	
	public void alteraPrograma(String[] memoriaPrograma) {
		//Arrays.fill(this.memoriaPrograma, null);
		this.memoriaPrograma = new String[memoriaPrograma.length];
		
		for(int i=0; i<memoriaPrograma.length; i++) {
			this.memoriaPrograma[i] = memoriaPrograma[i];
		}
	}
	
	public void alteraDados(int[] memoriaDados) {
		for(int i=0; i<memoriaDados.length; i++) {
			memoriaDados[i] = this.memoriaDados[i];
		}
	}
	
	public int[] salvaDados() {
		return this.memoriaDados;
	}
	
	public Interrupcao getCodigotInterrupcao() {
		return this.codigoInterrupcao;
	}
	
	public void resetaCodigoInterrupcao(){
		if(this.codigoInterrupcao == Interrupcao.NORMAL) return;
		
		this.codigoInterrupcao = Interrupcao.NORMAL;
		this.regContadorPrograma += 1;
	}
	
	public String instrucaoAtual() {
		if(this.regContadorPrograma < this.memoriaPrograma.length)
			return this.memoriaPrograma[this.regContadorPrograma];
		
		return "Posicao de CPU invalida";
	}
	
	public void executa() {
		String[] comandoSeparado; 
		String instrucao;
		int argumentoInt = 0;
		String argumentoString;
		
		comandoSeparado = this.memoriaPrograma[this.regContadorPrograma].split(" ");
		
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
				this.regAcumulador = argumentoInt;
				this.regContadorPrograma += 1;
				break;
				
			case "CARGM" :
				try {
					this.regAcumulador = this.memoriaDados[argumentoInt];
					this.regContadorPrograma += 1;
				} catch (Exception e) {
					System.out.println("Ocorreu uma violacao de memoria");
					this.codigoInterrupcao = Interrupcao.VIOLACAO_DE_MEMORIA;
				}
				break;
				
			case "CARGX" :
				try {
					this.regAcumulador = this.memoriaDados[this.memoriaDados[argumentoInt]];
					this.regContadorPrograma += 1;
				} catch (Exception e) {
					System.out.println("Ocorreu uma violacao de memoria");
					this.codigoInterrupcao = Interrupcao.VIOLACAO_DE_MEMORIA;
				}
				break;
				
			case "ARMM" :
				try {
					this.memoriaDados[argumentoInt] = this.regAcumulador;
					this.regContadorPrograma += 1;
				} catch (Exception e) {
					System.out.println("Ocorreu uma violacao de memoria");
					this.codigoInterrupcao = Interrupcao.VIOLACAO_DE_MEMORIA;
				}
				break;
				
			case "ARMX" :
				try {
					this.memoriaDados[this.memoriaDados[argumentoInt]] = this.regAcumulador;
					this.regContadorPrograma += 1;
				} catch (Exception e) {
					System.out.println("Ocorreu uma violacao de memoria");
					this.codigoInterrupcao = Interrupcao.VIOLACAO_DE_MEMORIA;
				}
				break;
				
			case "SOMA" :
				try {
					this.regAcumulador += this.memoriaDados[argumentoInt];
					this.regContadorPrograma += 1;
				} catch (Exception e) {
					System.out.println("Ocorreu uma violacao de memoria");
					this.codigoInterrupcao = Interrupcao.VIOLACAO_DE_MEMORIA;
				}
				break;
				
			case "NEG" :
				this.regAcumulador *= -1;
				this.regContadorPrograma += 1;
				break;
				
			case "DESVZ" :
				if(this.regAcumulador == 0)
					this.regContadorPrograma = argumentoInt;
				break;
				
			default :
				this.codigoInterrupcao = Interrupcao.INSTRUCAO_ILEGAL;
		}
	}
	
	public CpuEstado salvaEstado() {
		return new CpuEstado(this.regContadorPrograma, this.regAcumulador, this.codigoInterrupcao);
	}
	
	public void estadoInicializa() {
		CpuEstado cpuEstado = new CpuEstado();
		this.regContadorPrograma = cpuEstado.regContadorPrograma;
		this.regAcumulador = cpuEstado.regAcumulador;
		this.codigoInterrupcao = cpuEstado.codigoInterrupcao;
	}
	
	public void alteraEstado(CpuEstado cpuEstado) {
		this.regContadorPrograma = cpuEstado.regContadorPrograma;
		this.regAcumulador = cpuEstado.regAcumulador;
		this.codigoInterrupcao = cpuEstado.codigoInterrupcao;
	}
	
	public void setAcumulador(int novoAcumulador) {
		this.regAcumulador = novoAcumulador;
	}
	
	public int getAcumulador() {
		return this.regAcumulador;
	}
	
	public void cpuDormindo() {
		this.codigoInterrupcao = Interrupcao.DORMINDO;
	}
}
