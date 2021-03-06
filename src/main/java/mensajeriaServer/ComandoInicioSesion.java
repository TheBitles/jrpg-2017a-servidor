package mensajeriaServer;

import java.io.IOException;
import mensajeria.Comando;
import mensajeria.Paquete;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;
import servidor.Servidor;

public class ComandoInicioSesion extends ComandoServidor {

	@Override
	public void procesar() {
		Paquete paqueteSv = new Paquete(null, 0);
		paqueteSv.setComando(Comando.INICIOSESION);

		// Recibo el paquete usuario
		escuchador.setPaqueteUsuario((PaqueteUsuario) (gson.fromJson(objetoLeido, PaqueteUsuario.class)));

		// Si se puede loguear el usuario le envio un mensaje de exito y el paquete personaje con los datos
		if (Servidor.getConector().loguearUsuario(escuchador.getPaqueteUsuario())) {

			PaquetePersonaje paquetePersonaje;
			paquetePersonaje = Servidor.getConector().getPersonaje(escuchador.getPaqueteUsuario());
			paquetePersonaje.setComando(Comando.INICIOSESION);
			paquetePersonaje.setMensaje(Paquete.msjExito);

			escuchador.setIdPersonaje(paquetePersonaje.getId());

			try {
				escuchador.getSalida().writeObject(gson.toJson(paquetePersonaje));
			} catch (IOException e) {
				Servidor.log.append("Error de conexion - ComandoInicioSesion" + System.lineSeparator());
			}

		} else {
			paqueteSv.setMensaje(Paquete.msjFracaso);

			try {
				escuchador.getSalida().writeObject(gson.toJson(paqueteSv));
			} catch (IOException e) {
				Servidor.log.append("Error de conexion - ComandoInicioSesion" + System.lineSeparator());
			}
		}
	}

}
