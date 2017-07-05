package mensajeriaServer;

import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;
import servidor.Servidor;

public class ComandoConexion extends ComandoServidor {

	@Override
	public void procesar() {
		escuchador.setPaquetePersonaje((PaquetePersonaje) (gson.fromJson(objetoLeido, PaquetePersonaje.class)).clone());

		Servidor.getPersonajesConectados().put(escuchador.getPaquetePersonaje().getId(), (PaquetePersonaje) escuchador.getPaquetePersonaje().clone());
		Servidor.getUbicacionPersonajes().put(escuchador.getPaquetePersonaje().getId(), (PaqueteMovimiento) new PaqueteMovimiento(escuchador.getPaquetePersonaje().getId()).clone());
		
		synchronized(Servidor.atencionConexiones){
			Servidor.atencionConexiones.notify();
		}	
	}

}
