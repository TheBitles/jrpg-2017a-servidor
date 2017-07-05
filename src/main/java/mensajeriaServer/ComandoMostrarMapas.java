package mensajeriaServer;

import mensajeria.PaquetePersonaje;
import servidor.Servidor;

public class ComandoMostrarMapas extends ComandoServidor {

	@Override
	public void procesar() {
		escuchador.setPaquetePersonaje((PaquetePersonaje) gson.fromJson(objetoLeido, PaquetePersonaje.class));
		Servidor.log.append(escuchador.socket.getInetAddress().getHostAddress() + " ha elegido el mapa " + escuchador.getPaquetePersonaje().getMapa() + System.lineSeparator());
	}

}
