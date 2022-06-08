package hilos;

import juego.TwilightOfDarknessPrincipal;
import utilidades.Comunicacion;
import utilidades.Recursos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HiloServidor extends Thread{

    private DatagramSocket conexion;
    private int puerto = 2222;
    private boolean fin = false;
    private int cantClientes = 0;
    private DireccionRed[] clientes = new DireccionRed[2];
    private TwilightOfDarknessPrincipal game;
    private Comunicacion com;
    private DireccionRed[] clientesAuxiliar = new DireccionRed[2];
    private int nroClienteAux = 0;
    private boolean lan = false;
    private boolean player1 = false;
    private boolean player2 = false;
    private boolean conectado = false;


    public HiloServidor(TwilightOfDarknessPrincipal game){
        this.game = game;
        com = new Comunicacion(this, game );
        try {
            conexion = new DatagramSocket(puerto);
            System.out.println("CONEXION EN CURSO");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        do {
            //System.out.println("CORRIENDO... " + number++ );
            byte[] data = new byte[1024];
            DatagramPacket dp = new DatagramPacket(data, data.length);
            try {
                conexion.receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            procesarMensaje(dp);
        }while(!fin);
    }

    private void procesarMensaje(DatagramPacket dp) {
        String msg = (new String(dp.getData())).trim();
        String[] mensajeParametrizado = msg.split("-");
        System.out.println(msg);
        int nroCliente = -1;

        if (cantClientes > 1) {
            for (int i = 0; i < clientes.length; i++) {
                if (dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())) {
                    nroCliente = i;
                }
            }
        }

        if(cantClientes<2) {
            if(msg.equals("Conexion")) {
                if(cantClientes<2) {
                    if(!player1 && player2){
                        clientes[0] = new DireccionRed(dp.getAddress(),dp.getPort(), 0);
                        enviarMensaje("OK-"+(1) + "-" + 0,clientes[0].getIp(),clientes[0].getPuerto());
                        cantClientes = 2;
                        //enviarMensaje("CREACION-" + game.getJugador2().getPosicion().x + "-" + game.getJugador2().getPosicion().x,clientes[1].getIp(),clientes[1].getPuerto());
                        //com.actualizacionAlien(clientes);
                    }else if(player1 && !player2){
                        clientes[1] = new DireccionRed(dp.getAddress(),dp.getPort(), 1);
                        enviarMensaje("OK-"+(2) + "-" + 1,clientes[1].getIp(),clientes[1].getPuerto());
                        cantClientes = 2;
                        //enviarMensaje("CREACION-" + game.getJugador1().getPosicion().x + "-" + game.getJugador1().getPosicion().x,clientes[1].getIp(),clientes[1].getPuerto());
                        //com.actualizacionAlien(clientes);
                    }else{
                        clientes[cantClientes] = new DireccionRed(dp.getAddress(),dp.getPort(), cantClientes);
                        enviarMensaje("OK-"+(cantClientes+1) + "-" + cantClientes,clientes[cantClientes].getIp(),clientes[cantClientes++].getPuerto());
                    }
                    if(cantClientes==2) {
                        for (int i = 0; i < clientes.length; i++) {
                            enviarMensaje("Empieza",clientes[i].getIp(),clientes[i].getPuerto());
                            //Recursos.empieza = true;
                            player1 = true;
                            player2 = true;
                        }
                        //CONTROL LAN - DOS JUGADORES
                        if(clientes[0].getIp().equals(clientes[1].getIp())){
                            lan = true;
                        }
                    }
                }
            }

            else if(msg.equals("BYE") && player1){
                for (int i = 0; i < cantClientes; i++) {
                    if(dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())){
                        clientes[i] = null;
                        cantClientes = 0;
                    }
                }
            }else if(msg.equals("BYE") && !player1){
                clientes[1] = null;
                cantClientes = 0;
            }

        }else {
            if(nroCliente!=-1){

                 //EN LINEA

                    if(mensajeParametrizado[0].equals("MOVJ")){
                        for (int i = 0; i < clientes.length; i++) {
                            if(dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())){
                                com.movimientoJugador(mensajeParametrizado[1], mensajeParametrizado[2],this, clientes, i);
                            }
                        }

                    } else if(mensajeParametrizado[0].equals("CLIC")){
                        for (int i = 0; i < clientes.length; i++) {
                            if(dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())){
                                com.clicJugador(mensajeParametrizado[1], this, clientes, i);
                            }
                        }

                    }else if(mensajeParametrizado[0].equals("ACC")){
                        for (int i = 0; i < clientes.length; i++) {
                            if(dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())){
                                com.accionJugador(mensajeParametrizado[1], this, clientes, i);
                            }
                        }

                    }


//                else{ //EN LAN
//
//                    if(mensajeParametrizado[0].equals("MOVJ")){
//                        for (int i = 0; i < clientes.length; i++) {
//                            if(dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())){
//                                com.movimientoJugador(mensajeParametrizado[1], mensajeParametrizado[2],this, clientes, i);
//                            }
//                        }
//
//                    } else if(mensajeParametrizado[0].equals("CLIC")){
//                        for (int i = 0; i < clientes.length; i++) {
//                            if(dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())){
//                                com.clicJugador(mensajeParametrizado[1], this, clientes, i);
//                            }
//                        }
//
//                    }else if(mensajeParametrizado[0].equals("ACC")){
//                        for (int i = 0; i < clientes.length; i++) {
//                            if(dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())){
//                                com.accionJugador(mensajeParametrizado[1], this, clientes, i);
//                            }
//                        }
//
//                    }
//                }
                if(msg.equals("BYE")){
                    for (int i = 0; i < cantClientes; i++) {
                        if(dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())){
                            limpiezaClientes(i);
                        }
                    }
                }

            }
        }
    }

    private void limpiezaClientes(int numero) {
        if(numero == 0){
            clientes[numero] = null;
            player1 = false;
        }else if(numero == 1){
            clientes[numero] = null;
            player2= false;
        }
        conectado = true;
        cantClientes--;

        if(cantClientes == 0){
            terminar();
        }
    }

    //                   if (dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())) {
//                           nroCliente = i;
//                           com.accionJugador(mensajeParametrizado[1], this, clientes[i].getIp(), clientes[i].getPuerto());
//                       }

    public void enviarMensaje(String msg, InetAddress ip, int puerto) {
        byte[] data = msg.getBytes();
        DatagramPacket dp = new DatagramPacket(data, data.length,ip,puerto);
        try {
            conexion.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensajeATodos(String msg) {
        if(player1 && player2){
            for (int i = 0; i < cantClientes; i++) {
                enviarMensaje(msg, clientes[i].getIp(), clientes[i].getPuerto());
            }
        }
    }

    public void terminar(){
        fin = true;
    }
}