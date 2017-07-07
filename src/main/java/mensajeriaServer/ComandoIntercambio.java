package mensajeriaServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import dominio.Intercambiable;
import mensajeria.PaqueteIntercambiable;
import mensajeria.PaqueteIntercambio;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class ComandoIntercambio extends ComandoServidor {

	@Override
	public void procesar() {
		escuchador.setPaqueteIntercambiable((PaqueteIntercambiable) gson.fromJson(objetoLeido, PaqueteIntercambiable.class));
		PaqueteIntercambiable paqueteIntercambiable = escuchador.getPaqueteIntercambiable();
		Servidor.getIntercambiables().put(paqueteIntercambiable.getPersonajeId(), paqueteIntercambiable);
		Map<Integer, PaqueteIntercambiable> intercambiables = Servidor.getIntercambiables();

		Integer usuario1Id;
		Integer usuario2Id;
		ArrayList<Intercambiable> intercambiables1;
		ArrayList<Intercambiable> intercambiables2;
		Set<Integer> usuariosRevisados = new HashSet<>();
		PaqueteIntercambio paqueteIntercambio = new PaqueteIntercambio();;

		for(Map.Entry<Integer, PaqueteIntercambiable> intercambiableEntry1 : intercambiables.entrySet()) {

			usuario1Id = intercambiableEntry1.getKey();
			intercambiables1 = intercambiableEntry1.getValue().getIntercambiables();
			usuariosRevisados.add(usuario1Id);

			for(Map.Entry<Integer, PaqueteIntercambiable> intercambiableEntry2 : intercambiables.entrySet()) {

				usuario2Id = intercambiableEntry2.getKey();
    			intercambiables2 = intercambiableEntry2.getValue().getIntercambiables();

				if( !usuariosRevisados.contains(usuario2Id) ){

					int i = 0;
					for(Intercambiable intercambiable1 : intercambiables1){

						int j = 0;
						for(Intercambiable intercambiable2 : intercambiables2){

							if(Intercambiable.intercambiar(intercambiable1,  intercambiable2)){

								for (EscuchaCliente clienteConectado : Servidor.getClientesConectados()) {

									try {

    									if( clienteConectado.getIdPersonaje() == usuario1Id ) {
    										paqueteIntercambio.setIntercambio(i);
    										clienteConectado.getSalida().writeObject(gson.toJson(paqueteIntercambio));
    									}

    									if( clienteConectado.getIdPersonaje() == usuario2Id ) {
    										paqueteIntercambio.setIntercambio(j);
    										clienteConectado.getSalida().writeObject(gson.toJson(paqueteIntercambio));

    									}
    								} catch (IOException e) {
    									Servidor.log.append("Error de conexion - ComandoIntercambio" + System.lineSeparator());
    								}
								}

							}
							j++;

						}
						i++;

					}
				}
			}
		}

		synchronized(Servidor.intercambiables){
			Servidor.intercambiables.notify();
		}
	}
}
