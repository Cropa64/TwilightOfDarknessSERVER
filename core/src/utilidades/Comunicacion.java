package utilidades;

import com.badlogic.gdx.math.Vector2;
import hilos.DireccionRed;
import hilos.HiloServidor;
import juego.TwilightOfDarknessPrincipal;

import java.net.InetAddress;

public class Comunicacion {

    private boolean first = false;
    private HiloServidor hs;
    private TwilightOfDarknessPrincipal game;
    private Vector2 posicionJ1, posicionJ2;
    private String posJ1 = "", posJ2 = "";

    public Comunicacion(HiloServidor hiloServidor, TwilightOfDarknessPrincipal game) {
        this.hs = hiloServidor;
        this.game = game;
    }

//    public void actualizacionAlien(DireccionRed[] clientes){
//
//        int idAlien = 0;
//        float posX = 0;
//        float posY = 0;
//        for(int i = 0; i < game.getMapas().getMapaDungeon1().getAliensSize(); i++){
//            idAlien = game.getMapas().getMapaDungeon1().getAliens(i).getIdAlien();
//            posX = game.getMapas().getMapaDungeon1().getAliens(i).getPosicion().x;
//            posY = game.getMapas().getMapaDungeon1().getAliens(i).getPosicion().y;
//            hs.enviarMensaje("ACTUALIZACION" + "-" + idAlien + "-" + i + "-" +posX + "-" + posY, clientes[0].getIp(), clientes[0].getPuerto());
//        }
//        game.getMapas().getMapaDungeon1().crear();
//
//    }

    public void movimientoJugador(String msg, String coord, HiloServidor hs, DireccionRed[] clientes, int posI) {

        int nroCliente = clientes[posI].getNroCliente();

        if(nroCliente == 0){
            posicionJ1 = game.getJugador1().controlarMovimiento(msg, coord, game.getMapas().getMapaDungeon1(), hs);
            posJ1 = nroCliente + "-MOVJ-" + posicionJ1.x + "-" + posicionJ1.y;

            System.out.println("POSICION J1: "+ posJ1);

            hs.enviarMensaje(posJ1, clientes[1].getIp(), clientes[1].getPuerto());
            //hs.enviarMensajeATodos(posJ1);
        }else{
            posicionJ2 = game.getJugador2().controlarMovimiento(msg, coord, game.getMapas().getMapaDungeon1(), hs);
            posJ2 = nroCliente + "-MOVJ-" + posicionJ2.x + "-" + posicionJ2.y;

            System.out.println("POSICION J2:" + posJ2);
            hs.enviarMensaje(posJ2, clientes[0].getIp(), clientes[0].getPuerto());
            //hs.enviarMensajeATodos(posJ2);
        }
    }

    public void clicJugador(String msg, HiloServidor hs, DireccionRed[] clientes, int posI){
        int nroCliente = clientes[posI].getNroCliente();
        if(msg.equals("Izq")){
            if(nroCliente == 0){
                game.getJugador1().setAtacando(true);
            }else{
                game.getJugador2().setAtacando(true);
            }
        }else if(msg.equals("noIzq")) {
            if(nroCliente == 0){
                game.getJugador1().setAtacando(false);
            }else{
                game.getJugador2().setAtacando(false);
            }
        }
    }

    public void accionJugador(String msg, HiloServidor hs, DireccionRed[] clientes, int posI){
        int nroCliente = clientes[posI].getNroCliente();
        if(nroCliente == 0){
            if(msg.equals("presioneShift")){
                game.getJugador1().setCorriendo(true);
            }else{
                game.getJugador1().setCorriendo(false);
            }
        }else{
            if(msg.equals("presioneShift")){
                game.getJugador2().setCorriendo(true);
            }else{
                game.getJugador2().setCorriendo(false);
            }
        }
    }

}