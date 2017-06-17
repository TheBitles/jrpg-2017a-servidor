package mensajeriaServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import estados.Estado;
import mensajeria.PaqueteFinalizarBatalla;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class PaqueteFinalizarBatallaServer extends EscuchaCliente implements mensajeriaServer.Paquete{

	public PaqueteFinalizarBatallaServer(String ip, Socket socket, ObjectInputStream entrada,
			ObjectOutputStream salida) {
		super(ip, socket, entrada, salida);
	}
	
	public void ejecutar(){
		
		paqueteFinalizarBatalla = (PaqueteFinalizarBatalla) gson.fromJson(cadenaLeida, PaqueteFinalizarBatalla.class);
		Servidor.getPersonajesConectados().get(paqueteFinalizarBatalla.getId()).setEstado(Estado.estadoJuego);
		Servidor.getPersonajesConectados().get(paqueteFinalizarBatalla.getIdEnemigo()).setEstado(Estado.estadoJuego);
		for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
			if(conectado.getIdPersonaje() == paqueteFinalizarBatalla.getIdEnemigo()) {
				try {
					conectado.getSalida().writeObject(gson.toJson(paqueteFinalizarBatalla));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		synchronized(Servidor.atencionConexiones){
			Servidor.atencionConexiones.notify();
		}
	}


}