package mx.rmr.spaceinvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

class PantallaSpaceInvaders extends Pantalla{

    private final Juego juego;
    int COLUMNAS = 11;
    int RENGLONES = 5;

    //aliens
    private Array<Alien> arrAliens;
    private Texture texturaAlien;
    private final float TIEMPO_PASO = 0.5f;
    private final int MAX_PASOS = 32;
    private int numeroPasos = MAX_PASOS/2;
    private Direccion direccion = Direccion.DERECHA;
    float paso = ANCHO*0.2f/MAX_PASOS;
    private float timerAlienMover = 0;

    //Nave
    private Nave nave;
    private Texture texturaNave;
    private Movimiento movimiento = Movimiento.QUIETO;

    //Bala
    private Bala bala;
    private Texture texturaBala;

    //MARCADOR
    private Marcador marcador;


    public PantallaSpaceInvaders(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearAliens();
        crearNave();
        creatMarcador();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void creatMarcador() {
        marcador = new Marcador(0.2f*ANCHO, 0.9f*ALTO);
    }

    private void crearNave() {
        nave = new Nave(texturaNave, ANCHO/2, ALTO*0.05f);
    }

    private void cargarTexturas() {
        texturaAlien = new Texture("enemigoArriba.png");
        texturaNave = new Texture("nave.png");
        texturaBala = new Texture("bala.png");
    }

    private void crearAliens() {
        arrAliens = new Array<>(COLUMNAS*RENGLONES);
        float dx = (ANCHO*0.8f) / COLUMNAS;
        float dy = (ALTO*0.4f) / RENGLONES;

        for (int x = 0; x < COLUMNAS; x++){
            for (int y = 0; y < RENGLONES; y++){
                Alien alien = new Alien(texturaAlien, x*dx + ANCHO*0.1f, y*dy + ALTO*0.45f);
                arrAliens.add(alien);
            }
        }
    }

    @Override
    public void render(float delta) {

        //ACTUALIZACIONES

        actualizar(delta);

        //DIBUJAR
        borrarPantalla(0, 0, 0);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        for (Alien alien : arrAliens){
            alien.render(batch);
        }
        nave.render(batch);

        if(bala!= null)  bala.render(batch);

        marcador.render(batch);

        batch.end();
    }

    private void actualizar(float delta) {
        moverNave();
        moverEnemigos(delta);
        moverBala(delta);
        probarColisiones();
    }

    private void moverEnemigos(float delta) {
        timerAlienMover += delta;
        if (timerAlienMover>=0.5) {
            timerAlienMover = 0;
            float pasoDir = direccion == Direccion.DERECHA? paso: -paso;
            for(Alien alien: arrAliens){
                alien.mover(pasoDir);
            }
            numeroPasos++;
            if(numeroPasos>=MAX_PASOS){
                direccion = direccion==Direccion.DERECHA? Direccion.IZQUIERDA: Direccion.DERECHA;
                numeroPasos = 0;

                //bajan un paso
                float pasoAbajo = ALTO*0.4F / RENGLONES;
                for(Alien alien:arrAliens){
                    alien.bajar(pasoAbajo);
                }
            }
        }
    }

    private void probarColisiones() {
        if(bala!=null) {
            for (int i = arrAliens.size - 1; i >= 0; i--) {
                Alien alien = arrAliens.get(i);
                Rectangle rectAlien = alien.sprite.getBoundingRectangle();
                Rectangle rectBala = bala.sprite.getBoundingRectangle();

                if (rectAlien.overlaps(rectBala)) {
                    arrAliens.removeIndex(i);
                    bala = null;
                    marcador.marcar(10);
                    break;
                }
            }
        }
    }

    private void moverBala(float delta) {
        if(bala!=null) {
            bala.mover(delta);
            if (bala.sprite.getY() > ALTO) bala = null;
        }
    }

    private void moverNave() {
        switch (movimiento){
            case DERECHA:
                nave.mover(10);
                break;
            case IZQUIERDA:
                nave.mover(-10);
                break;
            default:
                break;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private class ProcesadorEntrada implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 v = new Vector3(screenX, screenY, 0);
            camara.unproject(v);
            if(v.y<ALTO/2){
                float xBala = nave.sprite.getX() + nave.sprite.getWidth()/2 - texturaBala.getWidth()/2;
                float yBala = nave.sprite.getY() + nave.sprite.getHeight();
                if(bala == null) bala = new Bala(texturaBala, xBala, yBala);
            }else {
                if (v.x >= ANCHO / 2) {
                    movimiento = Movimiento.DERECHA;
                } else {
                    movimiento = Movimiento.IZQUIERDA;
                }
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            movimiento = Movimiento.QUIETO;
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }

    private enum Movimiento{
        DERECHA,
        IZQUIERDA,
        QUIETO
    }
}
