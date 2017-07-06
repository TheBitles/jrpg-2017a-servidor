package mensajeriaServer;

import java.io.IOException;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class ComandoAtacar extends ComandoServidor {

	@Override
	public void procesar() {
		escuchador.setPaqueteAtacar((mensajeria.PaqueteAtacar) gson.fromJson(objetoLeido, mensajeria.PaqueteAtacar.class));

		for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
			if(conectado.getIdPersonaje() == escuchador.getPaqueteAtacar().getIdEnemigo()) {
				try {
					conectado.getSalida().writeObject(gson.toJson(escuchador.getPaqueteAtacar()));
				} catch (IOException e) {
					Servidor.log.append("Error al atacar" + System.lineSeparator());
					e.printStackTrace();
				}
			}
		}
	}

}
