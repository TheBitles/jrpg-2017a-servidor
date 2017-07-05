package mensajeriaServer;

import java.io.IOException;
import mensajeria.PaquetePersonaje;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class ComandoActualizarPersonaje extends ComandoServidor {

	@Override
	public void procesar() {
		escuchador.setPaquetePersonaje((PaquetePersonaje) gson.fromJson(objetoLeido, PaquetePersonaje.class));
		
		Servidor.getConector().actualizarPersonaje(escuchador.getPaquetePersonaje());
		
		Servidor.getPersonajesConectados().remove(escuchador.getPaquetePersonaje().getId());
		Servidor.getPersonajesConectados().put(escuchador.getPaquetePersonaje().getId(), escuchador.getPaquetePersonaje());

		for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
			try {
				conectado.getSalida().writeObject(gson.toJson(escuchador.getPaquetePersonaje()));
			} catch (IOException e) {
				Servidor.log.append("Error al actualizar personaje" + System.lineSeparator());
				e.printStackTrace();
			}
		}
		
	}

}
