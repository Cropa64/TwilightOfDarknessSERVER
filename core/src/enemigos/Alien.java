package enemigos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import hilos.HiloServidor;
import interfaces.Movible;
import mapas.gestorMapas.Mapa;
import personajes.PersonajePrincipal;
import utilidades.Utiles;

public class Alien extends Enemigo implements Movible {

    public Texture alienAbajo = new Texture("enemigos/alien/alienQuietoAbajo.png");
    public Sprite alienAbajoSprite = new Sprite(alienAbajo);

    public Texture alienArriba = new Texture("enemigos/alien/alienQuietoArriba.png");
    public Sprite alienArribaSprite = new Sprite(alienArriba);

    public Texture alienDerecha = new Texture("enemigos/alien/alienQuietoDerecha.png");
    public Sprite alienDerechaSprite = new Sprite(alienDerecha);

    public Texture alienIzquierda = new Texture("enemigos/alien/alienQuietoIzquierda.png");
    public Sprite alienIzquierdaSprite = new Sprite(alienIzquierda);

    private Rectangle rectanguloAlien;
    private int idAlien = 0;
    private Circle circuloAlien;
    private final float VELOCIDAD = 0.7f;
    private final int FILAS = 4, COLUMNAS = 3;
    private float tiempoAni = 0;
    private Animation<TextureRegion> animacionIzquierda, animacionDerecha, animacionArriba, animacionAbajo, animacionAtaqueDerecha, animacionAtaqueAbajo, animacionAtaqueIzquierda, animacionAtaqueArriba;
    private Animation<TextureRegion> animacionActual;
    private int correccionAncho = 25, correccionAlto = 120;
    private String direccionAlien = "", posicionFinal = "";
    private boolean enMovimiento = false, atacando, enColision, isEnCombate = false, combateOnline = false;
    private int danio = 10;
    private boolean muerto = false;
    private PersonajePrincipal jugadorPelea = null;

    public Alien(int idAlien) {
        super.set(alienAbajoSprite);
        rectanguloAlien = new Rectangle(getBoundingRectangle());
        circuloAlien = new Circle(this.posicion.x, this.posicion.y, 150);
        this.idAlien = idAlien;
        crearAnimacion();
        vidaMax = 100;
        vida = vidaMax;
    }

    @Override
    public void dibujar(Texture textura) {

        this.setTexture(textura);
        this.draw(Utiles.batch);

        this.setX(this.posicion.x);
        this.setY(this.posicion.y);

    }

    @Override
    public void dibujar(TextureRegion tr) {
        Utiles.batch.draw(tr, posicion.x, posicion.y, tr.getRegionWidth(), tr.getRegionHeight());
    }

    public void barraVida() {

        int anchoVida = (vida * 100) / vidaMax;

        Utiles.sr.setAutoShapeType(true);
        Utiles.sr.begin();
        Utiles.sr.set(ShapeType.Line);
        Utiles.sr.setColor(Color.BLACK);
        Utiles.sr.rect(getPosicion().x - 10, getPosicion().y + getHeight(), 100 * 0.5f, 10);

        Utiles.sr.set(ShapeType.Filled);
        Utiles.sr.setColor(Color.GREEN);
        Utiles.sr.rect(getPosicion().x - 10, getPosicion().y + getHeight(), anchoVida * 0.5f, 10);
        Utiles.sr.end();
    }

    @Override
    public void hacerDanio(PersonajePrincipal jugador) {
        if (!isMuerto()) {
            jugador.recibirDanio(danio);
        }
    }

    @Override
    public void recibirDanio(float cantidad) {
        this.vida -= cantidad;
    }

