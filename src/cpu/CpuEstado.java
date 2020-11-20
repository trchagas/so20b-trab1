package cpu;

public class CpuEstado {
	enum Estado {
		NORMAL,
		INSTRUCAO_ILEGAL,
	    VIOLACAO_DE_MEMORIA
	};
	
	int regContadorPrograma, regAcumulador, codigoInterrupcao;
	
	public CpuEstado() {
		this.regContadorPrograma = 0;
		this.regAcumulador = 0;
		this.codigoInterrupcao = Estado.NORMAL.ordinal(); 
	}
}
