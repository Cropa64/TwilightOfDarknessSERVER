package mapas.gestorMapas;

import hilos.HiloServidor;
import juego.TwilightOfDarknessPrincipal;
import mapas.dungeons.Dungeon1;
import personajes.PersonajePrincipal;

public class GestorMapas {

    private Mapa pueblo;
    private Mapa pueblo2;
    private Mapa dungeon1;
    private TwilightOfDarknessPrincipal game;

    public void crearMapas(PersonajePrincipal jugador1, PersonajePrincipal jugador2, TwilightOfDarknessPrincipal game, HiloServidor hs) {
      //  pueblo = new Pueblo(jugador1, jugador2, this);
      //  pueblo2 = new Pueblo2(jugador1, jugador2, this);
        dungeon1 = new Dungeon1(jugador1, jugador2, this, game, hs);
      //  pueblo.crear();
      //  pueblo2.crear();
        dungeon1.crear();
    }

    public Mapa getMapaPueblo() {
        return pueblo;
    }

    public Mapa getMapaPueblo2() {
        return pueblo2;
    }

    public Mapa getMapaDungeon1() {
        return dungeon1;
    }

}