    public void crearAnimacion() {

        Texture alienAnimacion = new Texture("enemigos/alien/alienPack.png");
        Texture alienAtaque = new Texture("enemigos/alien/alienAtaque.png");
        TextureRegion[][] tmp = TextureRegion.split(alienAnimacion, (alienAnimacion.getWidth() - correccionAncho) / COLUMNAS, (alienAnimacion.getHeight() - correccionAlto) / FILAS);
        TextureRegion[][] tmp2 = TextureRegion.split(alienAtaque, (alienAtaque.getWidth() - correccionAncho) / COLUMNAS, (alienAtaque.getHeight() - correccionAlto) / FILAS);
        TextureRegion[] walkFramesDerecha = new TextureRegion[COLUMNAS];
        TextureRegion[] walkFramesIzquierda = new TextureRegion[COLUMNAS];
        TextureRegion[] walkFramesArriba = new TextureRegion[COLUMNAS];
        TextureRegion[] walkFramesAbajo = new TextureRegion[COLUMNAS];

        TextureRegion[] framesAtaqueDerecha = new TextureRegion[COLUMNAS];
        TextureRegion[] framesAtaqueAbajo = new TextureRegion[COLUMNAS];
        TextureRegion[] framesAtaqueIzquierda = new TextureRegion[COLUMNAS];
        TextureRegion[] framesAtaqueArriba = new TextureRegion[COLUMNAS];

        int index = 0;
        for (int i = 0; i < FILAS; i++) {
            index = 0;
            for (int j = 0; j < COLUMNAS; j++) {

                if (i == 0) {
                    walkFramesArriba[index] = tmp[i][j];
                    framesAtaqueDerecha[index] = tmp2[i][j];
                }
                if (i == 1) {
                    walkFramesDerecha[index] = tmp[i][j];
                    framesAtaqueAbajo[index] = tmp2[i][j];
                }
                if (i == 2) {
                    walkFramesAbajo[index] = tmp[i][j];
                    framesAtaqueIzquierda[index] = tmp2[i][j];
                }
                if (i == 3) {
                    walkFramesIzquierda[index] = tmp[i][j];
                    framesAtaqueArriba[index] = tmp2[i][j];
                }
                index++;
            }
        }

        animacionArriba = new Animation<TextureRegion>(0.2f, walkFramesArriba);
        animacionIzquierda = new Animation<TextureRegion>(0.2f, walkFramesIzquierda);
        animacionAbajo = new Animation<TextureRegion>(0.2f, walkFramesAbajo);
        animacionDerecha = new Animation<TextureRegion>(0.2f, walkFramesDerecha);

        animacionAtaqueDerecha = new Animation<TextureRegion>(0.2f, framesAtaqueDerecha);
        animacionAtaqueAbajo = new Animation<TextureRegion>(0.2f, framesAtaqueAbajo);
        animacionAtaqueIzquierda = new Animation<TextureRegion>(0.2f, framesAtaqueIzquierda);
        animacionAtaqueArriba = new Animation<TextureRegion>(0.2f, framesAtaqueArriba);

    }

    TextureRegion frameActual;

    public void animar(boolean animar) {
        if (animar) {
            tiempoAni += Gdx.graphics.getDeltaTime();
            try {
                frameActual = animacionActual.getKeyFrame(tiempoAni, true);
                dibujar(frameActual);
            }catch (Exception e){}
        }
    }

    public void movimiento() {

        if (!isMuerto()) {
            Utiles.batch.begin();
            if (enMovimiento || atacando) {
                animar(true);
                if (direccionAlien.equals("izquierda")) {
                    posicionFinal = "izquierda";
                }
                if (direccionAlien.equals("derecha")) {
                    posicionFinal = "derecha";
                }
                if (direccionAlien.equals("abajo")) {
                    posicionFinal = "abajo";
                }
                if (direccionAlien.equals("arriba")) {
                    posicionFinal = "arriba";
                }
            } else if (!enMovimiento && !atacando) {
                animar(false);
                if (posicionFinal.equals("")) {
                    dibujar(alienAbajo);
                }
                if (posicionFinal.equals("izquierda")) {
                    dibujar(alienIzquierda);
                }
                if (posicionFinal.equals("abajo")) {
                    dibujar(alienAbajo);
                }
                if (posicionFinal.equals("derecha")) {
                    dibujar(alienDerecha);
                }
                if (posicionFinal.equals("arriba")) {
                    dibujar(alienArriba);
                }
            }
            Utiles.batch.end();
        }

    }

