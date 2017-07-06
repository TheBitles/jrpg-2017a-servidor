package mensajeriaServer;

import java.io.IOException;
import mensajeria.Comando;
import mensajeria.Paquete;
import mensajeria.PaqueteInventario;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;
import servidor.Servidor;

public class ComandoGuardarInventario extends ComandoServidor {

	@Override
	public void procesar() {
		escuchador.setPaqueteInventario((PaqueteInventario) gson.fromJson(objetoLeido, PaqueteInventario.class));
		PaqueteInventario paqueteInventario = escuchador.getPaqueteInventario();
		Servidor.getConector().actualizarItems(paqueteInventario.getPersonajeId(), paqueteInventario.getItems());
	}

}
