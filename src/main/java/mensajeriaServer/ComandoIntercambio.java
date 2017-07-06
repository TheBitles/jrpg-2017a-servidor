package mensajeriaServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import dominio.Intercambiable;
import dominio.Item;
import estados.Estado;
import mensajeria.Comando;
import mensajeria.PaqueteIntercambiable;
import mensajeria.PaqueteIntercambio;
import mensajeria.PaquetePersonaje;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class ComandoIntercambio extends ComandoServidor {

	@Override
	public void procesar() {
		
		escuchador.setPaqueteIntercambiable((PaqueteIntercambiable) gson.fromJson(objetoLeido, PaqueteIntercambiable.class));
		PaqueteIntercambiable paqueteIntercambiable = escuchador.getPaqueteIntercambiable();
		Servidor.getIntercambiables().put(paqueteIntercambiable.getPersonajeId(), paqueteIntercambiable);
		
		Map<Integer, PaquetePersonaje> personajesConectados = Servidor.getPersonajesConectados();
		Map<Integer, PaqueteIntercambiable> intercambiables = Servidor.getIntercambiables();
		
		/*for(Intercambiable intercambiable: paqueteIntercambiable.getIntercambiables()) {
			Servidor.log.append("entra");
			Item oferta = intercambiable.getOferta();
			Item demanda = intercambiable.getDemanda();
			String a = oferta == null ? "null" : oferta.getId() + "";
			String b = demanda == null ? "null" : demanda.getId() + "";
			Servidor.log.append(a + " -- " + b);
		}*/
		
		
		Integer usuario1Id;
		Integer usuario2Id;
		ArrayList<Intercambiable> intercambiables1;
		ArrayList<Intercambiable> intercambiables2;
		
		
		try {
    		for(Map.Entry<Integer, PaqueteIntercambiable> intercambiableEntry1 : intercambiables.entrySet()) {
    			
    			usuario1Id = intercambiableEntry1.getKey();
    			intercambiables1 = intercambiableEntry1.getValue().getIntercambiables();
    			
    			//for(Intercambiable intercambiable : intercambiableEntry1.getValue().getIntercambiables()) {
    			//	Servidor.log.append(intercambiableEntry1.getKey() + " - " + intercambiable.getDemanda().getId() + " - " + intercambiable.getOferta().getId() + " // ");
    			//}
    
    			for(Map.Entry<Integer, PaqueteIntercambiable> intercambiableEntry2 : intercambiables.entrySet()) {
    				
					usuario2Id = intercambiableEntry2.getKey();
        			intercambiables2 = intercambiableEntry2.getValue().getIntercambiables();
    				
    				if( usuario1Id != usuario2Id ){

    					int i = 0;
    					for(Intercambiable intercambiable1 : intercambiables1){
    						
    						int j = 0;
    						for(Intercambiable intercambiable2 : intercambiables2){
    							
    							if(Intercambiable.sonIntercambiables(intercambiable1,  intercambiable2)){
    								
    								Servidor.log.append(usuario1Id + ": " + i + " // ");
    								Servidor.log.append(usuario2Id + ": " + j + " // ");
    								
    							}
    							j++;
    						}
    						i++;

    					}
    				}
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    		
		synchronized(Servidor.intercambiables){
			Servidor.intercambiables.notify();
		}

		
		/*

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
		
		synchronized(Servidor.intercambiables){
			Servidor.intercambiables.notify();
		}
		
		*/
		
        //PaqueteIntercambio paqueteIntercambio = new PaqueteIntercambio();
        //paqueteIntercambio.setComando(Comando.INTERCAMBIO);
        
        /*try {
          escuchador.getSalida().writeObject(gson.toJson(paqueteIntercambiado));
        } catch (IOException e) {
          e.printStackTrace();
        }*/
	}

}
