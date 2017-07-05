package mensajeriaServer;

import mensajeria.Comando;
import servidor.EscuchaCliente;

public abstract class ComandoServidor extends Comando {

	protected EscuchaCliente escuchador;
	
	public void setEscuchador(EscuchaCliente escuchador) {
		this.escuchador = escuchador;
	}

}

