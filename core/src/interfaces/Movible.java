package interfaces;

import com.badlogic.gdx.math.Vector2;
import hilos.HiloServidor;
import mapas.gestorMapas.Mapa;
import personajes.PersonajePrincipal;

public interface Movible {

    public void comportamiento(PersonajePrincipal jugador, PersonajePrincipal jugador2, Mapa mapa, HiloServidor hs, int posMemoria);

}
