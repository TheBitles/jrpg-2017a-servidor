package mensajeriaServer;

import java.io.IOException;
import mensajeria.PaquetePersonaje;
import servidor.Servidor;

public class ComandoCrearPersonaje extends ComandoServidor {

	@Override
	public void procesar() {
		// Casteo el paquete personaje
		escuchador.setPaquetePersonaje((PaquetePersonaje) (gson.fromJson(objetoLeido, PaquetePersonaje.class)));
		
		// Guardo el personaje en ese usuario
		Servidor.getConector().registrarPersonaje(escuchador.getPaquetePersonaje(), escuchador.getPaqueteUsuario());
		
		// Le envio el id del personaje
		try {
			escuchador.getSalida().writeObject(gson.toJson(escuchador.getPaquetePersonaje(), escuchador.getPaquetePersonaje().getClass()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
