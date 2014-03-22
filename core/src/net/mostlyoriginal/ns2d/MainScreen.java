package net.mostlyoriginal.ns2d;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.mostlyoriginal.ns2d.system.CameraSystem;

/**
 * @author Daan van Yperen
 */
public class MainScreen implements Screen {

    OrthographicCamera camera;

    public MainScreen() {
        G.screen = this;

        G.world = new World();

        G.world.setSystem(new CameraSystem());

        G.world.initialize();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
  		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        G.world.setDelta(delta);
        G.world.process();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

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
}
