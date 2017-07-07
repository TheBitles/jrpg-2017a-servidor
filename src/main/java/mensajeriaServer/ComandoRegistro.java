package mensajeriaServer;

import java.io.IOException;
import mensajeria.Comando;
import mensajeria.Paquete;
import mensajeria.PaqueteUsuario;
import servidor.Servidor;

public class ComandoRegistro extends ComandoServidor {

	@Override
	public void procesar() {
		Paquete paqueteSv = new Paquete(null, 0);
		paqueteSv.setComando(Comando.REGISTRO);

		escuchador.setPaqueteUsuario((PaqueteUsuario) (gson.fromJson(objetoLeido, PaqueteUsuario.class)).clone());

		try {
			// Si el usuario se pudo registrar le envio un msj de exito
			if (Servidor.getConector().registrarUsuario(escuchador.getPaqueteUsuario())) {
				paqueteSv.setMensaje(Paquete.msjExito);
				escuchador.getSalida().writeObject(gson.toJson(paqueteSv));
			// Si el usuario no se pudo registrar le envio un msj de fracaso
			} else {
				paqueteSv.setMensaje(Paquete.msjFracaso);
				escuchador.getSalida().writeObject(gson.toJson(paqueteSv));
			}
		} catch (IOException e) {
			Servidor.log.append("Error de conexion - ComandoRegistro" + System.lineSeparator());
		}
	}

}
