package mx.rmr.spaceinvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

class PantallaMario extends Pantalla{

    private final Juego juego;

    //MAPA
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;
    private Music audioFondo;
    private Sound efecto;


    public PantallaMario(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        cargarMapa();
        Gdx.input.setInputProcessor(new ProcessadorEntrada());
    }

    private void cargarMapa() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("mapaMario.tmx", TiledMap.class);
        manager.load("marioBros.mp3", Music.class);
        manager.load("moneda.mp3", Sound.class);
        manager.finishLoading();
        mapa = manager.get("mapaMario.tmx");
        rendererMapa = new OrthogonalTiledMapRenderer(mapa);

        audioFondo = manager.get("marioBros.mp3");
        audioFondo.setLooping(true);
        audioFondo.play();

        efecto = manager.get("moneda.mp3");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        rendererMapa.setView(camara);
        rendererMapa.render();
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

    private class ProcessadorEntrada implements InputProcessor {
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
            efecto.play();
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
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
}
