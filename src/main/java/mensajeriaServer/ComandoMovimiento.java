package mensajeriaServer;

import mensajeria.PaqueteMovimiento;
import servidor.Servidor;

public class ComandoMovimiento extends ComandoServidor {

	@Override
	public void procesar() {
		escuchador.setPaqueteMovimiento((mensajeria.PaqueteMovimiento) (gson.fromJson((String) objetoLeido, PaqueteMovimiento.class)));
		
		PaqueteMovimiento paqueteMovimiento = escuchador.getPaqueteMovimiento();
		PaqueteMovimiento ubicacionPersonaje = Servidor.getUbicacionPersonajes().get(paqueteMovimiento.getIdPersonaje());
		
		ubicacionPersonaje.setPosX(paqueteMovimiento.getPosX());
		ubicacionPersonaje.setPosY(paqueteMovimiento.getPosY());
		ubicacionPersonaje.setDireccion(paqueteMovimiento.getDireccion());
		ubicacionPersonaje.setFrame(paqueteMovimiento.getFrame());
		
		synchronized(Servidor.atencionMovimientos){
			Servidor.atencionMovimientos.notify();
		}
	}

}
