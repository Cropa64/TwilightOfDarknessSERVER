package pantallas;

import com.badlogic.gdx.Screen;
import juego.TwilightOfDarknessPrincipal;
import mapas.gestorMapas.Mapa;
import personajes.PersonajePrincipal;
import utilidades.*;

public class PantallaPrincipal implements Screen {

    private PersonajePrincipal jugador1, jugador2;
    private Entrada entrada;
    private Mapa mapa;
    private Mapa mapaSiguiente;
    private boolean cambiarMapa;
    private Hud hud;
    private TwilightOfDarknessPrincipal game;

    public PantallaPrincipal(Entrada entrada, TwilightOfDarknessPrincipal game, Mapa mapa, PersonajePrincipal jugador1, PersonajePrincipal jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.game = game;
        this.mapa = mapa;
        this.entrada = entrada;
    }

    @Override
    public void show() {
//		mapa.crear();
        hud = new Hud(jugador1);
       //TODO hud = new Hud(jugador2);
    }

    @Override
    public void render(float delta) {

        actualizar();

        Render.limpiarPantalla();
        Utiles.sr.setProjectionMatrix(mapa.getCamara().combined);

        mapa.setearCamara(jugador1.getPosicion().x, jugador1.getPosicion().y);
        mapa.renderizar();
//        cambiarMapa = mapa.comprobarSalidaMapa();
//
//        if (cambiarMapa) {
//            mapaSiguiente = mapa.cambioMapa();
//            cambiarMapa(mapaSiguiente);
//        }

//        mapa.mostrarColisiones();
//        jugador1.mostrarColisiones();
//        jugador2.mostrarColisiones();

//        jugador1.controlarMovimiento(entrada, mapa);

        hud.mostrarHud();
        jugador1.controlarInventario();

//		System.out.println("X Jugador: "+jugador.getPosicion().x);
//		System.out.println("Y Jugador: "+jugador.getPosicion().y);

    }

//    public void cambiarMapa(Mapa mapa) {
//        this.mapa = mapa;
//        this.mapa.setPosicionJugador();
//    }

    public void controlarSalida() {
        if (entrada.pressedEsc) {
            Recursos.posJugadorX = (int) jugador1.getPosicion().x;
            Recursos.posJugadorY = (int) jugador1.getPosicion().y;

            game.establecerConexion(false);
            System.exit(0);
        }
    }

    private void actualizar() {
        controlarSalida();
    }

    @Override
    public void resize(int width, int height) {


    }

    @Override
    public void pause() {


    }

    @Override
    public void resume() {


    }

    @Override
    public void hide() {


    }

    @Override
    public void dispose() {
        Utiles.batch.dispose();
        mapa.dispose();
        Utiles.sr.dispose();
    }

}
