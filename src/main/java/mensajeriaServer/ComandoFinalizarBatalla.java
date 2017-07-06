package mensajeriaServer;

import java.io.IOException;
import estados.Estado;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class ComandoFinalizarBatalla extends ComandoServidor {

	@Override
	public void procesar(){
		
		escuchador.setPaqueteFinalizarBatalla((mensajeria.PaqueteFinalizarBatalla) gson.fromJson(objetoLeido, mensajeria.PaqueteFinalizarBatalla.class));
		Servidor.getPersonajesConectados().get(escuchador.getPaqueteFinalizarBatalla().getId()).setEstado(Estado.estadoJuego);
		Servidor.getPersonajesConectados().get(escuchador.getPaqueteFinalizarBatalla().getIdEnemigo()).setEstado(Estado.estadoJuego);
		for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
			if(conectado.getIdPersonaje() == escuchador.getPaqueteFinalizarBatalla().getIdEnemigo()) {
				try {
					conectado.getSalida().writeObject(gson.toJson(escuchador.getPaqueteFinalizarBatalla()));
				} catch (IOException e) {
					Servidor.log.append("Error al finalizar batalla" + System.lineSeparator());
					e.printStackTrace();
				}
			}
		}
		
		synchronized(Servidor.atencionConexiones){
			Servidor.atencionConexiones.notify();
		}
	}


}