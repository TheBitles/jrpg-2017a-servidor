package mensajeriaServer;

import java.io.IOException;
import java.util.Map;

import mensajeria.Comando;
import mensajeria.PaqueteMensaje;
import mensajeria.PaquetePersonaje;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class ComandoConversar extends ComandoServidor {

	@Override
	public void procesar() {
		PaqueteMensaje paqueteMensaje = (PaqueteMensaje) (gson.fromJson(objetoLeido, PaqueteMensaje.class));
		paqueteMensaje.setComando(Comando.CONVERSAR);

		String receptor = paqueteMensaje.getReceptor();
		String emisor = paqueteMensaje.getEmisor();
		String usuario = receptor == null ? emisor : receptor;

		// wtf: enviar el id para evitar esta negrada.
		Integer usuarioId = -1;
		for (Map.Entry<Integer, PaquetePersonaje> personajeConectado : Servidor.getPersonajesConectados().entrySet()) {
			Servidor.log.append(personajeConectado.getValue().getNombre() + " == " + usuario + "\n");

			if(personajeConectado.getValue().getNombre().equals(usuario)) {
				usuarioId = personajeConectado.getValue().getId();
			}
		}

		Integer personajeId;
		for (EscuchaCliente clienteConectado : Servidor.getClientesConectados()) {
			personajeId = clienteConectado.getIdPersonaje();

			boolean condicionPrivado = personajeId == usuarioId && receptor != null; // es mensaje privado y lo esta recibiendo el personaje correspondiente
			boolean condicionPublico = personajeId != usuarioId && receptor == null; // es mensaje publico y no lo recibe el personaje enviador

			if(condicionPrivado || condicionPublico) {
				try {
					clienteConectado.getSalida().writeObject(gson.toJson(paqueteMensaje));
				} catch (IOException e) {
					Servidor.log.append("Error de conexion - PaqueteMensaje");
				}
			}
		}
	}
}