    public void comportamiento(PersonajePrincipal jugador, PersonajePrincipal jugador2, Mapa mapa, HiloServidor hs, int posMemoria) {
        float newX = posicion.x, newY = posicion.y;
        float oldX = posicion.x, oldY = posicion.y;

        if(!isMuerto()) {
            if(circuloAlien.contains(jugador.getPosicion().x, jugador.getPosicion().y) || circuloAlien.contains(jugador2.getPosicion().x, jugador2.getPosicion().y)) {
                int nroJugador = 0;

                if(circuloAlien.contains(jugador.getPosicion().x, jugador.getPosicion().y)){
                    jugadorPelea = jugador;
                }else if(circuloAlien.contains(jugador2.getPosicion().x, jugador2.getPosicion().y)){
                    jugadorPelea = jugador2;
                }
                enMovimiento = true;

                if(rectanguloAlien.overlaps(jugador.getRectangulo()) || rectanguloAlien.overlaps(jugador2.getRectangulo())) {

                    if(rectanguloAlien.overlaps(jugador.getRectangulo())){
                        jugadorPelea = jugador;
                        nroJugador = 0;
                    }else if(rectanguloAlien.overlaps(jugador2.getRectangulo())){
                        jugadorPelea = jugador2;
                        nroJugador = 1;
                    }
                    isEnCombate = true;
                    atacando = true;
                    enMovimiento = false;
                    hacerDanio(jugadorPelea);

                    if(direccionAlien.equals("izquierda")) {
                        animacionActual = animacionAtaqueIzquierda;
                    }
                    if(direccionAlien.equals("abajo")) {
                        animacionActual = animacionAtaqueAbajo;
                    }
                    if(direccionAlien.equals("derecha")) {
                        animacionActual = animacionAtaqueDerecha;
                    }
                    if(direccionAlien.equals("arriba")) {
                        animacionActual = animacionAtaqueArriba;
                    }

                    hs.enviarMensajeATodos(idAlien + "-COMBATEA-" + nroJugador + "-" + direccionAlien);

                } else {
                    isEnCombate = false;
                    enMovimiento = true;
                    atacando = false;

                    if(jugador.getPosicion().x < this.posicion.x || jugador2.getPosicion().x < this.posicion.x) {
                        animacionActual = animacionIzquierda;
                        direccionAlien = "izquierda";
                        newX -=VELOCIDAD;
                        rectanguloAlien.setX(newX);

                        enColision = mapa.comprobarColision(this);

                        if(!enColision) {
                            posicion.x = newX;
                        }else {
                            posicion.x = oldX;
                        }
                        hs.enviarMensajeATodos(idAlien + "-MOVA-" + "IZQ-" + posicion.x + "-" + posicion.y);
                    }
                    if(jugador.getPosicion().y < this.posicion.y || jugador2.getPosicion().y < this.posicion.y) {
                        animacionActual = animacionAbajo;
                        direccionAlien = "abajo";
                        newY -=VELOCIDAD;
                        rectanguloAlien.setY(newY);

                        enColision = mapa.comprobarColision(this);

                        if(!enColision) {
                            posicion.y = newY;
                        }else {
                            posicion.y = oldY;
                        }
                        hs.enviarMensajeATodos(idAlien + "-MOVA-" + "ABAJO-" + posicion.x + "-" + posicion.y);
                    }
                    if(jugador.getPosicion().x > this.posicion.x || jugador2.getPosicion().x > this.posicion.x) {
                        animacionActual = animacionDerecha;
                        direccionAlien = "derecha";
                        newX +=VELOCIDAD;
                        rectanguloAlien.setX(newX);

                        enColision = mapa.comprobarColision(this);

                        if(!enColision) {
                            posicion.x = newX;
                        }else {
                            posicion.x = oldX;
                        }
                        hs.enviarMensajeATodos(idAlien + "-MOVA-" + "DER-" + posicion.x + "-" + posicion.y);
                    }
                    if(jugador.getPosicion().y > this.posicion.y || jugador2.getPosicion().y > this.posicion.y) {
                        animacionActual = animacionArriba;
                        direccionAlien = "arriba";
                        newY +=VELOCIDAD;
                        rectanguloAlien.setY(newY);

                        enColision = mapa.comprobarColision(this);

                        if(!enColision) {
                            posicion.y = newY;
                        }else {
                            posicion.y = oldY;
                        }
                        hs.enviarMensajeATodos(idAlien + "-MOVA-" + "ARRIBA-" + posicion.x + "-" + posicion.y);
                    }
                    circuloAlien.setX(posicion.x);
                    circuloAlien.setY(posicion.y);
                }
            }else {
                enMovimiento = false;
            }
            movimiento();
        }
    }

    public void mostrarColisiones() {
        Utiles.sr.circle(getCirculoAlien().x + (getWidth() / 2), getCirculoAlien().y + (getHeight() / 2), getCirculoAlien().radius);
        Utiles.sr.rect(getRectangulo().x, getRectangulo().y, getRectangulo().width, getRectangulo().height);
    }

    public Circle getCirculoAlien() {
        return circuloAlien;
    }

    public Rectangle getRectangulo() {
        return rectanguloAlien;
    }

    public boolean isMuerto() {
        return muerto;
    }

    public boolean isEnMovimiento() {
        return enMovimiento;
    }

    public void setMuerto(boolean muerto) {
        this.muerto = muerto;
    }

    public boolean isAtacando() {
        return atacando;
    }

    public int getIdAlien(){
        return this.idAlien;
    }

    public void setCombateOnline(boolean estado){
        this.combateOnline = estado;
    }

    public boolean getCombatOnline(){
        return this.combateOnline;
    }

    public boolean getEnCombate(){
        return this.isEnCombate;
    }

    public void setEnCombate(boolean estado){
        this.isEnCombate = estado;
    }


    public void setAtacando(boolean atacando) {
        this.atacando = atacando;
    }

}
