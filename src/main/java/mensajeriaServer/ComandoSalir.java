package mensajeriaServer;

import java.io.IOException;
import mensajeria.Paquete;
import servidor.Servidor;

public class ComandoSalir extends ComandoServidor {

	@Override
	public void procesar() {

		try {
			escuchador.getEntrada().close();
			escuchador.getSalida().close();
			escuchador.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Servidor.getClientesConectados().remove(this);
		Paquete paquete = (Paquete) gson.fromJson(objetoLeido, Paquete.class);
		Servidor.log.append(paquete.getIp() + " se ha desconectado." + System.lineSeparator());
	}

}
