package mensajeriaServer;

import java.io.IOException;
import estados.Estado;
import mensajeria.PaqueteBatalla;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class ComandoBatalla extends ComandoServidor {

	@Override
	public void procesar() {
		// Le reenvio al id del personaje batallado que quieren pelear
		escuchador.setPaqueteBatalla((mensajeria.PaqueteBatalla) escuchador.gson.fromJson(objetoLeido, PaqueteBatalla.class));
		Servidor.log.append(escuchador.getPaqueteBatalla().getId() + " quiere batallar con " + escuchador.getPaqueteBatalla().getIdEnemigo() + System.lineSeparator());
		
		//seteo estado de batalla
		Servidor.getPersonajesConectados().get(escuchador.getPaqueteBatalla().getId()).setEstado(Estado.estadoBatalla);
		Servidor.getPersonajesConectados().get(escuchador.getPaqueteBatalla().getIdEnemigo()).setEstado(Estado.estadoBatalla);
		escuchador.getPaqueteBatalla().setMiTurno(true);
		try {
			escuchador.salida.writeObject(escuchador.gson.toJson(escuchador.getPaqueteBatalla()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(EscuchaCliente conectado : Servidor.getClientesConectados()){
			if(conectado.getIdPersonaje() == escuchador.getPaqueteBatalla().getIdEnemigo()){
				int aux = escuchador.getPaqueteBatalla().getId();
				escuchador.getPaqueteBatalla().setId(escuchador.getPaqueteBatalla().getIdEnemigo());
				escuchador.getPaqueteBatalla().setIdEnemigo(aux);
				escuchador.getPaqueteBatalla().setMiTurno(false);
				try {
					conectado.getSalida().writeObject(escuchador.gson.toJson(escuchador.getPaqueteBatalla()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		
		synchronized(Servidor.atencionConexiones){
			Servidor.atencionConexiones.notify();
		}
	}

}
